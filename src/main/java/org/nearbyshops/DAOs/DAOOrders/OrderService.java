package org.nearbyshops.DAOs.DAOOrders;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.ModelDelivery.DeliveryAddress;

import org.nearbyshops.Model.ModelEndpoint.OrderEndPoint;
import org.nearbyshops.Model.ModelOrderStatus.OrderStatusHomeDelivery;
import org.nearbyshops.Model.ModelRoles.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by sumeet on 7/6/16.
 */


public class OrderService {


    private HikariDataSource dataSource = Globals.getDataSource();




    public Order getOrderDetails(int orderID)
    {

        String query = "SELECT "

                 + Order.ORDER_ID + ","
                 + Order.DELIVERY_ADDRESS_ID + ","
                 + Order.DATE_TIME_PLACED + ","

                 + Order.DELIVERY_CHARGES + ","
//                 + Order.DELIVERY_RECEIVED + ","
//                 + Order.PAYMENT_RECEIVED + ","

                 + Order.DELIVERY_GUY_SELF_ID + ","
                 + Order.END_USER_ID + ","
                 + Order.PICK_FROM_SHOP + ","

                + Order.SHOP_ID + ","
                + Order.STATUS_HOME_DELIVERY + ","
                + Order.STATUS_PICK_FROM_SHOP + ","

                + User.NAME + ","
                + User.E_MAIL + ","
                + User.PHONE + ""

                + " FROM " + Order.TABLE_NAME
                + " INNER JOIN " + User.TABLE_NAME + " ON ( " + User.TABLE_NAME + "." + User.USER_ID + " = " + Order.TABLE_NAME + "." + Order.END_USER_ID + " ) "
                + " WHERE " + Order.ORDER_ID + " = " + orderID;


        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        Order order = null;

        try {

            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while(rs.next())
            {
                order = new Order();

                order.setOrderID(rs.getInt(Order.ORDER_ID));
                order.setDeliveryAddressID(rs.getInt(Order.DELIVERY_ADDRESS_ID));
                order.setDateTimePlaced(rs.getTimestamp(Order.DATE_TIME_PLACED));


                order.setDeliveryCharges(rs.getInt(Order.DELIVERY_CHARGES));


                order.setDeliveryGuySelfID(rs.getInt(Order.DELIVERY_GUY_SELF_ID));
                order.setEndUserID(rs.getInt(Order.END_USER_ID));
                order.setPickFromShop(rs.getBoolean(Order.PICK_FROM_SHOP));


                order.setShopID(rs.getInt(Order.SHOP_ID));
                order.setStatusHomeDelivery(rs.getInt(Order.STATUS_HOME_DELIVERY));
                order.setStatusPickFromShop(rs.getInt(Order.STATUS_PICK_FROM_SHOP));

//                order.setDeliveryReceived(rs.getBoolean(Order.DELIVERY_RECEIVED));
//                order.setPaymentReceived(rs.getBoolean(Order.PAYMENT_RECEIVED));




                User endUser = new User();
                endUser.setName(rs.getString(User.NAME));
                endUser.setEmail(rs.getString(User.E_MAIL));
                endUser.setPhone(rs.getString(User.PHONE));

                order.setRt_end_user_profile(endUser);

            }


            //System.out.println("Total itemCategories queried " + itemCategoryList.size());



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally

        {

            try {
                if(rs!=null)
                {rs.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(statement!=null)
                {statement.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return order;
    }




    public OrderEndPoint getOrdersList(
                   Integer endUserID, Integer shopID,
                   Boolean pickFromShop,
                   Integer homeDeliveryStatus, Integer pickFromShopStatus,
                   Integer deliveryGuyID,
                   Boolean pendingOrders,
                   String searchString,
                   String sortBy,
                   int limit, int offset,
                   boolean getRowCount,
                   boolean getOnlyMetadata
    )
    {

        String queryCount = "";

        String query = "SELECT "

                + Order.TABLE_NAME + "." + Order.ORDER_ID + ","
                + Order.TABLE_NAME + "." + Order.SHOP_ID + ","
                + Order.TABLE_NAME + "." + Order.DATE_TIME_PLACED + ","
                + Order.TABLE_NAME + "." + Order.ITEM_COUNT + ","
                + Order.TABLE_NAME + "." + Order.NET_PAYABLE + ","
                + Order.TABLE_NAME + "." + Order.PICK_FROM_SHOP + ","
                + Order.TABLE_NAME + "." + Order.STATUS_HOME_DELIVERY + ","
                + Order.TABLE_NAME + "." + Order.STATUS_PICK_FROM_SHOP + ","

//                + Order.TABLE_NAME + "." + Order.ITEM_TOTAL + ","
//                + Order.TABLE_NAME + "." + Order.APP_SERVICE_CHARGE + ","
//                + Order.TABLE_NAME + "." + Order.DELIVERY_CHARGES + ","


//                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.NAME + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.PHONE_NUMBER + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.DELIVERY_ADDRESS + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.CITY + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.PINCODE + ""

                + " FROM " + Order.TABLE_NAME
                + " LEFT OUTER JOIN " + DeliveryAddress.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_ADDRESS_ID + " = " + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID + ")"
                + " WHERE TRUE ";





        if(endUserID !=null)
        {
            query = query + " AND " + Order.TABLE_NAME + "." + Order.END_USER_ID + " = " + endUserID;

        }



        if(shopID != null)
        {
            query = query + " AND " + Order.SHOP_ID + " = " + shopID;
        }




        if(searchString != null)
        {

            String queryPartSearch = " ( " + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.NAME +" ilike '%" + searchString + "%'"
                    + " or CAST ( " + Order.TABLE_NAME + "." + Order.ORDER_ID + " AS text )" + " ilike '%" + searchString + "%'" + ") ";


            query = query + " AND " + queryPartSearch;
        }





        if(pendingOrders!=null)
        {
            String queryPartPending = "";


            if(pendingOrders)
            {
                queryPartPending = "(" + Order.STATUS_HOME_DELIVERY + " < " + OrderStatusHomeDelivery.PAYMENT_RECEIVED + ")";

            }
            else
            {
                queryPartPending = "(" + Order.STATUS_HOME_DELIVERY + " = " + OrderStatusHomeDelivery.PAYMENT_RECEIVED + ")";

            }



            query = query + " AND " + queryPartPending;
        }




        if(homeDeliveryStatus != null)
        {

            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " = " + homeDeliveryStatus;
        }




        if(pickFromShopStatus != null)
        {
            query = query + " AND " + Order.STATUS_PICK_FROM_SHOP + " = " + pickFromShopStatus;
        }



        if(pickFromShop != null)
        {

            query = query + " AND " + Order.PICK_FROM_SHOP + " = " + pickFromShop;
        }




        if(deliveryGuyID != null)
        {
            query = query + " AND " + Order.DELIVERY_GUY_SELF_ID + " = " + deliveryGuyID;
        }




        // all the non-aggregate columns which are present in select must be present in group by also.
        query = query
                + " group by "
                + Order.TABLE_NAME + "." + Order.ORDER_ID + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID ;


        queryCount = query;





        if(sortBy!=null && !sortBy.equals(""))
        {
            String queryPartSortBy = " ORDER BY " + sortBy;
            query = query + queryPartSortBy;
        }




        query = query + " LIMIT " + limit + " " + " OFFSET " + offset;



        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        OrderEndPoint endPoint = new OrderEndPoint();

        ArrayList<Order> ordersList = new ArrayList<>();


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;




        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata) {


                statement = connection.prepareStatement(query);


                rs = statement.executeQuery();

                while (rs.next()) {

                    Order order = new Order();
                    order.setOrderID(rs.getInt(Order.ORDER_ID));
                    order.setShopID(rs.getInt(Order.SHOP_ID));
                    order.setPickFromShop(rs.getBoolean(Order.PICK_FROM_SHOP));
                    order.setStatusHomeDelivery(rs.getInt(Order.STATUS_HOME_DELIVERY));
                    order.setStatusPickFromShop(rs.getInt(Order.STATUS_PICK_FROM_SHOP));
                    order.setDateTimePlaced(rs.getTimestamp(Order.DATE_TIME_PLACED));
                    order.setItemCount((Integer) rs.getObject(Order.ITEM_COUNT));
                    order.setNetPayable(rs.getDouble(Order.NET_PAYABLE));

//                    order.setItemTotal((Double) rs.getObject(Order.ITEM_TOTAL));
//                    order.setAppServiceCharge(rs.getDouble(Order.APP_SERVICE_CHARGE));
//                    order.setDeliveryCharges(rs.getDouble(Order.DELIVERY_CHARGES));


                    DeliveryAddress address = new DeliveryAddress();
                    address.setName(rs.getString(DeliveryAddress.NAME));
                    address.setPhoneNumber(rs.getLong(DeliveryAddress.PHONE_NUMBER));
                    address.setDeliveryAddress(rs.getString(DeliveryAddress.DELIVERY_ADDRESS));
                    address.setCity(rs.getString(DeliveryAddress.CITY));
                    address.setPincode(rs.getLong(DeliveryAddress.PINCODE));
                    order.setDeliveryAddress(address);


                    ordersList.add(order);
                }


                endPoint.setResults(ordersList);
            }






            if(getRowCount)
            {
                statement = connection.prepareStatement(queryCount);
                rs = statement.executeQuery();

                while(rs.next())
                {
                    endPoint.setItemCount(rs.getInt("item_count"));
                }
            }



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        finally

        {

            try {
                if(rs!=null)
                {rs.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(statement!=null)
                {statement.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        return endPoint;
    }




    public OrderEndPoint getOrdersListWithDeliveryProfile(
            Integer endUserID, Integer shopID,
            Boolean pickFromShop,
            Integer homeDeliveryStatus, Integer pickFromShopStatus,
            Integer deliveryGuyID,
            Double latCenter, Double lonCenter,
            Boolean pendingOrders,
            String searchString,
            String sortBy,
            int limit, int offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    )
    {

        String queryCount = "";

        String query = "SELECT "

                + "6371 * acos( cos( radians("
                + latCenter + ")) * cos( radians( " + DeliveryAddress.LATITUDE + ") ) * cos(radians( " + DeliveryAddress.LONGITUDE + " ) - radians("
                + lonCenter + "))"
                + " + sin( radians(" + latCenter + ")) * sin(radians( " + DeliveryAddress.LATITUDE + " ))) as distance" + ","

                + Order.TABLE_NAME + "." + Order.ORDER_ID + ","
                + Order.TABLE_NAME + "." + Order.SHOP_ID + ","
                + Order.TABLE_NAME + "." + Order.DATE_TIME_PLACED + ","
                + Order.TABLE_NAME + "." + Order.ITEM_COUNT + ","
                + Order.TABLE_NAME + "." + Order.NET_PAYABLE + ","
                + Order.TABLE_NAME + "." + Order.PICK_FROM_SHOP + ","
                + Order.TABLE_NAME + "." + Order.STATUS_HOME_DELIVERY + ","
                + Order.TABLE_NAME + "." + Order.STATUS_PICK_FROM_SHOP + ","

                + Order.TABLE_NAME + "." + Order.ITEM_TOTAL + ","
                + Order.TABLE_NAME + "." + Order.APP_SERVICE_CHARGE + ","
                + Order.TABLE_NAME + "." + Order.DELIVERY_CHARGES + ","



//                + Order.TABLE_NAME + "." + Order.END_USER_ID + ","

//                + Order.TABLE_NAME + "." + Order.DELIVERY_ADDRESS_ID + ","
//                + Order.TABLE_NAME + "." + Order.DELIVERY_GUY_SELF_ID + ","
//                + Order.TABLE_NAME + "." + Order.DELIVERY_OTP + ","
//                + Order.TABLE_NAME + "." + Order.IS_CANCELLED_BY_END_USER + ","
//                + Order.TABLE_NAME + "." + Order.REASON_FOR_CANCELLED_BY_SHOP + ","
//                + Order.TABLE_NAME + "." + Order.REASON_FOR_CANCELLED_BY_USER + ","
//                + Order.TABLE_NAME + "." + Order.REASON_FOR_ORDER_RETURNED + ","
//


//                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.NAME + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.PHONE_NUMBER + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.DELIVERY_ADDRESS + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.CITY + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.PINCODE + ","



//                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.END_USER_ID + ","
//                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.LANDMARK + ","
//                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.LATITUDE + ","
//                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.LONGITUDE + ","

//

                + User.TABLE_NAME + "." + User.USER_ID + ","
                + User.TABLE_NAME + "." + User.NAME + ","
                + User.TABLE_NAME + "." + User.PHONE + ","
                + User.TABLE_NAME + "." + User.PROFILE_IMAGE_URL + ""

                + " FROM " + Order.TABLE_NAME
                + " LEFT OUTER JOIN " + DeliveryAddress.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_ADDRESS_ID + " = " + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID + ")"
                + " LEFT OUTER JOIN " + User.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_GUY_SELF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " WHERE TRUE ";





        if(endUserID !=null)
        {
            query = query + " AND " + Order.TABLE_NAME + "." + Order.END_USER_ID + " = " + endUserID;

        }



        if(shopID != null)
        {
            query = query + " AND " + Order.SHOP_ID + " = " + shopID;
        }




        if(searchString != null)
        {

            String queryPartSearch = " ( " + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.NAME +" ilike '%" + searchString + "%'"
                    + " or CAST ( " + Order.TABLE_NAME + "." + Order.ORDER_ID + " AS text )" + " ilike '%" + searchString + "%'" + ") ";


            query = query + " AND " + queryPartSearch;
        }





        if(pendingOrders!=null)
        {
            String queryPartPending = "";


            if(pendingOrders)
            {
                queryPartPending = "(" + Order.STATUS_HOME_DELIVERY + " < " + OrderStatusHomeDelivery.PAYMENT_RECEIVED + ")";

            }
            else
            {
                queryPartPending = "(" + Order.STATUS_HOME_DELIVERY + " = " + OrderStatusHomeDelivery.PAYMENT_RECEIVED + ")";

            }



            query = query + " AND " + queryPartPending;
        }




        if(homeDeliveryStatus != null)
        {

            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " = " + homeDeliveryStatus;
        }




        if(pickFromShopStatus != null)
        {
            query = query + " AND " + Order.STATUS_PICK_FROM_SHOP + " = " + pickFromShopStatus;
        }



        if(pickFromShop != null)
        {

            query = query + " AND " + Order.PICK_FROM_SHOP + " = " + pickFromShop;
        }




        if(deliveryGuyID != null)
        {
            query = query + " AND " + Order.DELIVERY_GUY_SELF_ID + " = " + deliveryGuyID;
        }




        // all the non-aggregate columns which are present in select must be present in group by also.
        query = query
                + " group by "
                + User.TABLE_NAME + "." + User.USER_ID + ","
                + Order.TABLE_NAME + "." + Order.ORDER_ID + ","
                + DeliveryAddress.TABLE_NAME + "." + DeliveryAddress.ID ;


        queryCount = query;





        if(sortBy!=null && !sortBy.equals(""))
        {
            String queryPartSortBy = " ORDER BY " + sortBy;
            query = query + queryPartSortBy;
        }




        query = query + " LIMIT " + limit + " " + " OFFSET " + offset;



        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        OrderEndPoint endPoint = new OrderEndPoint();

        ArrayList<Order> ordersList = new ArrayList<>();


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;




        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata) {


                statement = connection.prepareStatement(query);


                rs = statement.executeQuery();

                while (rs.next()) {

                    Order order = new Order();

                    order.setOrderID(rs.getInt(Order.ORDER_ID));
                    order.setShopID(rs.getInt(Order.SHOP_ID));
                    order.setPickFromShop(rs.getBoolean(Order.PICK_FROM_SHOP));
                    order.setStatusHomeDelivery(rs.getInt(Order.STATUS_HOME_DELIVERY));
                    order.setStatusPickFromShop(rs.getInt(Order.STATUS_PICK_FROM_SHOP));
                    order.setDateTimePlaced(rs.getTimestamp(Order.DATE_TIME_PLACED));
                    order.setItemCount((Integer) rs.getObject(Order.ITEM_COUNT));
                    order.setNetPayable(rs.getDouble(Order.NET_PAYABLE));

                    order.setItemTotal((Double) rs.getObject(Order.ITEM_TOTAL));
                    order.setAppServiceCharge(rs.getDouble(Order.APP_SERVICE_CHARGE));
                    order.setDeliveryCharges(rs.getDouble(Order.DELIVERY_CHARGES));





//                    order.setEndUserID(rs.getInt(Order.END_USER_ID));
//                    order.setDeliveryAddressID(rs.getInt(Order.DELIVERY_ADDRESS_ID));
//                    order.setDeliveryGuySelfID(rs.getInt(Order.DELIVERY_GUY_SELF_ID));
//                    order.setDeliveryOTP((Integer) rs.getObject(Order.DELIVERY_OTP));

//
//
//                    order.setCancelledByEndUser(rs.getBoolean(Order.IS_CANCELLED_BY_END_USER));
//                    order.setReasonCancelledByShop(rs.getString(Order.REASON_FOR_CANCELLED_BY_SHOP));
//                    order.setReasonCancelledByUser(rs.getString(Order.REASON_FOR_CANCELLED_BY_USER));
//                    order.setReasonForOrderReturned(rs.getString(Order.REASON_FOR_ORDER_RETURNED));






                    DeliveryAddress address = new DeliveryAddress();

                    address.setName(rs.getString(DeliveryAddress.NAME));
                    address.setPhoneNumber(rs.getLong(DeliveryAddress.PHONE_NUMBER));
                    address.setDeliveryAddress(rs.getString(DeliveryAddress.DELIVERY_ADDRESS));
                    address.setCity(rs.getString(DeliveryAddress.CITY));
                    address.setPincode(rs.getLong(DeliveryAddress.PINCODE));
                    address.setRt_distance(rs.getDouble("distance"));



//                    address.setEndUserID(rs.getInt(DeliveryAddress.END_USER_ID));

//                    address.setId(rs.getInt(DeliveryAddress.ID));
//                    address.setLandmark(rs.getString(DeliveryAddress.LANDMARK));

//                    address.setLatitude(rs.getDouble(DeliveryAddress.LATITUDE));
//                    address.setLongitude(rs.getDouble(DeliveryAddress.LONGITUDE));



                    order.setDeliveryAddress(address);



                    User deliveryGuy = new User();
                    deliveryGuy.setUserID(rs.getInt(User.USER_ID));
                    deliveryGuy.setName(rs.getString(User.NAME));
                    deliveryGuy.setPhone(rs.getString(User.PHONE));
                    deliveryGuy.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));

                    order.setRt_delivery_guy_profile(deliveryGuy);


                    ordersList.add(order);
                }


                endPoint.setResults(ordersList);
            }






            if(getRowCount)
            {
                statement = connection.prepareStatement(queryCount);
                rs = statement.executeQuery();

                while(rs.next())
                {
                    endPoint.setItemCount(rs.getInt("item_count"));
                }
            }



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        finally

        {

            try {
                if(rs!=null)
                {rs.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(statement!=null)
                {statement.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        return endPoint;
    }





    public int updateOrder(Order order)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET "
                + Order.END_USER_ID + " = ?,"
                + " " + Order.SHOP_ID + " = ?,"
                + " " + Order.STATUS_HOME_DELIVERY + " = ?,"
                + " " + Order.STATUS_PICK_FROM_SHOP + " = ?,"

//                + " " + Order.PAYMENT_RECEIVED + " = ?,"
//                + " " + Order.DELIVERY_RECEIVED + " = ?,"

                + " " + Order.DELIVERY_CHARGES + " = ?,"
                + " " + Order.DELIVERY_ADDRESS_ID + " = ?,"
                      + Order.DELIVERY_GUY_SELF_ID + " = ?,"
                      + Order.PICK_FROM_SHOP + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?";



        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        int i = 0;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);


            statement.setObject(++i,order.getEndUserID());
            statement.setObject(++i,order.getShopID());
            statement.setObject(++i,order.getStatusHomeDelivery());
            statement.setObject(++i,order.getStatusPickFromShop());

//            statement.setObject(5,order.getPaymentReceived());
//            statement.setObject(6,order.getDeliveryReceived());

            statement.setObject(++i,order.getDeliveryCharges());
            statement.setObject(++i,order.getDeliveryAddressID());
            statement.setObject(++i,order.getDeliveryGuySelfID());
            statement.setObject(++i,order.isPickFromShop());
            statement.setObject(++i,order.getOrderID());


            updatedRows = statement.executeUpdate();
            System.out.println("Total rows updated: " + updatedRows);

            //conn.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally

        {

            try {

                if(statement!=null)
                {statement.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return updatedRows;
    }






    public int updateStatusHomeDelivery(Order order)
    {
        String updateStatement = "UPDATE " + Order.TABLE_NAME

                + " SET "
//                + OrderPFS.END_USER_ID + " = ?,"
//                + " " + OrderPFS.SHOP_ID + " = ?,"
                + " " + Order.STATUS_HOME_DELIVERY + " = ?"
//                + " " + OrderPFS.STATUS_PICK_FROM_SHOP + " = ?"
//                + " " + OrderPFS.PAYMENT_RECEIVED + " = ?,"
//                + " " + OrderPFS.DELIVERY_RECEIVED + " = ?"
//                + " " + OrderPFS.DELIVERY_CHARGES + " = ?,"
//                + " " + OrderPFS.DELIVERY_ADDRESS_ID + " = ?,"
//                + OrderPFS.DELIVERY_GUY_SELF_ID + " = ?"
//                + OrderPFS.PICK_FROM_SHOP + " = ?"
                + " WHERE " + Order.ORDER_ID + " = ?";



        Connection connection = null;
        PreparedStatement statement = null;
        int updatedRows = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

//            statement.setObject(1,order.getEndUserID());
//            statement.setObject(1,order.getShopID());
            statement.setObject(1,order.getStatusHomeDelivery());
//            statement.setObject(4,order.getStatusPickFromShop());
//            statement.setObject(2,order.getPaymentReceived());
//            statement.setObject(3,order.getDeliveryReceived());
//            statement.setObject(7,order.getDeliveryCharges());
//            statement.setObject(8,order.getDeliveryAddressID());
//            statement.setObject(2,order.getDeliveryGuySelfID());
//            statement.setObject(10,order.getPickFromShop());
            statement.setObject(2,order.getOrderID());


            updatedRows = statement.executeUpdate();
            System.out.println("Total rows updated: " + updatedRows);

            //conn.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally

        {

            try {

                if(statement!=null)
                {statement.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return updatedRows;
    }




    public int orderCancelledByEndUser(Integer orderID)
    {
        Order order = Globals.daoOrderUtility.readStatusHomeDelivery(orderID);

        if(order!=null) {

            int status = order.getStatusHomeDelivery();

            if (status == OrderStatusHomeDelivery.ORDER_PLACED ||
                    status == OrderStatusHomeDelivery.ORDER_CONFIRMED ||
                    status == OrderStatusHomeDelivery.ORDER_PACKED)
            {
                order.setStatusHomeDelivery(OrderStatusHomeDelivery.CANCELLED_BY_USER);
            }
            else
            {
                return 0;
            }


            return updateStatusHomeDelivery(order);
        }

        return 0;
    }

}
