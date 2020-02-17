package org.nearbyshops.RESTEndpoints.RESTEndpointsOrder;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.nearbyshops.DAOs.DAOPushNotifications.DAOOneSignal;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.ModelRoles.User;

import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;


@Singleton
@Path("/api/Order/DeliveryGuySelf")
public class OrderEndpointDeliveryGuySelf {




	@PUT
	@Path("/StartPickup/{OrderID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_DELIVERY_GUY_SELF,GlobalConstants.ROLE_DELIVERY_GUY})
	public Response startPickup(@PathParam("OrderID")int orderID)
	{

		int rowCount = 0;


		int deliveryGuyID = ((User)Globals.accountApproved).getUserID();


		rowCount = Globals.daoOrderDeliveryGuy.pickupOrder(orderID,deliveryGuyID);



		if(rowCount>=1)
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
	@Path("/AcceptOrder/{OrderID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_DELIVERY_GUY_SELF,GlobalConstants.ROLE_DELIVERY_GUY})
	public Response acceptOrder(@PathParam("OrderID")int orderID)
	{

//		Order order = Globals.orderService.readStatusHomeDelivery(orderID);
//		User user = (User) Globals.accountApproved;


//		order.setStatusHomeDelivery(OrderStatusHomeDelivery.OUT_FOR_DELIVERY);
		int rowCount = Globals.daoOrderDeliveryGuy.acceptOrder(orderID);






//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}


		if(rowCount >= 1)
		{



			Order orderResult = Globals.orderService.getOrderDetails(orderID);


			// See documentation on defining a message payload.
			Message messageEndUser = Message.builder()
					.setNotification(new Notification("Out For Delivery", "Order number " + String.valueOf(orderID) + " is out for Delivery !"))
					.setTopic("end_user_" + orderResult.getEndUserID())
					.build();


			System.out.println("Topic : " + "end_user_" + orderResult.getEndUserID());


			try {


				String responseEndUser = FirebaseMessaging.getInstance().send(messageEndUser);
				System.out.println("Sent Notification to EndUser: " + responseEndUser);


			} catch (FirebaseMessagingException e) {
				e.printStackTrace();
			}



//				Globals.broadcastMessageToEndUser("Order Out For Delivery (Home Delivery)","Order Number " + String.valueOf(orderID) + " (HD) is Out for Delivery !",order.getEndUserID());
			return Response.status(Status.OK)
					.build();
		}


		return Response.status(Status.NOT_MODIFIED)
				.build();
	}





	@PUT
	@Path("/DeclineOrder/{OrderID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_DELIVERY_GUY_SELF,GlobalConstants.ROLE_DELIVERY_GUY})
	public Response declineOrder(@PathParam("OrderID")int orderID)
	{

//		Order order = Globals.orderService.readStatusHomeDelivery(orderID);
//		User user = (User) Globals.accountApproved;
//		order.setStatusHomeDelivery(OrderStatusHomeDelivery.OUT_FOR_DELIVERY);


		int rowCount = Globals.daoOrderDeliveryGuy.declineOrder(orderID);

		if(rowCount >= 1)
		{

//				Globals.broadcastMessageToEndUser("Order Out For Delivery (Home Delivery)","Order Number " + String.valueOf(orderID) + " (HD) is Out for Delivery !",order.getEndUserID());
			return Response.status(Status.OK)
					.build();
		}



		return Response.status(Status.NOT_MODIFIED)
				.build();


	}





	@PUT
	@Path("/ReturnPackage/{OrderID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_DELIVERY_GUY_SELF,GlobalConstants.ROLE_DELIVERY_GUY})
	public Response returnOrderPackage(@PathParam("OrderID")int orderID)
	{
//		Order order = Globals.orderService.readStatusHomeDelivery(orderID);

		int rowCount = Globals.daoOrderDeliveryGuy.returnOrder(orderID);

//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}


		if(rowCount >= 1)
		{
			Order orderResult = Globals.orderService.getOrderDetails(orderID);

//			String shopAdminPlayerID = oneSignalNotifications.getPlayerIDforShopAdmin(orderResult.getShopID());
			ArrayList<String> playerIDs =  Globals.oneSignalNotifications.getPlayerIDsForShopStaff(orderResult.getShopID(),
					null,null,null,null,true);


//			playerIDs.add(shopAdminPlayerID);



			Globals.oneSignalNotifications.sendNotificationToUser(
					playerIDs,
					GlobalConstants.ONE_SIGNAL_APP_ID_SHOP_OWNER_APP,
					GlobalConstants.ONE_SIGNAL_API_KEY_SHOP_OWNER_APP,
					"https://i1.wp.com/nearbyshops.org/wp-content/uploads/2017/02/cropped-backdrop_play_store-1.png?w=250&ssl=1",
					null,
					null,
					10,
					"Order Return Requested",
					"Return is requested for order number " + String.valueOf(orderID) + " !",
					1,
					DAOOneSignal.ORDER_RETURNED,
					null
			);


			return Response.status(Status.OK)
					.build();
		}



		return Response.status(Status.NOT_MODIFIED)
				.build();


	}





	@PUT
	@Path("/HandoverToUser/{OrderID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@RolesAllowed({GlobalConstants.ROLE_DELIVERY_GUY_SELF,GlobalConstants.ROLE_DELIVERY_GUY})
	public Response deliverOrder(@PathParam("OrderID")int orderID)
	{

//		Order order = Globals.orderService.readStatusHomeDelivery(orderID);
//		order.setStatusHomeDelivery(OrderStatusHomeDelivery.HANDOVER_REQUESTED);

		int rowCount = Globals.daoOrderDeliveryGuy.deliverOrder(orderID);

//
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		if (rowCount >= 1) {


			Order orderResult = Globals.orderService.getOrderDetails(orderID);

//			String shopAdminPlayerID = oneSignalNotifications.getPlayerIDforShopAdmin(orderResult.getShopID());
			ArrayList<String> playerIDs =  Globals.oneSignalNotifications.getPlayerIDsForShopStaff(orderResult.getShopID(),
					null,null,null,true,null);


//			playerIDs.add(shopAdminPlayerID);



			Globals.oneSignalNotifications.sendNotificationToUser(
					playerIDs,
					GlobalConstants.ONE_SIGNAL_APP_ID_SHOP_OWNER_APP,
					GlobalConstants.ONE_SIGNAL_API_KEY_SHOP_OWNER_APP,
					"https://i1.wp.com/nearbyshops.org/wp-content/uploads/2017/02/cropped-backdrop_play_store-1.png?w=250&ssl=1",
					null,
					null,
					10,
					"Order Delivered",
					"Order number " + String.valueOf(orderID) + " is Delivered !",
					1,
					DAOOneSignal.ORDER_RETURNED,
					null
			);



			return Response.status(Status.OK)
					.build();
		}


		return Response.status(Status.NOT_MODIFIED)
				.build();

	}




}
