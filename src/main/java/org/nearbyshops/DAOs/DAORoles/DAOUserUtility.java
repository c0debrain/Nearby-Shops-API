package org.nearbyshops.DAOs.DAORoles;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.Model.ModelRoles.ShopStaffPermissions;
import org.nearbyshops.Model.ModelRoles.User;

import java.sql.*;

public class DAOUserUtility {


    private HikariDataSource dataSource = Globals.getDataSource();



    public int getShopIDForShopAdmin(int shopAdminID)
    {
        String query =  " SELECT " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + "" +
                " FROM " + Shop.TABLE_NAME +
                " WHERE " + Shop.SHOP_ADMIN_ID + " = " + shopAdminID ;


        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
//        Shop shop = null;
        int shopID = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.createStatement();

            rs = statement.executeQuery(query);

            while(rs.next())
            {

                shopID = rs.getInt(Shop.SHOP_ID);
            }


//			System.out.println("Total Shops queried " + shopList.size());



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

        return shopID;
    }


    public Shop getShopForShopAdmin(int shopAdminID)
    {
        String query = " SELECT "
                + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
                + Shop.SHOP_NAME + ","

                + Shop.DELIVERY_RANGE + ","
                + Shop.LAT_CENTER + ","
                + Shop.LON_CENTER + ","

                + Shop.DELIVERY_CHARGES + ","
                + Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + ","
                + Shop.PICK_FROM_SHOP_AVAILABLE + ","
                + Shop.HOME_DELIVERY_AVAILABLE + ","

                + Shop.SHOP_ENABLED + ","

                + Shop.LOGO_IMAGE_PATH + ","

                + Shop.SHOP_ADDRESS + ","
                + Shop.CITY + ","
                + Shop.PINCODE + ","
                + Shop.LANDMARK + ","

                + Shop.CUSTOMER_HELPLINE_NUMBER + ","
                + Shop.DELIVERY_HELPLINE_NUMBER + ","

                + Shop.SHORT_DESCRIPTION + ","
                + Shop.LONG_DESCRIPTION + ","

                + Shop.TIMESTAMP_CREATED + ","
                + Shop.IS_OPEN + ","

                + Shop.ACCOUNT_BALANCE + ","
                + Shop.EXTENDED_CREDIT_LIMIT + "" +

                " FROM " + Shop.TABLE_NAME +
                " WHERE " + Shop.SHOP_ADMIN_ID + " = " + shopAdminID ;


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

                shop.setShopID(rs.getInt(Shop.SHOP_ID));
                shop.setShopAdminID(shopAdminID);

                shop.setShopName(rs.getString(Shop.SHOP_NAME));
                shop.setDeliveryRange(rs.getDouble(Shop.DELIVERY_RANGE));
                shop.setLatCenter(rs.getFloat(Shop.LAT_CENTER));
                shop.setLonCenter(rs.getFloat(Shop.LON_CENTER));

                shop.setDeliveryCharges(rs.getFloat(Shop.DELIVERY_CHARGES));
                shop.setBillAmountForFreeDelivery(rs.getInt(Shop.BILL_AMOUNT_FOR_FREE_DELIVERY));
                shop.setPickFromShopAvailable(rs.getBoolean(Shop.PICK_FROM_SHOP_AVAILABLE));
                shop.setHomeDeliveryAvailable(rs.getBoolean(Shop.HOME_DELIVERY_AVAILABLE));

                shop.setShopEnabled(rs.getBoolean(Shop.SHOP_ENABLED));
//				shop.setShopWaitlisted(rs.getBoolean(Shop.SHOP_WAITLISTED));

                shop.setLogoImagePath(rs.getString(Shop.LOGO_IMAGE_PATH));

                shop.setShopAddress(rs.getString(Shop.SHOP_ADDRESS));
                shop.setCity(rs.getString(Shop.CITY));
                shop.setPincode(rs.getLong(Shop.PINCODE));
                shop.setLandmark(rs.getString(Shop.LANDMARK));

                shop.setCustomerHelplineNumber(rs.getString(Shop.CUSTOMER_HELPLINE_NUMBER));
                shop.setDeliveryHelplineNumber(rs.getString(Shop.DELIVERY_HELPLINE_NUMBER));

                shop.setShortDescription(rs.getString(Shop.SHORT_DESCRIPTION));
                shop.setLongDescription(rs.getString(Shop.LONG_DESCRIPTION));

                shop.setTimestampCreated(rs.getTimestamp(Shop.TIMESTAMP_CREATED));
                shop.setOpen(rs.getBoolean(Shop.IS_OPEN));

                shop.setAccountBalance(rs.getDouble(Shop.ACCOUNT_BALANCE));


                shop.setRt_min_balance(GlobalConstants.min_account_balance_for_shop - rs.getDouble(Shop.EXTENDED_CREDIT_LIMIT));

            }


//			System.out.println("Total Shops queried " + shopList.size());



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

        return shop;
    }


    public Shop getShopForShopStaff(int staffUserID)
    {

        String query = " SELECT "
                + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
                + Shop.SHOP_NAME + ""
                + " FROM " + Shop.TABLE_NAME
                + " INNER JOIN " + ShopStaffPermissions.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " = " + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.SHOP_ID + " )"
                + " WHERE " + ShopStaffPermissions.STAFF_ID + " = " + staffUserID ;


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
                shop.setShopID(rs.getInt(Shop.SHOP_ID));
                shop.setShopName(rs.getString(Shop.SHOP_NAME));
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

        return shop;
    }



    public int getShopIDforShopStaff(int shopStaffID) {

        String query = "SELECT " + ShopStaffPermissions.SHOP_ID + ""
                + " FROM "   + ShopStaffPermissions.TABLE_NAME
                + " WHERE "  + ShopStaffPermissions.STAFF_ID + " = ?";



        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        //Distributor distributor = null;
        int shopID = -1;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);

            statement.setObject(1,shopStaffID);

            rs = statement.executeQuery();


            while(rs.next())
            {
                shopID = rs.getInt(ShopStaffPermissions.SHOP_ID);
            }




            //System.out.println("Total itemCategories queried " + itemCategoryList.size());


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally

        {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return shopID;
    }


    public int getUserIDforShopAdmin(int shopID) {

        String query = "SELECT " + Shop.SHOP_ADMIN_ID + ""
                + " FROM "   + Shop.TABLE_NAME
                + " WHERE "  + Shop.SHOP_ID + " = ?";



        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;



        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);

            statement.setObject(1,shopID);

            rs = statement.executeQuery();


            while(rs.next())
            {
                shopID = rs.getInt(Shop.SHOP_ADMIN_ID);
            }



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally

        {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return shopID;
    }



    public int getUserID(String username)
    {


        String queryPassword = "SELECT "

                + User.USER_ID + ","
                + User.USERNAME + ","
                + User.ROLE + ""

                + " FROM " + User.TABLE_NAME
                + " WHERE "
                + " ( " + User.USERNAME + " = ? "
                + " OR " + " CAST ( " +  User.USER_ID + " AS text ) " + " = ? "
                + " OR " + " ( " + User.E_MAIL + " = ?" + ")"
                + " OR " + " ( " + User.PHONE + " = ?" + ")" + ")";




        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        int userID = -1;

        try {

//            System.out.println(query);

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(queryPassword);

            int i = 0;
            statement.setString(++i,username); // username
            statement.setString(++i,username); // userID
            statement.setString(++i,username); // email
            statement.setString(++i,username); // phone


            rs = statement.executeQuery();

            while(rs.next())
            {
                userID = rs.getInt(User.USER_ID);
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



        return userID;
    }


    public boolean checkRoleExists(int role)
    {

        String query = "SELECT " + User.USERNAME
                + " FROM " + User.TABLE_NAME
                + " WHERE " + User.ROLE + " = ?";


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        System.out.println("Checked User Role  : " + role);

//		ShopAdmin shopAdmin = null;



        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);

            int i = 0;
            statement.setObject(++i,role);

            rs = statement.executeQuery();


            while(rs.next())
            {

                return true;
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

        return false;
    }




    public boolean checkUsernameExists(String username)
    {

        String query = "SELECT " + User.USERNAME
                + " FROM " + User.TABLE_NAME
                + " WHERE "
                + User.USERNAME + " = ?" + " OR "
                + User.E_MAIL + " = ? " + " OR "
                + User.PHONE + " = ?";


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        System.out.println("Checked Username : " + username);

//		ShopAdmin shopAdmin = null;



        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);

            int i = 0;
            statement.setObject(++i,username);
            statement.setObject(++i,username);
            statement.setObject(++i,username);


            rs = statement.executeQuery();


            while(rs.next())
            {

                return true;
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

        return false;
    }





    public void createAdminUsingEmail(User user, boolean getRowCount)
    {

        Connection connection = null;
        PreparedStatement statement = null;

        int idOfInsertedRow = -1;
        int rowCountItems = -1;


        String insertItemSubmission = "INSERT INTO "
                + User.TABLE_NAME
                + "("
                + User.ROLE + ","
                + User.E_MAIL + ","
                + User.PASSWORD + ""
                + ") values(?,?,?)";




        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);


            statement = connection.prepareStatement(insertItemSubmission,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 0;

            statement.setObject(++i, GlobalConstants.ROLE_ADMIN_CODE);
            statement.setString(++i,user.getEmail());
            statement.setString(++i,user.getPassword());



            rowCountItems = statement.executeUpdate();


            ResultSet rs = statement.getGeneratedKeys();

            if(rs.next())
            {
                idOfInsertedRow = rs.getInt(1);
            }


            connection.commit();



            System.out.println("Admin profile Created : Row count : " + rowCountItems);


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (connection != null) {
                try {

                    idOfInsertedRow=-1;
                    rowCountItems = 0;

                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        finally
        {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            try {

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }





    public void updateUserRole(int userID)
    {

        String updateStatement = "UPDATE " + User.TABLE_NAME

                + " SET " + User.ROLE + " = " + GlobalConstants.ROLE_ADMIN_CODE
                + " WHERE " + User.USER_ID + " = " + userID;

        // Please note there is supposed to be only one admin for the service. If that is not the case
        // then this method will not work for updating admin profile



        Connection connection = null;
        PreparedStatement statement = null;

        int rowCountUpdated = 0;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            rowCountUpdated = statement.executeUpdate();


            System.out.println("Admin Role updated : Row count : " + rowCountUpdated);

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

    }


    public void createAdmin(User user, boolean getRowCount)
    {

        Connection connection = null;
        PreparedStatement statement = null;

        int idOfInsertedRow = -1;
        int rowCountItems = -1;

        String insertItemSubmission = "INSERT INTO "
                + User.TABLE_NAME
                + "("
                + User.ROLE + ","
                + User.USERNAME + ","
                + User.PASSWORD + ""
                + ") values(?,?,?)";




        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);


            statement = connection.prepareStatement(insertItemSubmission,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 0;

            statement.setObject(++i,GlobalConstants.ROLE_ADMIN_CODE);
            statement.setString(++i,user.getUsername());
            statement.setString(++i,user.getPassword());



            rowCountItems = statement.executeUpdate();


            ResultSet rs = statement.getGeneratedKeys();

            if(rs.next())
            {
                idOfInsertedRow = rs.getInt(1);
            }


            connection.commit();





            System.out.println("Admin profile Created : Row count : " + rowCountItems);


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (connection != null) {
                try {

                    idOfInsertedRow=-1;
                    rowCountItems = 0;

                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        finally
        {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            try {

                if(connection!=null)
                {connection.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }


    public void updateAdminEmail(User user)
    {

        String updateStatement = "UPDATE " + User.TABLE_NAME

                + " SET "

                + User.E_MAIL + "=?,"
                + User.PASSWORD + "=?"

                + " WHERE " + User.ROLE + " = " + GlobalConstants.ROLE_ADMIN_CODE;

        // Please note there is supposed to be only one admin for the service. If that is not the case
        // then this method will not work for updating admin profile



        Connection connection = null;
        PreparedStatement statement = null;

        int rowCountUpdated = 0;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;


            statement.setString(++i,user.getUsername());
            statement.setObject(++i,user.getPassword());

            rowCountUpdated = statement.executeUpdate();


            System.out.println("Admin profile updated : Row count : " + rowCountUpdated);


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

    }


}
