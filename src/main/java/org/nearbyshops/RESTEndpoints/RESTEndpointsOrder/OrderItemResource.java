package org.nearbyshops.RESTEndpoints.RESTEndpointsOrder;

import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.ModelEndpoint.OrderItemEndPoint;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


@Path("/api/OrderItem")
public class OrderItemResource {


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN, GlobalConstants.ROLE_SHOP_STAFF, GlobalConstants.ROLE_END_USER})
	public Response getOrderItem(@QueryParam("OrderID")Integer orderID,
                                 @QueryParam("ItemID")Integer itemID,
								 @QueryParam("ShopID")Integer shopID,
								 @QueryParam("latCenter")double latCenter, @QueryParam("lonCenter")double lonCenter,
                                 @QueryParam("SearchString")String searchString,
                                 @QueryParam("SortBy") String sortBy,
                                 @QueryParam("Limit")Integer limit, @QueryParam("Offset")int offset)
	{



		if(limit!=null && limit >= GlobalConstants.max_limit)
		{
			limit = GlobalConstants.max_limit;
		}



		 OrderItemEndPoint endPoint = Globals.orderItemService.getOrderItem(
													 orderID,itemID,
													 searchString,sortBy,limit,offset
											 );



		endPoint.setOrderDetails(Globals.daoOrderUtility.getExtraDetailsForOrderDetailsScreen(orderID));

		if(shopID!=null)
		{
			endPoint.setShopDetails(Globals.shopDAO.getShopDetails(shopID,latCenter,lonCenter));
		}



		if(limit!=null)
		{
			endPoint.setLimit(limit);
			endPoint.setOffset(offset);
			endPoint.setMax_limit(GlobalConstants.max_limit);
		}




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


}
