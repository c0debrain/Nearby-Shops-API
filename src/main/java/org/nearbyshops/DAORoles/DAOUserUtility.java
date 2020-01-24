package org.nearbyshops.DAORoles;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.ModelRoles.ShopStaffPermissions;

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



}
