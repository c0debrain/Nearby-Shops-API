package org.nearbyshops.DAOs.DAOOrders;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.ModelReviewShop.ShopReview;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.Model.ModelRoles.Endpoints.UserEndpoint;
import org.nearbyshops.Model.ModelRoles.User;

import java.sql.*;
import java.util.ArrayList;

public class DAOOrderUtility {


    private HikariDataSource dataSource = Globals.getDataSource();



    public int checkOrderStatus(int orderID)
    {

        return 0;
    }




    public Shop getShopDetailsForCreateOrder(int ShopID)
    {

        String query = " ";

        query = "SELECT "

                + Shop.TABLE_NAME + "." + Shop.DELIVERY_CHARGES + ","
                + Shop.TABLE_NAME + "." + Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + ""

                + " FROM " + Shop.TABLE_NAME
                + " WHERE "	+ Shop.TABLE_NAME + "." + Shop.SHOP_ID + "= " + ShopID;



        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        Shop shop = null;
        try {

            connection = dataSource.getConnection();

            statement = connection.createStatement();

            rs = statement.executeQuery(query);


            while(rs.next())
            {
                shop = new Shop();
                shop.setDeliveryCharges(rs.getFloat(Shop.DELIVERY_CHARGES));
                shop.setBillAmountForFreeDelivery(rs.getInt(Shop.BILL_AMOUNT_FOR_FREE_DELIVERY));
            }


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

        return shop;
    }




    public Order getExtraDetailsForOrderDetailsScreen(int orderID)
    {

        String query = "SELECT "

                    + Order.TABLE_NAME + "." + Order.ITEM_TOTAL + ","
                    + Order.TABLE_NAME + "." + Order.APP_SERVICE_CHARGE + ","
                    + Order.TABLE_NAME + "." + Order.DELIVERY_CHARGES + ""

                    + " FROM " + Order.TABLE_NAME
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
                order.setItemTotal((Double) rs.getObject(Order.ITEM_TOTAL));
                order.setAppServiceCharge(rs.getDouble(Order.APP_SERVICE_CHARGE));
                order.setDeliveryCharges(rs.getDouble(Order.DELIVERY_CHARGES));
            }


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




    public Order readStatusHomeDelivery(int orderID)
    {

        String query = "SELECT "

//                + OrderPFS.ORDER_ID + ","
//                + OrderPFS.DELIVERY_ADDRESS_ID + ","
//                + OrderPFS.DATE_TIME_PLACED + ","

//                + OrderPFS.DELIVERY_CHARGES + ","

//                + Order.DELIVERY_RECEIVED + ","
//                + OrderPFS.PAYMENT_RECEIVED + ","

                + Order.DELIVERY_GUY_SELF_ID + ","
//                + OrderPFS.END_USER_ID + ","
//                + OrderPFS.PICK_FROM_SHOP + ","

                + Order.SHOP_ID + ","
                + Order.END_USER_ID + ","
                + Order.STATUS_HOME_DELIVERY + ""
//                + OrderPFS.STATUS_PICK_FROM_SHOP + ""

                + " FROM " + Order.TABLE_NAME
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


//                order.setDeliveryCharges(rs.getInt(OrderPFS.DELIVERY_CHARGES));
//                order.setEndUserID(rs.getInt(OrderPFS.END_USER_ID));

                order.setOrderID(orderID);

//                order.setDeliveryReceived(rs.getBoolean(Order.DELIVERY_RECEIVED));

                order.setShopID(rs.getInt(Order.SHOP_ID));
                order.setStatusHomeDelivery(rs.getInt(Order.STATUS_HOME_DELIVERY));
                order.setEndUserID(rs.getInt(Order.END_USER_ID));
                order.setDeliveryGuySelfID(rs.getInt(Order.DELIVERY_GUY_SELF_ID));



//                order.setPickFromShop(rs.getBoolean(OrderPFS.PICK_FROM_SHOP));
//                order.setDateTimePlaced(rs.getTimestamp(OrderPFS.DATE_TIME_PLACED));


//                order.setStatusPickFromShop(rs.getInt(OrderPFS.STATUS_PICK_FROM_SHOP));


//                order.setPaymentReceived(rs.getBoolean(OrderPFS.PAYMENT_RECEIVED));
//
//               order.setDeliveryAddressID((Integer) rs.getObject(OrderPFS.DELIVERY_ADDRESS_ID));

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



    // fetch delivery guys assigned to the orders in the given shop with given status
    public UserEndpoint fetchDeliveryGuys(
            Integer shopID,
            Integer homeDeliveryStatus,
            String sortBy,
            Integer limit, Integer offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    )
    {

        String queryCount = "";

        String query = "SELECT "

                + User.TABLE_NAME + "." + User.USER_ID + ","
                + User.TABLE_NAME + "." + User.NAME + ","
                + User.TABLE_NAME + "." + User.PHONE + ","
                + User.TABLE_NAME + "." + User.PROFILE_IMAGE_URL + ""

                + " FROM " + Order.TABLE_NAME
                + " INNER JOIN " + User.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_GUY_SELF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " WHERE TRUE ";



//        boolean isFirst = true;



        if(shopID != null)
        {
            query = query + " AND " + Order.SHOP_ID + " = " + shopID;
        }







        if(homeDeliveryStatus != null)
        {

            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " = " + homeDeliveryStatus;
        }






        // all the non-aggregate columns which are present in select must be present in group by also.
        query = query
                + " group by "
                + User.TABLE_NAME + "." + User.USER_ID ;


        queryCount = query;



        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;

                query = query + queryPartSortBy;
            }
        }



        if(limit != null)
        {

            String queryPartLimitOffset = "";

            if(offset!=null)
            {
                queryPartLimitOffset = " LIMIT " + limit + " " + " OFFSET " + offset;

            }else
            {
                queryPartLimitOffset = " LIMIT " + limit + " " + " OFFSET " + 0;
            }

            query = query + queryPartLimitOffset;
        }




        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        UserEndpoint endPoint = new UserEndpoint();

        ArrayList<User> usersList = new ArrayList<>();
        Connection connection = null;


        PreparedStatement statement = null;
        ResultSet rs = null;


        PreparedStatement statementCount = null;
        ResultSet resultSetCount = null;


        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata) {


//                statement = connection.prepareStatement(queryJoin);

                statement = connection.prepareStatement(query);


                rs = statement.executeQuery();

                while (rs.next()) {


                    User deliveryGuy = new User();
                    deliveryGuy.setUserID(rs.getInt(User.USER_ID));
                    deliveryGuy.setName(rs.getString(User.NAME));
                    deliveryGuy.setPhone(rs.getString(User.PHONE));
                    deliveryGuy.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));


                    usersList.add(deliveryGuy);
                }


                endPoint.setResults(usersList);
            }



            if(getRowCount)
            {
                statementCount = connection.prepareStatement(queryCount);

                i = 0;



                resultSetCount = statementCount.executeQuery();

                while(resultSetCount.next())
                {
                    System.out.println("Item Count : " + String.valueOf(endPoint.getItemCount()));
                    endPoint.setItemCount(resultSetCount.getInt("item_count"));
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





    // fetch delivery guys assigned to the orders in the given shop with given status
    public UserEndpoint filterShops(
            Integer deliveryGuyID,
            Integer homeDeliveryStatus,
            String sortBy,
            Integer limit, Integer offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    )
    {

        String queryCount = "";

        String query = "SELECT "

                + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
                + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
                + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ""

                + " FROM " + Order.TABLE_NAME
                + " INNER JOIN " + User.TABLE_NAME + " ON (" + Order.TABLE_NAME + "." + Order.DELIVERY_GUY_SELF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " INNER JOIN " + Shop.TABLE_NAME + " ON (" + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = " + Order.TABLE_NAME + "." + Order.SHOP_ID + ")"
                + " WHERE TRUE ";




        if(deliveryGuyID != null)
        {
            query = query + " AND " + User.USER_ID + " = " + deliveryGuyID;
        }




        if(homeDeliveryStatus != null)
        {

            query = query + " AND " + Order.STATUS_HOME_DELIVERY + " = " + homeDeliveryStatus;
        }





        // all the non-aggregate columns which are present in select must be present in group by also.
        query = query
                + " group by "
                + Shop.TABLE_NAME + "." + Shop.SHOP_ID ;


        queryCount = query;





        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;

                query = query + queryPartSortBy;
            }
        }



        if(limit != null)
        {

            String queryPartLimitOffset = "";

            if(offset!=null)
            {
                queryPartLimitOffset = " LIMIT " + limit + " " + " OFFSET " + offset;

            }else
            {
                queryPartLimitOffset = " LIMIT " + limit + " " + " OFFSET " + 0;
            }

            query = query + queryPartLimitOffset;
        }




        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";




        UserEndpoint endPoint = new UserEndpoint();

        ArrayList<User> usersList = new ArrayList<>();
        Connection connection = null;


        PreparedStatement statement = null;
        ResultSet rs = null;


        PreparedStatement statementCount = null;
        ResultSet resultSetCount = null;


        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata) {


//                statement = connection.prepareStatement(queryJoin);

                statement = connection.prepareStatement(query);


                rs = statement.executeQuery();

                while (rs.next()) {


                    User deliveryGuy = new User();
                    deliveryGuy.setUserID(rs.getInt(User.USER_ID));
                    deliveryGuy.setName(rs.getString(User.NAME));
                    deliveryGuy.setPhone(rs.getString(User.PHONE));
                    deliveryGuy.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));


                    usersList.add(deliveryGuy);
                }


                endPoint.setResults(usersList);
            }



            if(getRowCount)
            {
                statementCount = connection.prepareStatement(queryCount);

                i = 0;



                resultSetCount = statementCount.executeQuery();

                while(resultSetCount.next())
                {
//                    System.out.println("Item Count : " + String.valueOf(endPoint.getItemCount()));
                    endPoint.setItemCount(resultSetCount.getInt("item_count"));
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



}
