package org.nearbyshops.RESTEndpoints.RESTEndpointsOrder;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.ModelEndpoint.OrderEndPoint;
import org.nearbyshops.Model.ModelRoles.User;

import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;



@Singleton
@Path("/api/Order")
public class OrderResource {




	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOrder(Order order, @QueryParam("CartID") int cartID)
	{

		int orderId = Globals.pladeOrderDAO.placeOrderNew(order, cartID);

		if (orderId != -1) {

			Order orderResult = Globals.orderService.getOrderDetails(orderId);



			// send push notification
			if(GlobalConstants.push_notification_provider==1)
			{

				// send notification using fcm
				String topic = GlobalConstants.market_id_for_fcm + "shop_" + orderResult.getShopID();

				// See documentation on defining a message payload.
				Message message = Message.builder()
						.setNotification(new Notification("Order Received", "You have received an order. Please check the order and respond to the customer !"))
						.putData("notification_type",GlobalConstants.NOTIFICATION_ORDER_RECIEVED)
						.setTopic(topic)
						.build();




				System.out.println("Topic : " + topic);


				try {


					String response = FirebaseMessaging.getInstance().send(message);
					System.out.println("Sent Notification : " + response);


				} catch (FirebaseMessagingException e) {
					e.printStackTrace();
				}



			}




			return Response.status(Status.CREATED)
					.build();


		}
		else {

			return Response.status(Status.NOT_MODIFIED)
					.build();
		}

	}





	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_END_USER})
	public Response getOrders(
			@QueryParam("FilterOrdersByShopID") boolean filterOrdersByShopID,
			@QueryParam("FilterOrdersByUserID") boolean filterOrdersByUserID,
			@QueryParam("GetDeliveryProfile") boolean getDeliveryProfile,
			@QueryParam("DeliveryGuyID")Integer deliveryGuyID,
			@QueryParam("PickFromShop") Boolean pickFromShop,
			@QueryParam("StatusHomeDelivery")Integer homeDeliveryStatus,
			@QueryParam("StatusPickFromShopStatus")Integer pickFromShopStatus,
			@QueryParam("latCenter")Double latCenter, @QueryParam("lonCenter")Double lonCenter,
			@QueryParam("PendingOrders") Boolean pendingOrders,
			@QueryParam("SearchString") String searchString,
			@QueryParam("SortBy") String sortBy,
			@QueryParam("Limit")int limit, @QueryParam("Offset")int offset,
			@QueryParam("GetRowCount")boolean getRowCount,
			@QueryParam("MetadataOnly")boolean getOnlyMetaData
	)
	{

//		@QueryParam("ShopID")Integer shopID
//		@QueryParam("UserID")Integer userID
//		@QueryParam("DeliveryGuyID")Integer deliveryGuyID

		Integer shopID = null;
		Integer userID = null;
//		Integer deliveryGuyID = null;




		// *********************** second Implementation

		User user = (User) Globals.accountApproved;


		if(filterOrdersByShopID)
		{
			if(user.getRole()==GlobalConstants.ROLE_SHOP_ADMIN_CODE)
			{
				shopID = Globals.daoUserUtility.getShopIDForShopAdmin(user.getUserID());
			}
			else if(user.getRole()==GlobalConstants.ROLE_SHOP_STAFF_CODE)
			{
				shopID = Globals.daoUserUtility.getShopIDforShopStaff(user.getUserID());
			}

		}



		if(filterOrdersByUserID)
		{
			// when an endpoint requires you to show orders only for given user. this is required when staff member is logged in as end user in end user app
			userID = user.getUserID();
		}



		if(limit >= GlobalConstants.max_limit)
		{
			limit = GlobalConstants.max_limit;
		}



		OrderEndPoint endpoint  = null;


		if(getDeliveryProfile)
		{
			endpoint = Globals.orderService.getOrdersListWithDeliveryProfile(
					userID,shopID, pickFromShop,
					homeDeliveryStatus,pickFromShopStatus,
					deliveryGuyID,
					latCenter,lonCenter,
					pendingOrders,
					searchString,
					sortBy,limit,offset,
					getRowCount,getOnlyMetaData);


		}
		else
		{
			endpoint = Globals.orderService.getOrdersList(
					userID,shopID, pickFromShop,
					homeDeliveryStatus,pickFromShopStatus,
					deliveryGuyID,
					pendingOrders,
					searchString,
					sortBy,limit,offset,
					getRowCount,getOnlyMetaData);


		}






		endpoint.setLimit(limit);
		endpoint.setOffset(offset);
		endpoint.setMax_limit(GlobalConstants.max_limit);



//
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		//Marker

		return Response.status(Status.OK)
				.entity(endpoint)
				.build();
	}







	// requires authentication by the Distributor
	@PUT
	@Path("/CancelByUser/{OrderID}")
	@RolesAllowed({GlobalConstants.ROLE_END_USER})
	public Response cancelledByShop(@PathParam("OrderID")int orderID)
	{
		Order order = Globals.daoOrderUtility.readStatusHomeDelivery(orderID);

		User user = (User) Globals.accountApproved;



		int rowCount = Globals.orderService.orderCancelledByEndUser(orderID);

		if(rowCount >= 1)
		{

			return Response.status(Status.OK)
					.build();
		}



		return Response.status(Status.NOT_MODIFIED)
				.build();

	}



}
