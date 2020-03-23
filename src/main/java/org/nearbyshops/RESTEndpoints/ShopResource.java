package org.nearbyshops.RESTEndpoints;

import net.coobird.thumbnailator.Thumbnails;
import org.nearbyshops.DAOs.DAOBilling.DAOAddBalance;
import org.nearbyshops.DAOs.ShopDAO;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Image;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
import org.nearbyshops.Model.ModelRoles.StaffPermissions;
import org.nearbyshops.Model.ModelRoles.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;


@Path("/api/v1/Shop")
@Produces(MediaType.APPLICATION_JSON)
public class ShopResource {



	private ShopDAO shopDAO = Globals.shopDAO;
	private DAOAddBalance addBalanceDAO = Globals.daoAddBalance;



	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_END_USER})
	public Response createShop(Shop shop)
	{

		User user = (User) Globals.accountApproved;

		int idOfInsertedRow = shopDAO.insertShop(shop,user.getUserID());
		shop.setShopID(idOfInsertedRow);
		
		
		if(idOfInsertedRow >=1)
		{
			return Response.status(Status.CREATED)
					.entity(shop)
					.build();
		}
		else {


			return Response.status(Status.NOT_MODIFIED)
					.build();
		}
	}
	




	@PUT
	@Path("/UpdateByAdmin/{ShopID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_ADMIN, GlobalConstants.ROLE_STAFF})
	public Response updateShopByAdmin(Shop shop, @PathParam("ShopID")int ShopID)
	{


		User user = (User) Globals.accountApproved;

		if(user.getRole()==GlobalConstants.ROLE_STAFF_CODE) {

			StaffPermissions permissions = Globals.daoStaff.getStaffPermissions(user.getUserID());

			if (!permissions.isPermitApproveShops())
			{
				// the staff member doesnt have persmission to update shop
				throw new ForbiddenException("Not Permitted");
			}
		}


		shop.setShopID(ShopID);
		int rowCount = shopDAO.updateShopByAdmin(shop);


		if(rowCount >= 1)
		{
			return Response.status(Status.OK)
					.build();
		}
		else
		{
			return Response.status(Status.NOT_MODIFIED)
					.build();
		}

	}



	//, @PathParam("ShopID")int ShopID

	@PUT
	@Path("/UpdateBySelf")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
	public Response updateShopByOwner(Shop shop)
	{


		shop.setShopAdminID(((User)Globals.accountApproved).getUserID());

		int rowCount = shopDAO.updateShopBySelf(shop);

		if(rowCount >= 1)
		{
			return Response.status(Status.OK)
					.build();
		}
		else
		{
			return Response.status(Status.NOT_MODIFIED)
					.build();
		}
	}






	@PUT
	@Path("/SetShopOpen")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
	public Response setShopOpen()
	{

		int shopAdminID = ((User)Globals.accountApproved).getUserID();

		int rowCount = shopDAO.setShopOpen(true,shopAdminID);


		if(rowCount >= 1)
		{
			return Response.status(Status.OK)
					.build();
		}
		else
		{
			return Response.status(Status.NOT_MODIFIED)
					.build();
		}
	}





	@PUT
	@Path("/SetShopClosed")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
	public Response setShopClosed()
	{

		int shopAdminID = ((User)Globals.accountApproved).getUserID();

		int rowCount = shopDAO.setShopOpen(false,shopAdminID);


		if(rowCount >= 1)
		{
			return Response.status(Status.OK)
					.build();
		}

		return Response.status(Status.NOT_MODIFIED)
				.build();

	}






	@DELETE
	@Path("/{ShopID}")
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN, GlobalConstants.ROLE_ADMIN, GlobalConstants.ROLE_STAFF})
	public Response deleteShop(@PathParam("ShopID")int shopID)
	{


		User user = (User) Globals.accountApproved;

		if(user.getRole()==GlobalConstants.ROLE_SHOP_ADMIN_CODE)
		{
			shopID = Globals.daoUserUtility.getShopIDForShopAdmin(user.getUserID());
		}
		else if(user.getRole()==GlobalConstants.ROLE_STAFF_CODE)
		{

			StaffPermissions permissions = Globals.daoStaff.getStaffPermissions(user.getUserID());

			if(!permissions.isPermitApproveShops())
			{
				return Response.status(Status.BAD_REQUEST)
						.build();
			}

		}





		int rowCount = shopDAO.deleteShop(shopID);
	
		
		if(rowCount>=1)
		{

			return Response.status(Status.OK)
					.build();
		}
		else if(rowCount == 0)
		{

			return Response.status(Status.NOT_MODIFIED)
					.build();
		}
		
		return null;

	}




	@GET
	@Path("/GetShopDetails/{ShopID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getShopDetails(@PathParam("ShopID")int shopID,
								   @QueryParam("latCenter")double latCenter, @QueryParam("lonCenter")double lonCenter)
	{
		Shop shop = shopDAO.getShopDetails(shopID,latCenter,lonCenter);

		if(shop!= null)
		{
			return Response.status(Status.OK)
					.entity(shop)
					.build();

		}
		else
		{

			return Response.status(Status.NO_CONTENT)
					.build();

		}
	}






	@GET
	@Path("/GetShopForShopAdmin")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
	public Response getShopForShopAdmin()
	{
		User user = ((User) Globals.accountApproved);


//		System.out.println("User Role " + user.getRole() + " User :" + user.getEmail());
		Shop shop = Globals.daoUserUtility.getShopForShopAdmin(user.getUserID());



		if(shop!= null)
		{
			return Response.status(Status.OK)
					.entity(shop)
					.build();

		} else
		{

			return Response.status(Status.NO_CONTENT)
					.build();
		}


	}




	@GET
	@Path("/GetShopForShopStaff")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_SHOP_STAFF})
	public Response getShopForShopStaff()
	{
		User user = ((User) Globals.accountApproved);


//		int shopID = Globals.daoUserUtility.getShopIDforShopStaff(user.getUserID());
//		Shop shop = Globals.shopDAO.getShopDetails(shopID,0d,0d);

		Shop shop = Globals.daoUserUtility.getShopForShopStaff(user.getUserID());


		if(shop!= null)
		{
			return Response.status(Status.OK)
					.entity(shop)
					.build();

		} else
		{

			return Response.status(Status.NO_CONTENT)
					.build();
		}


	}







	//	@PUT
//	@Path("/BecomeASeller")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@RolesAllowed({GlobalConstants.ROLE_END_USER})
//	public Response becomeASeller()
//	{
//		// this is deprecated and no longer required ... create shop method has replaced it
//
//		User user = (User) Globals.accountApproved;
//
//		int rowCount = Globals.daoShopStaff.becomeASeller(user.getUserID());
//
//
//		if(rowCount >= 1)
//		{
//			return Response.status(Response.Status.OK)
//					.build();
//		}
//		else {
//
//			return Response.status(Status.NOT_MODIFIED)
//					.build();
//		}
//	}




	@PUT
	@Path("/AddBalance/{ShopAdminID}/{AmountToAdd}")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_ADMIN,GlobalConstants.ROLE_STAFF})
	public Response addBalance(@PathParam("ShopAdminID") int shopAdminID, @PathParam("AmountToAdd") double amountToAdd)
	{

		User user = ((User)Globals.accountApproved);
		StaffPermissions permissions = Globals.daoStaff.getStaffPermissions(user.getUserID());

		// check staff permissions

		if(user.getRole()!=GlobalConstants.ROLE_ADMIN_CODE)
		{
			if(permissions==null || !permissions.isPermitApproveShops())
			{
				return Response.status(Response.Status.EXPECTATION_FAILED)
						.build();
			}
		}




		int rowCount = addBalanceDAO.add_balance_to_shop(shopAdminID,amountToAdd);


		if(rowCount >= 1)
		{

			return Response.status(Response.Status.OK)
					.build();


		}
		else {

			return Response.status(Status.NOT_MODIFIED)
					.build();
		}


	}







	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/QuerySimple")
	public Response getShopListSimple(
			@QueryParam("UnderReview")Boolean underReview,
            @QueryParam("Enabled")Boolean enabled, @QueryParam("Waitlisted") Boolean waitlisted,
            @QueryParam("latCenter")Double latCenter, @QueryParam("lonCenter")Double lonCenter,
            @QueryParam("SearchString") String searchString,
            @QueryParam("SortBy") String sortBy,
            @QueryParam("Limit") int limit, @QueryParam("Offset") int offset,
			@QueryParam("GetRowCount")boolean getRowCount,
			@QueryParam("MetadataOnly")boolean getOnlyMetaData
	)
	{


		if(limit >= GlobalConstants.max_limit)
		{
			limit = GlobalConstants.max_limit;
		}



		ShopEndPoint endPoint = shopDAO.getShopsListQuerySimple(
									underReview,
									enabled,waitlisted,
									latCenter,lonCenter,
									searchString, sortBy,
									limit,offset, getRowCount,getOnlyMetaData
		);


		endPoint.setLimit(limit);
		endPoint.setMax_limit(GlobalConstants.max_limit);
		endPoint.setOffset(offset);


		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/



		//Marker
		return Response.status(Status.OK)
				.entity(endPoint)
				.build();
	}







	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getShops(
            @QueryParam("LeafNodeItemCategoryID")Integer itemCategoryID,
            @QueryParam("latCenter")Double latCenter, @QueryParam("lonCenter")Double lonCenter,
            @QueryParam("deliveryRangeMax")Double deliveryRangeMax,
            @QueryParam("deliveryRangeMin")Double deliveryRangeMin,
            @QueryParam("proximity")Double proximity,
            @QueryParam("SearchString") String searchString,
            @QueryParam("SortBy") String sortBy,
            @QueryParam("Limit") int limit, @QueryParam("Offset") int offset,
			@QueryParam("GetRowCount")boolean getRowCount,
			@QueryParam("MetadataOnly")boolean getOnlyMetaData
	)
	{



		if(limit >= GlobalConstants.max_limit)
		{
			limit = GlobalConstants.max_limit;
		}



		ShopEndPoint endPoint = shopDAO.getShopListQueryJoin(itemCategoryID,
				latCenter,lonCenter,
				deliveryRangeMin,deliveryRangeMax,proximity,
				searchString,sortBy,
				limit,offset,
				getRowCount,getOnlyMetaData);




		endPoint.setLimit(limit);
		endPoint.setOffset(offset);
		endPoint.setMax_limit(GlobalConstants.max_limit);



		//Marker
		return Response.status(Status.OK)
				.entity(endPoint)
				.build();
	}





	@GET
	@Path("/FilterByItemCat/{ItemCategoryID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response filterShopsByItemCategory(
            @PathParam("ItemCategoryID")Integer itemCategoryID,
            @QueryParam("DistributorID")Integer distributorID,
            @QueryParam("latCenter")Double latCenter, @QueryParam("lonCenter")Double lonCenter,
            @QueryParam("deliveryRangeMax")Double deliveryRangeMax,
            @QueryParam("deliveryRangeMin")Double deliveryRangeMin,
            @QueryParam("proximity")Double proximity,
            @QueryParam("SortBy") String sortBy,
            @QueryParam("Limit") int limit, @QueryParam("Offset") int offset,
			@QueryParam("GetRowCount")boolean getRowCount,
			@QueryParam("MetadataOnly")boolean getOnlyMetaData
	)
	{



		if(limit >= GlobalConstants.max_limit)
		{
			limit = GlobalConstants.max_limit;
		}


		ShopEndPoint endPoint = shopDAO.filterShopsByItemCategory(
				itemCategoryID,
				latCenter,lonCenter,
				deliveryRangeMin,deliveryRangeMax,
				proximity,sortBy,
				limit,offset,
				getRowCount,getOnlyMetaData
		);



		endPoint.setLimit(limit);
		endPoint.setOffset(offset);
		endPoint.setMax_limit(GlobalConstants.max_limit);


/*
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/


		//Marker
		return Response.status(Status.OK)
				.entity(endPoint)
				.build();

	}









//	@QueryParam("SearchString") String searchString,
//	@QueryParam("SortBy") String sortBy,
//	@QueryParam("Limit") Integer limit, @QueryParam("Offset") Integer offset,

	@GET
	@Path("/ForShopFilters")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getShopForFilters(
			@QueryParam("latCenter")Double latCenter, @QueryParam("lonCenter")Double lonCenter,
			@QueryParam("deliveryRangeMax")Double deliveryRangeMax,
			@QueryParam("deliveryRangeMin")Double deliveryRangeMin,
			@QueryParam("proximity")Double proximity,
			@QueryParam("GetRowCount")boolean getRowCount,
			@QueryParam("MetadataOnly")boolean getOnlyMetaData
	)
	{


		shopDAO.getShopsForShopFiltersPrepared(latCenter,lonCenter, deliveryRangeMin,deliveryRangeMax, proximity);

		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/



		//Marker
		return Response.status(Status.OK)
//				.entity(endPoint)
				.build();
	}



	// Image MEthods

	private static final java.nio.file.Path BASE_DIR = Paths.get("./data/images/Shop");
	private static final double MAX_IMAGE_SIZE_MB = 2;




	@POST
	@Path("/Image")
	@Consumes({MediaType.APPLICATION_OCTET_STREAM})
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN, GlobalConstants.ROLE_ADMIN})
	public Response uploadImage(InputStream in, @HeaderParam("Content-Length") long fileSize,
                                @QueryParam("PreviousImageName") String previousImageName
	) throws Exception
	{


		if(previousImageName!=null)
		{
			Files.deleteIfExists(BASE_DIR.resolve(previousImageName));
			Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + previousImageName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + previousImageName + ".jpg"));
		}


		File theDir = new File(BASE_DIR.toString());

		// if the directory does not exist, create it
		if (!theDir.exists()) {

//			System.out.println("Creating directory: " + BASE_DIR.toString());

			boolean result = false;

			try{
				theDir.mkdir();
				result = true;
			}
			catch(Exception se){
				//handle it
			}
			if(result) {
//				System.out.println("DIR created");
			}
		}



		String fileName = "" + System.currentTimeMillis();

		// Copy the file to its location.
		long filesize = Files.copy(in, BASE_DIR.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

		if(filesize > MAX_IMAGE_SIZE_MB * 1048 * 1024)
		{
			// delete file if it exceeds the file size limit
			Files.deleteIfExists(BASE_DIR.resolve(fileName));

			return Response.status(Status.EXPECTATION_FAILED).build();
		}


		createThumbnails(fileName);


		Image image = new Image();
		image.setPath(fileName);

		// Return a 201 Created response with the appropriate Location header.

		return Response.status(Status.CREATED).location(URI.create("/api/Images/" + fileName)).entity(image).build();
	}



	private void createThumbnails(String filename)
	{
		try {

			Thumbnails.of(BASE_DIR.toString() + "/" + filename)
					.size(300,300)
					.outputFormat("jpg")
					.toFile(new File(BASE_DIR.toString() + "/" + "three_hundred_" + filename));

			//.toFile(new File("five-" + filename + ".jpg"));

			//.toFiles(Rename.PREFIX_DOT_THUMBNAIL);


			Thumbnails.of(BASE_DIR.toString() + "/" + filename)
					.size(500,500)
					.outputFormat("jpg")
					.toFile(new File(BASE_DIR.toString() + "/" + "five_hundred_" + filename));



		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	@GET
	@Path("/Image/{name}")
	@Produces("image/jpeg")
	public InputStream getImage(@PathParam("name") String fileName) {

		//fileName += ".jpg";
		java.nio.file.Path dest = BASE_DIR.resolve(fileName);

		if (!Files.exists(dest)) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}


		try {
			return Files.newInputStream(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}




	@DELETE
	@Path("/Image/{name}")
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN,GlobalConstants.ROLE_ADMIN})
	public Response deleteImageFile(@PathParam("name")String fileName)
	{

		boolean deleteStatus = false;

		Response response;

//		System.out.println("Filename: " + fileName);


		try {


			//Files.delete(BASE_DIR.resolve(fileName));
			deleteStatus = Files.deleteIfExists(BASE_DIR.resolve(fileName));

			// delete thumbnails
			Files.deleteIfExists(BASE_DIR.resolve("three_hundred_" + fileName + ".jpg"));
			Files.deleteIfExists(BASE_DIR.resolve("five_hundred_" + fileName + ".jpg"));


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		if(!deleteStatus)
		{
			response = Response.status(Status.NOT_MODIFIED).build();

		}else
		{
			response = Response.status(Status.OK).build();
		}

		return response;
	}
	
}
