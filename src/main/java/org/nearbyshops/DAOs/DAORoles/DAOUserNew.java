package org.nearbyshops.DAOs.DAORoles;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.ModelRoles.*;
import org.nearbyshops.Model.ModelRoles.Endpoints.UserEndpoint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by sumeet on 21/4/17.
 */
public class DAOUserNew {

    private HikariDataSource dataSource = Globals.getDataSource();


    public int updatePassword(User user, String oldPassword)
    {

//        + User.USERNAME + " = ?"




//        Gson gson = new Gson();
//        System.out.println(gson.toJson(user) + "\nOldPassword : " + oldPassword);


        String updateStatement = "UPDATE " + User.TABLE_NAME

                + " SET "
                + User.PASSWORD + "=?"

                + " WHERE "
                + " ( " + User.USERNAME + " = ? "
                + " OR " +  User.USER_ID + " = ? "
                + " OR " + " ( " + User.E_MAIL + " = ?" + ")"
                + " OR " + " ( " + User.PHONE + " = ?" + ")"
                + ")"
                + " AND " + User.PASSWORD + " = ?";


        Connection connection = null;
        PreparedStatement statement = null;

        int rowCountUpdated = 0;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

//            statement.setString(++i,user.getToken());
//            statement.setTimestamp(++i,user.getTimestampTokenExpires());

            statement.setString(++i,user.getPassword());


            statement.setString(++i,user.getUsername());
            statement.setObject(++i,user.getUserID());
            statement.setString(++i,user.getEmail());
            statement.setString(++i,user.getPhone());

            statement.setString(++i,oldPassword);


            rowCountUpdated = statement.executeUpdate();

            System.out.println("Total rows updated: " + rowCountUpdated);


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

        return rowCountUpdated;
    }



    public int updateEmail(User user)
    {

        Connection connection = null;
        PreparedStatement statement = null;

//        int idOfInsertedRow = -1;
        int rowCountItems = -1;

        String insertItemSubmission = " UPDATE " + User.TABLE_NAME
                                    + " SET "    + User.E_MAIL + " = " + EmailVerificationCode.TABLE_NAME + "." + EmailVerificationCode.EMAIL
                                    + " FROM "   + EmailVerificationCode.TABLE_NAME
                                    + " WHERE "  + EmailVerificationCode.TABLE_NAME + "." + EmailVerificationCode.EMAIL + " = ? "
                                    + " AND "    + EmailVerificationCode.TABLE_NAME + "." + EmailVerificationCode.VERIFICATION_CODE + " = ?"
                                    + " AND "    + EmailVerificationCode.TABLE_NAME + "." + EmailVerificationCode.TIMESTAMP_EXPIRES + " > now()"
                                    + " AND "    + User.TABLE_NAME + "." + User.USER_ID + " = ?"
                                    + " AND "    + User.TABLE_NAME + "." + User.PASSWORD + " = ?";


        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);


            statement = connection.prepareStatement(insertItemSubmission,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 0;


            // check email is verification code to ensure email belongs to user
            statement.setString(++i,user.getEmail());
            statement.setString(++i,user.getRt_email_verification_code());
            statement.setObject(++i,user.getUserID());
            statement.setString(++i,user.getPassword());


            rowCountItems = statement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (connection != null) {
                try {

//                    idOfInsertedRow=-1;
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



        return rowCountItems;
    }



    public int updatePhone(User user)
    {

        Connection connection = null;
        PreparedStatement statement = null;

//        int idOfInsertedRow = -1;
        int rowCountItems = -1;

        String insertItemSubmission = " UPDATE " + User.TABLE_NAME
                + " SET "    + User.PHONE + " = " + PhoneVerificationCode.TABLE_NAME + "." + PhoneVerificationCode.PHONE
                + " FROM "   + PhoneVerificationCode.TABLE_NAME
                + " WHERE "  + PhoneVerificationCode.TABLE_NAME + "." + PhoneVerificationCode.PHONE + " = ? "
                + " AND "    + PhoneVerificationCode.TABLE_NAME + "." + PhoneVerificationCode.VERIFICATION_CODE + " = ?"
                + " AND "    + PhoneVerificationCode.TABLE_NAME + "." + PhoneVerificationCode.TIMESTAMP_EXPIRES + " > now()"
                + " AND "    + User.TABLE_NAME + "." + User.USER_ID + " = ?"
                + " AND "    + User.TABLE_NAME + "." + User.PASSWORD + " = ?";


        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);


            statement = connection.prepareStatement(insertItemSubmission,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 0;





            // check email is verification code to ensure email belongs to user
            statement.setString(++i,user.getPhoneWithCountryCode());
            statement.setString(++i,user.getRt_phone_verification_code());
            statement.setObject(++i,user.getUserID());
            statement.setString(++i,user.getPassword());


            rowCountItems = statement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (connection != null) {
                try {

//                    idOfInsertedRow=-1;
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



        return rowCountItems;
    }





    public User checkGoogleID(String googleID) {


        String query = "SELECT "

                + User.USER_ID + ","
                + User.USERNAME + ","
                + User.PASSWORD + ","
                + User.E_MAIL + ","
                + User.PHONE + ","
                + User.NAME + ","
                + User.GENDER + ","
//                + User.PROFILE_IMAGE_ID + ","
                + User.IS_ACCOUNT_PRIVATE + ","
                + User.ABOUT + ""

                + " FROM " + User.TABLE_NAME;




        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        System.out.println("Checked Google ID : " + googleID);


        User user = null;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);

            statement.setObject(1,googleID);

            rs = statement.executeQuery();


            while(rs.next())
            {
                user = new User();

                user.setUserID(rs.getInt(User.USER_ID));
                user.setUsername(rs.getString(User.USERNAME));
                user.setPassword(rs.getString(User.PASSWORD));
                user.setEmail(rs.getString(User.E_MAIL));
                user.setPhone(rs.getString(User.PHONE));
                user.setName(rs.getString(User.NAME));
                user.setGender(rs.getBoolean(User.GENDER));
//                user.setProfileImageID(rs.getInt(User.PROFILE_IMAGE_ID));
                user.setAccountPrivate(rs.getBoolean(User.IS_ACCOUNT_PRIVATE));
                user.setAbout(rs.getString(User.ABOUT));
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

        return user;
    }




    public int saveGoogleProfile(User user, boolean getRowCount)
    {

        Connection connection = null;
        PreparedStatement statement = null;

        int idOfInsertedRow = -1;
        int rowCountItems = -1;

        String insertItemSubmission = "INSERT INTO "
                + User.TABLE_NAME
                + "("

                + User.USERNAME + ","
                + User.PASSWORD + ","
                + User.E_MAIL + ","

                + User.PHONE + ","
                + User.NAME + ","
                + User.GENDER + ","

//                + User.PROFILE_IMAGE_ID + ","
                + User.ROLE + ","
                + User.IS_ACCOUNT_PRIVATE + ","

                + User.TOKEN + ","
                + User.TIMESTAMP_TOKEN_EXPIRES + ","

                + User.ABOUT + ""


                + ") VALUES(?,?,? ,?,?,? ,?,?,?  ,?,?, ?)";


        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);


            statement = connection.prepareStatement(insertItemSubmission,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 0;

            statement.setString(++i,user.getUsername());
            statement.setString(++i,user.getPassword());
            statement.setString(++i,user.getEmail());

            statement.setString(++i,user.getPhone());
            statement.setString(++i,user.getName());
            statement.setObject(++i,user.getGender());

//            statement.setInt(++i,user.getProfileImageID());
            statement.setObject(++i,user.getRole());
            statement.setObject(++i,user.isAccountPrivate());

            statement.setString(++i,user.getToken());
            statement.setTimestamp(++i,user.getTimestampTokenExpires());

            statement.setString(++i,user.getAbout());

            rowCountItems = statement.executeUpdate();


            ResultSet rs = statement.getGeneratedKeys();

            if(rs.next())
            {
                idOfInsertedRow = rs.getInt(1);
            }



            connection.commit();

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

        if(getRowCount)
        {
            return rowCountItems;
        }
        else
        {
            return idOfInsertedRow;
        }
    }






    public int updateUser(User user)
    {

        String updateStatement = "UPDATE " + User.TABLE_NAME

                + " SET "

//                + User.USERNAME + "=?,"
//                + User.PASSWORD + "=?,"
//                + User.E_MAIL + "=?,"
//                + User.PHONE + "=?,"
                + User.NAME + "=?,"
                + User.SECRET_CODE + "=?,"
                + User.GENDER + "=?,"

                + User.PROFILE_IMAGE_URL + "=?,"
                + User.IS_ACCOUNT_PRIVATE + "=?,"
                + User.ABOUT + "=?"

                + " WHERE " + User.USER_ID + " = ?";


        Connection connection = null;
        PreparedStatement statement = null;

        int rowCountUpdated = 0;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

//            statement.setString(++i,user.getUsername());
//            statement.setString(++i,user.getPassword());
//            statement.setString(++i,user.getEmail());
//            statement.setString(++i,user.getPhone());

            statement.setString(++i,user.getName());
            statement.setInt(++i,user.getSecretCode());
            statement.setObject(++i,user.getGender());

            statement.setString(++i,user.getProfileImagePath());
            statement.setObject(++i,user.isAccountPrivate());
            statement.setString(++i,user.getAbout());

            statement.setObject(++i,user.getUserID());


            rowCountUpdated = statement.executeUpdate();


            System.out.println("Total rows updated: " + rowCountUpdated);


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

        return rowCountUpdated;
    }



    public int updateUserByAdmin(User user)
    {

        String updateStatement = "UPDATE " + User.TABLE_NAME
                                + " SET " + User.USERNAME + "=?,"
//                                        + User.PASSWORD + "=?,"
                                        + User.E_MAIL + "=?,"
                                        + User.PHONE + "=?,"

                                        + User.NAME + "=?,"
                                        + User.GENDER + "=?,"

                                        + User.PROFILE_IMAGE_URL + "=?,"
                                        + User.IS_ACCOUNT_PRIVATE + "=?,"
                                        + User.ABOUT + "=?"

                                + " WHERE " + User.USER_ID + " = ?";




        Connection connection = null;
        PreparedStatement statement = null;

        int rowCountUpdated = 0;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setString(++i,user.getUsername());
//            statement.setString(++i,user.getPassword());
            statement.setString(++i,user.getEmail());
            statement.setString(++i,user.getPhone());

            statement.setString(++i,user.getName());
            statement.setObject(++i,user.getGender());

            statement.setString(++i,user.getProfileImagePath());
            statement.setObject(++i,user.isAccountPrivate());
            statement.setString(++i,user.getAbout());

            statement.setObject(++i,user.getUserID());


            rowCountUpdated = statement.executeUpdate();


            System.out.println("Total rows updated: " + rowCountUpdated);


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

        return rowCountUpdated;
    }




    public int updateToken(User user)
    {




        String updateStatement = "UPDATE " + User.TABLE_NAME

                + " SET "
                + User.TOKEN + "=?,"
                + User.TIMESTAMP_TOKEN_EXPIRES + "=?"

                + " WHERE "
                + " ( " + User.USERNAME + " = ? "
                + " OR " +  User.USER_ID + " = ? "
                + " OR " + " ( " + User.E_MAIL + " = ?" + ")"
                + " OR " + " ( " + User.PHONE + " = ?" + ")"
                + ")"
                + " AND " + User.PASSWORD + " = ? ";

//        + " OR " + " CAST ( " +  User.USER_ID + " AS text ) " + " = ? "
//                + User.USERNAME + " = ?"
//                + " AND " + User.PASSWORD + " = ?";


        Connection connection = null;
        PreparedStatement statement = null;

        int rowCountUpdated = 0;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setString(++i,user.getToken());
            statement.setTimestamp(++i,user.getTimestampTokenExpires());

//            statement.setString(++i,username); // username
//            statement.setString(++i,username); // userID
//            statement.setString(++i,username); // email
//            statement.setString(++i,username); // phone
//            statement.setString(++i,token); // token

            statement.setString(++i,user.getUsername());
            statement.setObject(++i,user.getUserID());
            statement.setString(++i,user.getEmail());
            statement.setString(++i,user.getPhone());

            statement.setString(++i,user.getPassword());

            rowCountUpdated = statement.executeUpdate();

            System.out.println("Total rows updated: " + rowCountUpdated);


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

        return rowCountUpdated;
    }




    public User verifyUser(String username, String token)
    {

        boolean isFirst = true;

        String queryToken = "SELECT "

                + User.USER_ID + ","
                + User.USERNAME + ","
                + User.ROLE + ""

                + " FROM " + User.TABLE_NAME
                + " WHERE "
                + " ( " + User.USERNAME + " = ? "
                + " OR " + " CAST ( " +  User.USER_ID + " AS text ) " + " = ? "
                + " OR " + " ( " + User.E_MAIL + " = ?" + ")"
                + " OR " + " ( " + User.PHONE + " = ?" + ")" + ")"
                + " AND " + User.TOKEN + " = ? "
                + " AND " + User.TIMESTAMP_TOKEN_EXPIRES + " > now()";



        String queryPassword = "SELECT "

                + User.USER_ID + ","
                + User.USERNAME + ","
                + User.ROLE + ""

                + " FROM " + User.TABLE_NAME
                + " WHERE "
                + " ( " + User.USERNAME + " = ? "
                + " OR " + " CAST ( " +  User.USER_ID + " AS text ) " + " = ? "
                + " OR " + " ( " + User.E_MAIL + " = ?" + ")"
                + " OR " + " ( " + User.PHONE + " = ?" + ")" + ")"
                + " AND " + User.PASSWORD + " = ? ";



//        CAST (" + User.TIMESTAMP_TOKEN_EXPIRES + " AS TIMESTAMP)"



        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        //Distributor distributor = null;
        User user = null;

        try {

//            System.out.println(query);

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(queryPassword);

            int i = 0;
            statement.setString(++i,username); // username
            statement.setString(++i,username); // userID
            statement.setString(++i,username); // email
            statement.setString(++i,username); // phone
            statement.setString(++i,token); // token
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




    // fetch profile details by the user for his own profile
    public User getProfile(int userID)
    {

        boolean isFirst = true;

        String query = "SELECT "

                + User.USER_ID + ","
                + User.USERNAME + ","
                + User.E_MAIL + ","
                + User.PHONE + ","
                + User.NAME + ","
                + User.SECRET_CODE + ","
                + User.GENDER + ","
                + User.PROFILE_IMAGE_URL + ","
                + User.ROLE + ","
                + User.IS_ACCOUNT_PRIVATE + ","
                + User.ABOUT + ""

                + " FROM " + User.TABLE_NAME
                + " WHERE " + User.USER_ID  + " = ? ";



        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        //Distributor distributor = null;
        User user = null;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);

            int i = 0;


            statement.setObject(++i,userID); // username


            rs = statement.executeQuery();

            while(rs.next())
            {
                user = new User();

                user.setUserID(rs.getInt(User.USER_ID));
                user.setUsername(rs.getString(User.USERNAME));
                user.setEmail(rs.getString(User.E_MAIL));
                user.setPhone(rs.getString(User.PHONE));
                user.setName(rs.getString(User.NAME));
                user.setSecretCode(rs.getInt(User.SECRET_CODE));
                user.setGender(rs.getBoolean(User.GENDER));
                user.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));
                user.setRole(rs.getInt(User.ROLE));
                user.setAccountPrivate(rs.getBoolean(User.IS_ACCOUNT_PRIVATE));
                user.setAbout(rs.getString(User.ABOUT));
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

        return user;
    }



    public User getProfile(String username, String password)
    {

        boolean isFirst = true;

        String query = "SELECT "

                + User.USER_ID + ","
                + User.USERNAME + ","
//                + User.PASSWORD + ","
                + User.E_MAIL + ","
                + User.PHONE + ","
//                + User.IS_PHONE_VERIFIED + ","
                + User.NAME + ","
                + User.SECRET_CODE + ","
                + User.GENDER + ","
                + User.PROFILE_IMAGE_URL + ","
                + User.ROLE + ","
                + User.IS_ACCOUNT_PRIVATE + ","
                + User.ABOUT + ","
                + User.SERVICE_ACCOUNT_BALANCE + ","
                + User.IS_ACCOUNT_PRIVATE + ""
//                + User.TOKEN + ","
//                + User.TIMESTAMP_TOKEN_EXPIRES + ""

                + " FROM " + User.TABLE_NAME
                + " WHERE "
                + " ( " + User.USERNAME + " = ? "
                + " OR " + " CAST ( " +  User.USER_ID + " AS text ) " + " = ? "
                + " OR " + " ( " + User.E_MAIL + " = ?" + ")"
                + " OR " + " ( " + User.PHONE + " = ?" + ")"
                + ")"
                + " AND " + User.PASSWORD + " = ? ";



//                + " ( " + User.USERNAME + " = ? "
//                + " OR " + " CAST ( " +  User.USER_ID + " AS text ) " + " = ? )";

//        CAST (" + User.TIMESTAMP_TOKEN_EXPIRES + " AS TIMESTAMP)"



        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        //Distributor distributor = null;
        User user = null;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);

            int i = 0;


            statement.setString(++i,username); // username
            statement.setString(++i,username); // userID
            statement.setString(++i,username); // email
            statement.setString(++i,username); // phone
            statement.setString(++i,password); // password


//            statement.setString(++i,username); // username
//            statement.setString(++i,username); // userID


            rs = statement.executeQuery();

            while(rs.next())
            {
                user = new User();

                user.setUserID(rs.getInt(User.USER_ID));
                user.setUsername(rs.getString(User.USERNAME));
//                user.setPassword(rs.getString(User.PASSWORD));
                user.setEmail(rs.getString(User.E_MAIL));
                user.setPhone(rs.getString(User.PHONE));
//                user.setPhoneVerified(rs.getBoolean(User.IS_PHONE_VERIFIED));
                user.setName(rs.getString(User.NAME));
                user.setSecretCode(rs.getInt(User.SECRET_CODE));
                user.setGender(rs.getBoolean(User.GENDER));
                user.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));
                user.setRole(rs.getInt(User.ROLE));
                user.setAccountPrivate(rs.getBoolean(User.IS_ACCOUNT_PRIVATE));
                user.setServiceAccountBalance(rs.getDouble(User.SERVICE_ACCOUNT_BALANCE));
                user.setAbout(rs.getString(User.ABOUT));

//                user.setToken(rs.getString(User.TOKEN));
//                user.setTimestampTokenExpires(rs.getTimestamp(User.TIMESTAMP_TOKEN_EXPIRES));

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

        return user;
    }






    public UserEndpoint getUsers(
            Integer userRole,
            Integer shopID,
            Boolean gender,
            String sortBy,
            int limit, int offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    ) {


        String queryCount = "";

        String queryJoin = "SELECT DISTINCT "

                + User.TABLE_NAME + "." + User.USER_ID + ","
                + User.TABLE_NAME + "." + User.NAME + ","
                + User.TABLE_NAME + "." + User.ROLE + ","
                + User.TABLE_NAME + "." + User.PROFILE_IMAGE_URL + ","
                + User.TABLE_NAME + "." + User.TIMESTAMP_CREATED + ","
                + User.TABLE_NAME + "." + User.IS_VERIFIED + ""

                + " FROM " + User.TABLE_NAME
                + " LEFT OUTER JOIN " + StaffPermissions.TABLE_NAME + " ON (" + StaffPermissions.TABLE_NAME + "." + StaffPermissions.STAFF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " LEFT OUTER JOIN " + ShopStaffPermissions.TABLE_NAME + " ON (" + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.STAFF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " LEFT OUTER JOIN " + DeliveryGuyData.TABLE_NAME + " ON (" + DeliveryGuyData.TABLE_NAME + "." + DeliveryGuyData.STAFF_USER_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " WHERE TRUE ";


//        " AND " + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_STAFF_CODE




        if(userRole != null)
        {
            queryJoin = queryJoin + " AND " + User.TABLE_NAME + "." + User.ROLE + " = ?";
        }



        if(gender != null)
        {
            queryJoin = queryJoin + " AND " + User.TABLE_NAME + "." + User.GENDER + " = ?";
        }



        if(shopID!=null)
        {

            if(userRole==null || userRole==GlobalConstants.ROLE_DELIVERY_GUY_SELF_CODE)
            {
                queryJoin = queryJoin + " AND ( " + DeliveryGuyData.TABLE_NAME + "." + DeliveryGuyData.SHOP_ID + " = ? )";
            }
            else
            {
                queryJoin = queryJoin + " AND ( " + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.SHOP_ID + " = ? )";
            }


//            + " AND ( " + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_SHOP_STAFF_CODE + " AND " +  ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.SHOP_ID + " = ? )"
//            + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_DELIVERY_GUY_CODE +  " AND "
        }








        // all the non-aggregate columns which are present in select must be present in group by also.
        queryJoin = queryJoin

                + " group by "
                + DeliveryGuyData.TABLE_NAME + "." + DeliveryGuyData.DATA_ID + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.PERMISSION_ID + ","
                + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMISSION_ID + ","
                + User.TABLE_NAME + "." + User.USER_ID;




        queryCount = queryJoin;



        if(sortBy!=null && !sortBy.equals(""))
        {
            String queryPartSortBy = " ORDER BY " + sortBy;
            queryJoin = queryJoin + queryPartSortBy;
        }





        queryJoin = queryJoin + " LIMIT " + limit + " " + " OFFSET " + offset;





		/*

		Applying filters Ends

		 */

        // Applying filters




        queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";


        UserEndpoint endPoint = new UserEndpoint();
        ArrayList<User> itemList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

//        PreparedStatement statementCount = null;
//        ResultSet resultSetCount = null;

        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata)
            {
                statement = connection.prepareStatement(queryJoin);


                if(userRole!=null)
                {
                    statement.setObject(++i,userRole);
                }

                if(gender!=null)
                {
                    statement.setObject(++i,gender);
                }


                if(shopID!=null)
                {
                    statement.setObject(++i,shopID);
//                    statement.setObject(++i,shopID);
                }


                rs = statement.executeQuery();

                while(rs.next())
                {

                    User user = new User();

                    user.setUserID(rs.getInt(User.USER_ID));
                    user.setName(rs.getString(User.NAME));
                    user.setRole(rs.getInt(User.ROLE));
                    user.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));
                    user.setTimestampCreated(rs.getTimestamp(User.TIMESTAMP_CREATED));
                    user.setVerified(rs.getBoolean(User.IS_VERIFIED));

                    itemList.add(user);
                }

                endPoint.setResults(itemList);

            }


            if(getRowCount)
            {
                statement = connection.prepareStatement(queryCount);

                i = 0;


                if(userRole!=null)
                {
                    statement.setObject(++i,userRole);
                }


                if(gender!=null)
                {
                    statement.setObject(++i,gender);
                }




                if(shopID!=null)
                {
                    statement.setObject(++i,shopID);
//                    statement.setObject(++i,shopID);
                }




                rs = statement.executeQuery();

                while(rs.next())
                {
                    endPoint.setItemCount(rs.getInt("item_count"));
                }
            }






        }
        catch (SQLException e) {
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





    // fetch user details for admin and staff
    public User getUserDetails(int userID)
    {

        String query = "SELECT "

                + User.USER_ID + ","
                + User.USERNAME + ","
                + User.E_MAIL + ","
                + User.PHONE + ","
                + User.NAME + ","
                + User.SECRET_CODE + ","
                + User.GENDER + ","
                + User.PROFILE_IMAGE_URL + ","
                + User.ROLE + ","
                + User.IS_ACCOUNT_PRIVATE + ","
                + User.ABOUT + ""

                + " FROM " + User.TABLE_NAME
                + " WHERE " + User.USER_ID  + " = ? ";



        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;


        //Distributor distributor = null;
        User user = null;

        try {

            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);

            int i = 0;


            statement.setObject(++i,userID); // username


            rs = statement.executeQuery();

            while(rs.next())
            {
                user = new User();

                user.setUserID(rs.getInt(User.USER_ID));
                user.setUsername(rs.getString(User.USERNAME));
                user.setEmail(rs.getString(User.E_MAIL));
                user.setPhone(rs.getString(User.PHONE));
                user.setName(rs.getString(User.NAME));
                user.setSecretCode(rs.getInt(User.SECRET_CODE));
                user.setGender(rs.getBoolean(User.GENDER));
                user.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));
                user.setRole(rs.getInt(User.ROLE));
                user.setAccountPrivate(rs.getBoolean(User.IS_ACCOUNT_PRIVATE));
                user.setAbout(rs.getString(User.ABOUT));
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

        return user;
    }


}


