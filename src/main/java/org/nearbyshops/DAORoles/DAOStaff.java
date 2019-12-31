package org.nearbyshops.DAORoles;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.ModelRoles.Endpoints.UserEndpoint;
import org.nearbyshops.ModelRoles.StaffPermissions;
import org.nearbyshops.ModelRoles.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by sumeet on 28/8/17.
 */
public class DAOStaff {

    private HikariDataSource dataSource = Globals.getDataSource();


    /*

        saveStaffByAdmin(User staff)
    *  getStaffPublic()
    *  getStaffForAdmin()
    *
    *
    *
    * */



    public int updateStaffLocation(StaffPermissions permissions)
    {


        String insertStaffPermissions =

                "INSERT INTO " + StaffPermissions.TABLE_NAME
                        + "("
                        + StaffPermissions.STAFF_ID + ","
                        + StaffPermissions.LAT_CURRENT + ","
                        + StaffPermissions.LON_CURRENT + ""
                        + ") values(?,?,?)"
                        + " ON CONFLICT (" + StaffPermissions.STAFF_ID + ")"
                        + " DO UPDATE "
                        + " SET "
                        + StaffPermissions.LAT_CURRENT + "= excluded." + StaffPermissions.LAT_CURRENT + " , "
                        + StaffPermissions.LON_CURRENT + "= excluded." + StaffPermissions.LON_CURRENT;




        Connection connection = null;
        PreparedStatement statement = null;

        int rowCountUpdated = 0;

        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);


            statement = connection.prepareStatement(insertStaffPermissions,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 0;


            if(permissions!=null)
            {
                statement.setObject(++i,permissions.getStaffUserID());
                statement.setObject(++i,permissions.getLatCurrent());
                statement.setObject(++i,permissions.getLonCurrent());

                rowCountUpdated = statement.executeUpdate();
            }


            connection.commit();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (connection != null) {
                try {

//                    idOfInsertedRow=-1;
//                    rowCountItems = 0;

                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
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

        return rowCountUpdated;
    }




    public StaffPermissions getStaffPermissions(int staffID)
    {

        boolean isFirst = true;

        String query = "SELECT "

                + StaffPermissions.STAFF_ID + ","
                + StaffPermissions.PERMIT_CREATE_UPDATE_ITEM_CATEGORIES + ","
                + StaffPermissions.PERMIT_CREATE_UPDATE_ITEMS + ","
                + StaffPermissions.PERMIT_APPROVE_SHOPS + ""

                + " FROM "  + StaffPermissions.TABLE_NAME
                + " WHERE " + StaffPermissions.STAFF_ID  + " = ? ";



        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        //Distributor distributor = null;
        StaffPermissions permissions = null;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);

            int i = 0;


            statement.setObject(++i,staffID); // username


            rs = statement.executeQuery();

            while(rs.next())
            {
                permissions = new StaffPermissions();

                permissions.setStaffUserID(rs.getInt(StaffPermissions.STAFF_ID));
                permissions.setPermitCreateUpdateItemCat(rs.getBoolean(StaffPermissions.PERMIT_CREATE_UPDATE_ITEM_CATEGORIES));
                permissions.setPermitCreateUpdateItems(rs.getBoolean(StaffPermissions.PERMIT_CREATE_UPDATE_ITEMS));
                permissions.setPermitApproveShops(rs.getBoolean(StaffPermissions.PERMIT_APPROVE_SHOPS));
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

        return permissions;
    }



}
