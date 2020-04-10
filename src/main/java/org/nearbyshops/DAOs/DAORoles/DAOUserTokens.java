package org.nearbyshops.DAOs.DAORoles;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.ModelRoles.UserTokens;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;





public class DAOUserTokens {


    private HikariDataSource dataSource = Globals.getDataSource();


    public User verifyUser(String username, String tokenString)
    {

        String queryToken = "SELECT "

                + User.USER_ID + ","
                + User.USERNAME + ","
                + User.ROLE + ""

                + " FROM " + User.TABLE_NAME
                + " INNER JOIN " + UserTokens.TABLE_NAME + " ON ( " + User.TABLE_NAME + "." + User.USER_ID + " = " + UserTokens.TABLE_NAME + "." + UserTokens.LOCAL_USER_ID + ")"
                + " WHERE "
                + " ( " + User.USERNAME + " = ? "
                + " OR " + " CAST ( " +  User.USER_ID + " AS text ) " + " = ? "
                + " OR " + " ( " + User.E_MAIL + " = ?" + ")"
                + " OR " + " ( " + User.PHONE + " = ?" + ")" + ")"
                + " AND " + UserTokens.TOKEN_STRING + " = ? ";


        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;



        User user = null;

        try {

//            System.out.println(query);

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(queryToken);

            int i = 0;
            statement.setString(++i,username); // username
            statement.setString(++i,username); // userID
            statement.setString(++i,username); // email
            statement.setString(++i,username); // phone
            statement.setString(++i,tokenString); // token
//            statement.setTimestamp(++i,new Timestamp(System.currentTimeMillis()));


            rs = statement.executeQuery();

            while(rs.next())
            {
                user = new User();

                user.setUserID(rs.getInt(User.USER_ID));
                user.setUsername(rs.getString(User.USERNAME));
                user.setRole(rs.getInt(User.ROLE));
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

        return user;
    }



    public int insertToken(int userID, String tokenString)
    {

        int idOfInsertedRow = 0;
        int rowCount = 0;

        Connection connection = null;
        PreparedStatement statement = null;


        String insertToken = "";

        insertToken = "INSERT INTO "
                + UserTokens.TABLE_NAME
                + "("
                + UserTokens.LOCAL_USER_ID + ","
                + UserTokens.TOKEN_STRING + ""
                + ") VALUES(?,?)";


        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(insertToken,PreparedStatement.RETURN_GENERATED_KEYS);

            int i = 0;
            statement.setInt(++i,userID);
            statement.setString(++i,tokenString);

            rowCount = statement.executeUpdate();


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


        return rowCount;
    }


}
