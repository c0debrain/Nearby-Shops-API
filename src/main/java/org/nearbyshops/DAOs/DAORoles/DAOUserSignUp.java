package org.nearbyshops.DAOs.DAORoles;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.ModelRoles.EmailVerificationCode;
import org.nearbyshops.Model.ModelRoles.PhoneVerificationCode;
import org.nearbyshops.Model.ModelRoles.User;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.Model.ModelBilling.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sumeet on 14/8/17.
 */
public class DAOUserSignUp {



    private HikariDataSource dataSource = Globals.getDataSource();




    // adds joining credit and referral credit
    public int registerUsingEmailOrPhone(User user,
                                         boolean applyJoiningCredit,
                                         boolean applyReferralCredit,
                                         boolean getRowCount)
    {

        Connection connection = null;
        PreparedStatement statement = null;



        int idOfInsertedRow = -1;
        int rowCountItems = -1;


        String insertProfileEmail = "";
        String insertProfilePhone = "";
        String insertShop = "";

        String applyJoiningCreditSQL = "";
        String createTransactionJoiningCredit = "";

        String applyReferralCreditSQL = "";
        String createTransactionReferralCredit = "";



        insertProfileEmail = "INSERT INTO "
                + User.TABLE_NAME
                + "("

                + User.PASSWORD + ","
                + User.E_MAIL + ","

                + User.NAME + ","
                + User.GENDER + ","
                + User.SECRET_CODE + ","

                + User.PROFILE_IMAGE_URL + ","
                + User.ROLE + ","
                + User.IS_ACCOUNT_PRIVATE + ","
                + User.REFERRED_BY + ","
                + User.IS_REFERRER_CREDITED + ","
                + User.ABOUT + ""
                + ") "
                + " Select "
                + " ?,? ,?,?,? ,?,?,?,?,?,? "
                + " from " + EmailVerificationCode.TABLE_NAME
                + " WHERE "
                + "("
                + "(" + EmailVerificationCode.TABLE_NAME + "." + EmailVerificationCode.EMAIL + " = ? " + ")"
                + " and "
                + "(" + EmailVerificationCode.VERIFICATION_CODE + " = ?" + ")"
                + " and "
                + "(" + EmailVerificationCode.TIMESTAMP_EXPIRES + " > now()" + ")"
                + ")";




        insertProfilePhone = "INSERT INTO "
                + User.TABLE_NAME
                + "("

                + User.PASSWORD + ","
                + User.PHONE + ","

                + User.NAME + ","
                + User.GENDER + ","
                + User.SECRET_CODE + ","

                + User.PROFILE_IMAGE_URL + ","
                + User.ROLE + ","
                + User.IS_ACCOUNT_PRIVATE + ","
                + User.REFERRED_BY + ","
                + User.IS_REFERRER_CREDITED + ","
                + User.ABOUT + ""
                + ") "
                + " Select "
                + " ?,? ,?,?,? ,?,?,?,?,?,? "
                + " from " + PhoneVerificationCode.TABLE_NAME
                + " WHERE "
                + "("
                + "(" + PhoneVerificationCode.TABLE_NAME + "." + PhoneVerificationCode.PHONE + " = ? " + ")"
                + " and "
                + "(" + PhoneVerificationCode.VERIFICATION_CODE + " = ?" + ")"
                + " and "
                + "(" + PhoneVerificationCode.TIMESTAMP_EXPIRES + " > now()" + ")"
                + ")";






        insertShop = " INSERT INTO " + Shop.TABLE_NAME
                + "(" + Shop.SHOP_ADMIN_ID + ","
                + Shop.SHOP_ENABLED + ","
                + Shop.SHOP_WAITLISTED + ""

                + ") " +
                " VALUES( ?,?,? )";





        // add joining credit to the users account
        applyJoiningCreditSQL =  " UPDATE " + User.TABLE_NAME
                                + " SET " + User.SERVICE_ACCOUNT_BALANCE + " = " + User.SERVICE_ACCOUNT_BALANCE + " + ?"
                                + " WHERE " + User.TABLE_NAME + "." + User.USER_ID + " = ? ";


        createTransactionJoiningCredit = "INSERT INTO " + Transaction.TABLE_NAME
                + "("

                + Transaction.USER_ID + ","

                + Transaction.TITLE + ","
                + Transaction.DESCRIPTION + ","

                + Transaction.TRANSACTION_TYPE + ","
                + Transaction.TRANSACTION_AMOUNT + ","

                + Transaction.IS_CREDIT + ","

//                            + Transaction.CURRENT_DUES_BEFORE_TRANSACTION + ","
                + Transaction.BALANCE_AFTER_TRANSACTION + ""

                + ") "
                + " SELECT "

                + User.TABLE_NAME + "." + User.USER_ID + ","
                + " '" + Transaction.TITLE_JOINING_CREDIT_FOR_DRIVER + "',"
                + " '" + Transaction.DESCRIPTION_JOINING_CREDIT_FOR_DRIVEr + "',"

                + Transaction.TRANSACTION_TYPE_JOINING_CREDIT + ","
                + " ? ,"

                + " true " + ","

//                            + User.TABLE_NAME + "." + User.SERVICE_ACCOUNT_BALANCE + " - ?,"
                + User.TABLE_NAME + "." + User.SERVICE_ACCOUNT_BALANCE + ""

                + " FROM " + User.TABLE_NAME
                + " WHERE " + User.TABLE_NAME + "." + User.USER_ID + " = ?";




//
//        // add referral charges to the user bill
//        updateDUESReferral =  " UPDATE " + User.TABLE_NAME
//                        + " SET "
//                        + User.SERVICE_ACCOUNT_BALANCE + " = " + User.SERVICE_ACCOUNT_BALANCE + " - ?,"
//                        + User.TOTAL_CREDITS + " = " + User.TOTAL_CREDITS + " - ?"
//                        + " FROM " + User.TABLE_NAME + " as registered_user"
//                        + " WHERE " + "registered_user." + User.REFERRED_BY + " = " + User.TABLE_NAME + "." + User.USER_ID
//                        + " AND " + "registered_user." + User.USER_ID + " = ? ";






        // add referral credit
        applyReferralCreditSQL =  " UPDATE " + User.TABLE_NAME
                + " SET " + User.SERVICE_ACCOUNT_BALANCE + " = " + User.SERVICE_ACCOUNT_BALANCE + " + ?"
                + " WHERE " + User.TABLE_NAME + "." + User.USER_ID + " = ? ";




        createTransactionReferralCredit = "INSERT INTO " + Transaction.TABLE_NAME
                + "("

                + Transaction.USER_ID + ","

                + Transaction.TITLE + ","
                + Transaction.DESCRIPTION + ","

                + Transaction.TRANSACTION_TYPE + ","
                + Transaction.TRANSACTION_AMOUNT + ","

                + Transaction.IS_CREDIT + ","

//                + Transaction.CURRENT_DUES_BEFORE_TRANSACTION + ","
                + Transaction.BALANCE_AFTER_TRANSACTION + ""

                + ") "
                + " SELECT "

                + User.TABLE_NAME + "." + User.USER_ID + ","
                + " '" + Transaction.TITLE_REFERRAL_CREDIT_APPLIED + "',"
                + " '" + Transaction.DESCRIPTION_REFERRAL_CREDIT_APPLIED + "',"

                + Transaction.TRANSACTION_TYPE_USER_REFERRAL_CREDIT + ","
                + " ? ,"

                + " true " + ","

//                + User.TABLE_NAME + "." + User.SERVICE_ACCOUNT_BALANCE + " - ?,"
                + User.TABLE_NAME + "." + User.SERVICE_ACCOUNT_BALANCE + ""

                + " FROM " + User.TABLE_NAME
                + " WHERE " + User.TABLE_NAME + "." + User.USER_ID + " = ?";





        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            int i = 0;



            if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
            {
                statement = connection.prepareStatement(insertProfileEmail,PreparedStatement.RETURN_GENERATED_KEYS);


                statement.setString(++i,user.getPassword());
                statement.setString(++i,user.getEmail());


                statement.setString(++i,user.getName());
                statement.setObject(++i,user.getGender());
                statement.setObject(++i,Globals.generateOTP(5));

                statement.setString(++i,user.getProfileImagePath());
                statement.setObject(++i,user.getRole());
                statement.setObject(++i,user.isAccountPrivate());
                statement.setObject(++i,user.getReferredBy());

                if(user.getRole()== GlobalConstants.ROLE_END_USER_CODE)
                {
                    statement.setObject(++i,true);
                }
                else
                {
                    statement.setObject(++i,false);
                }

                statement.setString(++i,user.getAbout());



                // check email is verification code to ensure email belongs to user
                statement.setString(++i,user.getEmail());
                statement.setString(++i,user.getRt_email_verification_code());


                rowCountItems = statement.executeUpdate();


                ResultSet rs = statement.getGeneratedKeys();

                if(rs.next())
                {
                    idOfInsertedRow = rs.getInt(1);
                }


            }
            else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
            {

                statement = connection.prepareStatement(insertProfilePhone, PreparedStatement.RETURN_GENERATED_KEYS);


                statement.setString(++i, user.getPassword());
                statement.setString(++i, user.getPhoneWithCountryCode());

                statement.setString(++i, user.getName());
                statement.setObject(++i, user.getGender());
                statement.setObject(++i,Globals.generateOTP(5));

                statement.setString(++i, user.getProfileImagePath());
                statement.setObject(++i, user.getRole());
                statement.setObject(++i, user.isAccountPrivate());
                statement.setObject(++i, user.getReferredBy());




                if (user.getRole() == GlobalConstants.ROLE_END_USER_CODE) {
                    statement.setObject(++i, true);
                } else {
                    statement.setObject(++i, false);
                }


                statement.setString(++i, user.getAbout());



                // check phone is verified or not to ensure phone belongs to user
                statement.setString(++i, user.getPhoneWithCountryCode());
                statement.setString(++i, user.getRt_phone_verification_code());

                rowCountItems = statement.executeUpdate();


                ResultSet rs = statement.getGeneratedKeys();

                if (rs.next()) {
                    idOfInsertedRow = rs.getInt(1);
                }

            }




            if(user.getRole()== GlobalConstants.ROLE_SHOP_ADMIN_CODE)
            {
                if (rowCountItems == 1)
                {

                    statement = connection.prepareStatement(insertShop);
                    i = 0;

                    statement.setObject(++i,idOfInsertedRow);
                    statement.setObject(++i,null);
                    statement.setObject(++i,false);

                    statement.executeUpdate();

                }
            }





            if(rowCountItems == 1)
            {

                if(applyJoiningCredit)
                {

                    statement = connection.prepareStatement(applyJoiningCreditSQL);
                    i = 0;

                    if(user.getRole()== GlobalConstants.ROLE_SHOP_ADMIN_CODE)
                    {
                        statement.setObject(++i, GlobalConstants.joining_credit_for_shop_owner_value);
                    }
                    else if(user.getRole()== GlobalConstants.ROLE_END_USER_CODE)
                    {
                        statement.setObject(++i, GlobalConstants.joining_credit_for_end_user_value);
                    }
                    else
                    {
                        statement.setObject(++i,0);
                    }


                    statement.setObject(++i,idOfInsertedRow);

                    rowCountItems = statement.executeUpdate();




                    statement = connection.prepareStatement(createTransactionJoiningCredit);
                    i = 0;


                    if(user.getRole()== GlobalConstants.ROLE_SHOP_ADMIN_CODE)
                    {

                        statement.setObject(++i, GlobalConstants.joining_credit_for_shop_owner_value);
                    }
                    else if(user.getRole()== GlobalConstants.ROLE_END_USER_CODE)
                    {
                        statement.setObject(++i, GlobalConstants.joining_credit_for_end_user_value);
                    }
                    else
                    {
                        statement.setObject(++i,0);
                    }

                    statement.setObject(++i,idOfInsertedRow);
                    rowCountItems = statement.executeUpdate();


                }


                if(applyReferralCredit)
                {
                    // apply referral credit

                    statement = connection.prepareStatement(applyReferralCreditSQL);
                    i = 0;


                    if(user.getRole()== GlobalConstants.ROLE_END_USER_CODE)
                    {
                        statement.setObject(++i, GlobalConstants.REFERRAL_CREDIT_FOR_END_USER_REGISTRATION);
                    }
                    else
                    {
                        statement.setObject(++i,0);
                    }



                    statement.setObject(++i,user.getReferredBy());
                    rowCountItems = statement.executeUpdate();




                    statement = connection.prepareStatement(createTransactionReferralCredit);
                    i = 0;


                    if(user.getRole()== GlobalConstants.ROLE_END_USER_CODE)
                    {
                        statement.setObject(++i, GlobalConstants.REFERRAL_CREDIT_FOR_END_USER_REGISTRATION);
                    }
                    else
                    {
                        statement.setObject(++i,0);
                    }

                    statement.setObject(++i,user.getReferredBy());
                    rowCountItems = statement.executeUpdate();

                }

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






    public int registerUsingUsername(User user, boolean getRowCount)
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

                + User.NAME + ","
                + User.GENDER + ","

                + User.PROFILE_IMAGE_URL + ","
                + User.ROLE + ","
                + User.IS_ACCOUNT_PRIVATE + ","
                + User.ABOUT + ""
                + ") values(?,? ,?,? ,?,?,?,? )";


        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);


            statement = connection.prepareStatement(insertItemSubmission,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 0;

            statement.setString(++i,user.getUsername());
            statement.setString(++i,user.getPassword());

            statement.setString(++i,user.getName());
            statement.setObject(++i,user.getGender());

            statement.setString(++i,user.getProfileImagePath());
            statement.setObject(++i,user.getRole());
            statement.setObject(++i,user.isAccountPrivate());
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

            statement.setObject(++i,GlobalConstants.ROLE_ADMIN_CODE);
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
