package org.nearbyshops.DAORoles;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.ModelBilling.Transaction;
import org.nearbyshops.ModelRoles.*;
import org.nearbyshops.ModelRoles.Endpoints.UserEndpoint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAODeprecatedFunctions {



    private HikariDataSource dataSource = Globals.getDataSource();



//    Taken from DAOUserNew



    public int registerUsingPhone(User user, boolean getRowCount,
                                  double joiningCredit,
                                  double referralCredit,
                                  boolean isReferrerCredited)

    {

        Connection connection = null;
        PreparedStatement statement = null;

//        PreparedStatement statementInsertShop = null;

        // for applying joining credit
//        PreparedStatement statementUpdateDUES = null;
//        PreparedStatement statementCreateTransaction = null;

        // for applying referral credit
//        PreparedStatement statementUpdateDUESReferral = null;
//        PreparedStatement statementTransactionReferral = null;


        int idOfInsertedRow = -1;
        int rowCountItems = -1;



        String insertShop = "";
        String updateDUES = "";
        String createTransaction = "";
        String updateDUESReferral = "";
        String createTransactionReferral = "";



        String insertProfile = "INSERT INTO "
                + User.TABLE_NAME
                + "("

                + User.PASSWORD + ","
                + User.PHONE + ","

                + User.NAME + ","
                + User.GENDER + ","

                + User.PROFILE_IMAGE_URL + ","
                + User.ROLE + ","
                + User.IS_ACCOUNT_PRIVATE + ","
                + User.REFERRED_BY + ","
                + User.IS_REFERRER_CREDITED + ","
                + User.ABOUT + ""
                + ") "
                + " Select "
                + " ?,? ,?,? ,?,?,?,?,?,? "
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




        // add joining credit
        updateDUES =  " UPDATE " + User.TABLE_NAME
                + " SET " + User.SERVICE_ACCOUNT_BALANCE + " = " + User.SERVICE_ACCOUNT_BALANCE + " + ?"
                + " WHERE " + User.TABLE_NAME + "." + User.USER_ID + " = ? ";




        createTransaction = "INSERT INTO " + Transaction.TABLE_NAME
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
                + " '" + Transaction.TITLE_JOINING_CREDIT_FOR_DRIVER + "',"
                + " '" + Transaction.DESCRIPTION_JOINING_CREDIT_FOR_DRIVEr + "',"

                + Transaction.TRANSACTION_TYPE_JOINING_CREDIT + ","
                + " ? ,"

                + " true " + ","

//                + User.TABLE_NAME + "." + User.SERVICE_ACCOUNT_BALANCE + " - ?,"
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






        // add referral credit to the user
        updateDUESReferral =  " UPDATE " + User.TABLE_NAME
                + " SET " + User.SERVICE_ACCOUNT_BALANCE + " = " + User.SERVICE_ACCOUNT_BALANCE + " + ?"
                + " WHERE " + User.TABLE_NAME + "." + User.USER_ID + " = ? ";




        createTransactionReferral = "INSERT INTO " + Transaction.TABLE_NAME
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


            statement = connection.prepareStatement(insertProfile, PreparedStatement.RETURN_GENERATED_KEYS);


//            statement.setString(++i,user.getUsername());

            statement.setString(++i, user.getPassword());
            statement.setString(++i, user.getPhoneWithCountryCode());

            statement.setString(++i, user.getName());
            statement.setObject(++i, user.getGender());

            statement.setString(++i, user.getProfileImagePath());
            statement.setObject(++i, user.getRole());
            statement.setObject(++i, user.isAccountPrivate());
            statement.setObject(++i, user.getReferredBy());


//            if (user.getRole() == GlobalConstants.ROLE_END_USER_CODE) {
//                statement.setObject(++i, true);
//            } else {
//                statement.setObject(++i, false);
//            }


            statement.setObject(++i,isReferrerCredited);


            statement.setString(++i, user.getAbout());





            // check phone is verified or not to ensure phone belongs to user
            statement.setString(++i, user.getPhoneWithCountryCode());
            statement.setString(++i, user.getRt_phone_verification_code());

            rowCountItems = statement.executeUpdate();


            ResultSet rs = statement.getGeneratedKeys();

            if (rs.next()) {
                idOfInsertedRow = rs.getInt(1);
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

                statement = connection.prepareStatement(updateDUES);
                i = 0;

                statement.setObject(++i,joiningCredit);
//                statementUpdateDUES.setObject(++i,joiningCredit);
                statement.setObject(++i,idOfInsertedRow);

                rowCountItems = statement.executeUpdate();







                statement = connection.prepareStatement(createTransaction);
                i = 0;

                statement.setObject(++i,joiningCredit);
                statement.setObject(++i,idOfInsertedRow);

                rowCountItems = statement.executeUpdate();


                statement = connection.prepareStatement(updateDUESReferral);
                i = 0;

                statement.setObject(++i,referralCredit);
//                statementUpdateDUESReferral.setObject(++i,referralCredit);

                statement.setObject(++i,user.getReferredBy());
                rowCountItems = statement.executeUpdate();




                statement = connection.prepareStatement(createTransactionReferral);
                i = 0;


                statement.setObject(++i,referralCredit);

                statement.setObject(++i,user.getReferredBy());
                rowCountItems = statement.executeUpdate();

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





    public int updateShopAdmin(User user)
    {

        String updateStatement = "UPDATE " + User.TABLE_NAME

                + " SET "

//                + User.USERNAME + "=?,"
//                + User.PASSWORD + "=?,"
//                + User.E_MAIL + "=?,"

//                + User.PHONE + "=?,"
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

//            statement.setString(++i,user.getUsername());
//            statement.setString(++i,user.getPassword());
//            statement.setString(++i,user.getEmail());

//            statement.setString(++i,user.getPhone());

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




    public int registerUsingEmailNoCredits(
            User user, boolean getRowCount
    )
    {

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statementInsertShop = null;
        PreparedStatement statementPermissions = null;
        PreparedStatement statementDeliveryGuyData = null;


        int idOfInsertedRow = -1;
        int rowCountItems = -1;


        String insertItemSubmission = "";
        String insertShop = "";



        insertItemSubmission = "INSERT INTO "
                + User.TABLE_NAME
                + "("

                + User.PASSWORD + ","
                + User.E_MAIL + ","

                + User.NAME + ","
                + User.GENDER + ","

                + User.PROFILE_IMAGE_URL + ","
                + User.ROLE + ","
                + User.IS_ACCOUNT_PRIVATE + ","
                + User.REFERRED_BY + ","
                + User.IS_REFERRER_CREDITED + ","
                + User.ABOUT + ""
                + ") "
                + " Select "
                + " ?,? ,?,? ,?,?,?,?,?,? "
                + " from " + EmailVerificationCode.TABLE_NAME
                + " WHERE "
                + "("
                + "(" + EmailVerificationCode.TABLE_NAME + "." + EmailVerificationCode.EMAIL + " = ? " + ")"
                + " and "
                + "(" + EmailVerificationCode.VERIFICATION_CODE + " = ?" + ")"
                + " and "
                + "(" + EmailVerificationCode.TIMESTAMP_EXPIRES + " > now()" + ")"
                + ")";







        insertShop = " INSERT INTO " + Shop.TABLE_NAME
                + "(" + Shop.SHOP_ADMIN_ID + ","
                + Shop.SHOP_ENABLED + ","
                + Shop.SHOP_WAITLISTED + ""

                + ") " +
                " VALUES( ?,?,? )";





        String insertShopStaffPermissions =

                "INSERT INTO " + ShopStaffPermissions.TABLE_NAME
                        + "("
                        + ShopStaffPermissions.STAFF_ID + ","
                        + ShopStaffPermissions.SHOP_ID + ""
                        + ") values(?,?)";


        String insertDeliveryGuyData =

                "INSERT INTO " + DeliveryGuyData.TABLE_NAME
                        + "("
                        + DeliveryGuyData.STAFF_USER_ID + ","
                        + DeliveryGuyData.IS_EMPLOYED_BY_SHOP + ","
                        + DeliveryGuyData.SHOP_ID + ""
                        + ") values(?,?,?)";





        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);


            statement = connection.prepareStatement(insertItemSubmission,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 0;

//            statement.setString(++i,user.getUsername());
            statement.setString(++i,user.getPassword());
            statement.setString(++i,user.getEmail());

//            statement.setString(++i,user.getPhone());
            statement.setString(++i,user.getName());
            statement.setObject(++i,user.getGender());

            statement.setString(++i,user.getProfileImagePath());
            statement.setObject(++i,user.getRole());
            statement.setObject(++i,user.isAccountPrivate());
            statement.setObject(++i,user.getReferredBy());

            statement.setObject(++i,false);

            statement.setString(++i,user.getAbout());


//             check username is not null
//            statement.setString(++i,user.getUsername());


            // check email is verification code to ensure email belongs to user
            statement.setString(++i,user.getEmail());
            statement.setString(++i,user.getRt_email_verification_code());

            // check phone is verified or not to ensure phone belongs to user
//            statement.setString(++i,user.getPhone());
//            statement.setString(++i,user.getRt_phone_verification_code());

            rowCountItems = statement.executeUpdate();


            ResultSet rs = statement.getGeneratedKeys();

            if(rs.next())
            {
                idOfInsertedRow = rs.getInt(1);
            }



            if(user.getRole()== GlobalConstants.ROLE_SHOP_ADMIN_CODE)
            {
                if (rowCountItems == 1)
                {

                    statementInsertShop = connection.prepareStatement(insertShop);
                    i = 0;

                    statementInsertShop.setObject(++i,idOfInsertedRow);
                    statementInsertShop.setObject(++i,null);
                    statementInsertShop.setObject(++i,false);
                    statementInsertShop.executeUpdate();
                }
            }





            if(idOfInsertedRow!=-1 && user.getRole()==GlobalConstants.ROLE_SHOP_STAFF_CODE)
            {

                statementPermissions = connection.prepareStatement(insertShopStaffPermissions,PreparedStatement.RETURN_GENERATED_KEYS);
                i = 0;


                ShopStaffPermissions permissions = user.getRt_shop_staff_permissions();

                statementPermissions.setInt(++i,idOfInsertedRow);
                statementPermissions.setInt(++i,permissions.getShopID());

                rowCountItems = statementPermissions.executeUpdate();
            }







            if(idOfInsertedRow!=-1)
            {

                if( (user.getRole()== GlobalConstants.ROLE_DELIVERY_GUY_SELF_CODE || user.getRole() ==GlobalConstants.ROLE_DELIVERY_GUY_CODE))
                {
                    statementDeliveryGuyData = connection.prepareStatement(insertDeliveryGuyData,PreparedStatement.RETURN_GENERATED_KEYS);
                    i = 0;



                    DeliveryGuyData deliveryGuyData = user.getRt_delivery_guy_data();

                    statementDeliveryGuyData.setInt(++i,idOfInsertedRow);
                    statementDeliveryGuyData.setBoolean(++i,deliveryGuyData.isEmployedByShop());
                    statementDeliveryGuyData.setInt(++i,deliveryGuyData.getShopID());

                    rowCountItems = statementDeliveryGuyData.executeUpdate();

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


            if (statementInsertShop != null) {
                try {
                    statementInsertShop.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }




            if (statementDeliveryGuyData != null) {
                try {
                    statementDeliveryGuyData.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            if (statementPermissions != null) {
                try {
                    statementPermissions.close();
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








    public int registerUsingPhoneNoCredits(User user, boolean getRowCount)
    {

        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statementPermissions = null;
        PreparedStatement statementDeliveryGuyData = null;


        int idOfInsertedRow = -1;
        int rowCountItems = -1;




        String insertUser = "INSERT INTO "
                + User.TABLE_NAME
                + "("

                + User.PASSWORD + ","
                + User.PHONE + ","

                + User.NAME + ","
                + User.GENDER + ","

                + User.PROFILE_IMAGE_URL + ","
                + User.ROLE + ","
                + User.IS_ACCOUNT_PRIVATE + ","
                + User.ABOUT + ""
                + ") "
                + " Select "
                + " ?,? ,?,? ,?,?,?,? "
                + " from " + PhoneVerificationCode.TABLE_NAME
                + " WHERE "
                + "("
                + "(" + PhoneVerificationCode.TABLE_NAME + "." + PhoneVerificationCode.PHONE + " = ? " + ")"
                + " and "
                + "(" + PhoneVerificationCode.VERIFICATION_CODE + " = ?" + ")"
                + " and "
                + "(" + PhoneVerificationCode.TIMESTAMP_EXPIRES + " > now()" + ")"
                + ")";





        String insertShopStaffPermissions =

                "INSERT INTO " + ShopStaffPermissions.TABLE_NAME
                        + "("
                        + ShopStaffPermissions.STAFF_ID + ","
                        + ShopStaffPermissions.SHOP_ID + ""
                        + ") values(?,?)";


        String insertDeliveryGuyData =

                "INSERT INTO " + DeliveryGuyData.TABLE_NAME
                        + "("
                        + DeliveryGuyData.STAFF_USER_ID + ","
                        + DeliveryGuyData.IS_EMPLOYED_BY_SHOP + ","
                        + DeliveryGuyData.SHOP_ID + ""
                        + ") values(?,?,?)";






        // add referral charges to the user bill


        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);


            statement = connection.prepareStatement(insertUser,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 0;


//            statement.setString(++i,user.getUsername());

            statement.setString(++i,user.getPassword());
            statement.setString(++i,user.getPhoneWithCountryCode());

            statement.setString(++i,user.getName());
            statement.setObject(++i,user.getGender());

            statement.setString(++i,user.getProfileImagePath());
            statement.setObject(++i,user.getRole());
            statement.setObject(++i,user.isAccountPrivate());

//            statement.setObject(++i,user.getReferredBy());
//
//            if(user.getRole()==GlobalConstantsNBS.ROLE_END_USER_CODE)
//            {
//                statement.setObject(++i,true);
//            }
//            else
//            {
//                statement.setObject(++i,false);
//            }


            statement.setString(++i,user.getAbout());


            // check phone is verified or not to ensure phone belongs to user
            statement.setString(++i,user.getPhoneWithCountryCode());
            statement.setString(++i,user.getRt_phone_verification_code());

            rowCountItems = statement.executeUpdate();


            ResultSet rs = statement.getGeneratedKeys();

            if(rs.next())
            {
                idOfInsertedRow = rs.getInt(1);
            }





            if(idOfInsertedRow!=-1 && user.getRole()==GlobalConstants.ROLE_SHOP_STAFF_CODE)
            {

                statementPermissions = connection.prepareStatement(insertShopStaffPermissions,PreparedStatement.RETURN_GENERATED_KEYS);
                i = 0;


                ShopStaffPermissions permissions = user.getRt_shop_staff_permissions();

                statementPermissions.setInt(++i,idOfInsertedRow);
                statementPermissions.setInt(++i,permissions.getShopID());

                rowCountItems = statementPermissions.executeUpdate();
            }







            if(idOfInsertedRow!=-1)
            {

                if( (user.getRole()== GlobalConstants.ROLE_DELIVERY_GUY_SELF_CODE || user.getRole() ==GlobalConstants.ROLE_DELIVERY_GUY_CODE))
                {
                    statementDeliveryGuyData = connection.prepareStatement(insertDeliveryGuyData,PreparedStatement.RETURN_GENERATED_KEYS);
                    i = 0;



                    DeliveryGuyData deliveryGuyData = user.getRt_delivery_guy_data();

                    statementDeliveryGuyData.setInt(++i,idOfInsertedRow);
                    statementDeliveryGuyData.setBoolean(++i,deliveryGuyData.isEmployedByShop());
                    statementDeliveryGuyData.setInt(++i,deliveryGuyData.getShopID());

                    rowCountItems = statementDeliveryGuyData.executeUpdate();

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



            if (statementPermissions != null) {
                try {
                    statementPermissions.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            if (statementDeliveryGuyData != null) {
                try {
                    statementDeliveryGuyData.close();
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







//    Taken from DAOUserNew

    /*
    *
    *
    *
    *
    *
    * */




    public int updateStaffProfile(User user)
    {

        String updateStatement = "UPDATE " + User.TABLE_NAME

                + " SET "

//                + User.USERNAME + "=?,"
//                + User.PASSWORD + "=?,"
//                + User.E_MAIL + "=?,"
//                + User.PHONE + "=?,"
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

//            statement.setString(++i,user.getUsername());
//            statement.setString(++i,user.getPassword());
//            statement.setString(++i,user.getEmail());
//            statement.setString(++i,user.getPhone());

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




    public int updateStaffByAdmin(User user)
    {

        String updateStatement = "UPDATE " + User.TABLE_NAME

                + " SET "

                + User.NAME + "=?,"
                + User.GENDER + "=?,"

                + User.PROFILE_IMAGE_URL + "=?,"
                + User.IS_ACCOUNT_PRIVATE + "=?,"
                + User.ABOUT + "=?,"
                + User.IS_VERIFIED + "=?"

                + " WHERE " + User.USER_ID + " = ?";




        String insertStaffPermissions =

                "INSERT INTO " + StaffPermissions.TABLE_NAME
                        + "("
                        + StaffPermissions.STAFF_ID + ","
                        + StaffPermissions.DESIGNATION + ","
                        + StaffPermissions.PERMIT_CREATE_UPDATE_ITEM_CATEGORIES + ","
                        + StaffPermissions.PERMIT_CREATE_UPDATE_ITEMS + ","
                        + StaffPermissions.PERMIT_APPROVE_SHOPS + ","
                        + StaffPermissions.MAX_EARNINGS_LIMIT + ""
                        + ") values(?,?,?,?,?,?)"
                        + " ON CONFLICT (" + StaffPermissions.STAFF_ID + ")"
                        + " DO UPDATE "
                        + " SET "
                        + StaffPermissions.DESIGNATION + "= excluded." + StaffPermissions.DESIGNATION + " , "
                        + StaffPermissions.PERMIT_CREATE_UPDATE_ITEM_CATEGORIES + "= excluded." + StaffPermissions.PERMIT_CREATE_UPDATE_ITEM_CATEGORIES + " , "
                        + StaffPermissions.PERMIT_CREATE_UPDATE_ITEMS + "= excluded." + StaffPermissions.PERMIT_CREATE_UPDATE_ITEMS  + ","
                        + StaffPermissions.PERMIT_APPROVE_SHOPS + "= excluded." + StaffPermissions.PERMIT_APPROVE_SHOPS  + ","
                        + StaffPermissions.MAX_EARNINGS_LIMIT + "= excluded." + StaffPermissions.MAX_EARNINGS_LIMIT;






        Connection connection = null;
        PreparedStatement statement = null;

        int rowCountUpdated = 0;

        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);



            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setString(++i,user.getName());
            statement.setObject(++i,user.getGender());

            statement.setString(++i,user.getProfileImagePath());
            statement.setObject(++i,user.isAccountPrivate());
            statement.setString(++i,user.getAbout());
            statement.setObject(++i,user.isVerified());

            statement.setObject(++i,user.getUserID());


            rowCountUpdated = statement.executeUpdate();
            System.out.println("Total rows updated: " + rowCountUpdated);


            statement = connection.prepareStatement(insertStaffPermissions,PreparedStatement.RETURN_GENERATED_KEYS);
            i = 0;

            StaffPermissions permissions = user.getRt_staff_permissions();

            if(permissions!=null)
            {
                statement.setObject(++i,user.getUserID());
                statement.setString(++i,permissions.getDesignation());
                statement.setObject(++i,permissions.isPermitCreateUpdateItemCat());
                statement.setObject(++i,permissions.isPermitCreateUpdateItems());
                statement.setObject(++i,permissions.isPermitApproveShops());
                statement.setObject(++i,permissions.getMaxEarnings());

                statement.executeUpdate();
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








    public UserEndpoint getStaffForAdmin(
            Double latPickUp, Double lonPickUp,
            Boolean permitCreateUpdateItemCat,
            Boolean permitCreateUpdateItems,
            Boolean gender,
            String sortBy,
            Integer limit, Integer offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    ) {


        boolean isfirst = true;

        String queryCount = "";


        String queryJoin = "SELECT DISTINCT "


                + "6371 * acos( cos( radians("
                + latPickUp + ")) * cos( radians(" +  StaffPermissions.LAT_CURRENT +  ") ) * cos(radians(" + StaffPermissions.LON_CURRENT +  ") - radians("
                + lonPickUp + "))"
                + " + sin( radians(" + latPickUp + ")) * sin(radians(" + StaffPermissions.LAT_CURRENT + "))) as distance" + ","

                + User.TABLE_NAME + "." + User.USER_ID + ","
                + User.TABLE_NAME + "." + User.USERNAME + ","
                + User.TABLE_NAME + "." + User.E_MAIL + ","
                + User.TABLE_NAME + "." + User.PHONE + ","

                + User.TABLE_NAME + "." + User.NAME + ","
                + User.TABLE_NAME + "." + User.GENDER + ","

                + User.TABLE_NAME + "." + User.PROFILE_IMAGE_URL + ","
                + User.TABLE_NAME + "." + User.IS_ACCOUNT_PRIVATE + ","
                + User.TABLE_NAME + "." + User.ABOUT + ","

                + User.TABLE_NAME + "." + User.TIMESTAMP_CREATED + ","
                + User.TABLE_NAME + "." + User.IS_VERIFIED + ","

                + StaffPermissions.TABLE_NAME + "." + StaffPermissions.DESIGNATION + ","
                + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_CREATE_UPDATE_ITEM_CATEGORIES + ","
                + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_CREATE_UPDATE_ITEMS + ","
                + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_APPROVE_SHOPS + ""
//                + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_TAXI_PROFILE_UPDATE + ""

                + " FROM " + User.TABLE_NAME
                + " LEFT OUTER JOIN " + StaffPermissions.TABLE_NAME + " ON (" + StaffPermissions.TABLE_NAME + "." + StaffPermissions.STAFF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " WHERE TRUE "
                + " AND ( " + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_STAFF_CODE
                + " OR "
                + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_ADMIN_CODE + " ) ";




        if(gender != null)
        {
            queryJoin = queryJoin + " AND " + User.TABLE_NAME + "." + User.GENDER + " = ?";
        }
//


//        if(permitProfileUpdate!=null && permitProfileUpdate)
//        {
////            queryJoin = queryJoin + " AND " + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_TAXI_PROFILE_UPDATE + " = TRUE ";
//        }
//
//
//        if(permitRegistrationAndRenewal !=null && permitRegistrationAndRenewal)
//        {
//            queryJoin = queryJoin + " AND " + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_TAXI_REGISTRATION_AND_RENEWAL + " = TRUE ";
//        }





        // all the non-aggregate columns which are present in select must be present in group by also.
        queryJoin = queryJoin

                + " group by "
                + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMISSION_ID + ","
                + User.TABLE_NAME + "." + User.USER_ID;


        queryCount = queryJoin;



        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;

//				queryNormal = queryNormal + queryPartSortBy;
                queryJoin = queryJoin + queryPartSortBy;
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


//			queryNormal = queryNormal + queryPartLimitOffset;
            queryJoin = queryJoin + queryPartLimitOffset;
        }






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

        PreparedStatement statementCount = null;
        ResultSet resultSetCount = null;

        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata)
            {
                statement = connection.prepareStatement(queryJoin);


                if(gender!=null)
                {
                    statement.setObject(++i,gender);
                }


                rs = statement.executeQuery();

                while(rs.next())
                {

                    User user = new User();

                    user.setUserID(rs.getInt(User.USER_ID));
                    user.setUsername(rs.getString(User.USERNAME));
                    user.setEmail(rs.getString(User.E_MAIL));
                    user.setPhone(rs.getString(User.PHONE));


                    user.setName(rs.getString(User.NAME));
                    user.setGender(rs.getBoolean(User.GENDER));


                    user.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));
                    user.setAccountPrivate(rs.getBoolean(User.IS_ACCOUNT_PRIVATE));
                    user.setAbout(rs.getString(User.ABOUT));

                    user.setTimestampCreated(rs.getTimestamp(User.TIMESTAMP_CREATED));
                    user.setVerified(rs.getBoolean(User.IS_VERIFIED));

                    StaffPermissions permissions = new StaffPermissions();

                    permissions.setDesignation(rs.getString(StaffPermissions.DESIGNATION));
                    permissions.setPermitCreateUpdateItemCat(rs.getBoolean(StaffPermissions.PERMIT_CREATE_UPDATE_ITEM_CATEGORIES));
                    permissions.setPermitCreateUpdateItems(rs.getBoolean(StaffPermissions.PERMIT_CREATE_UPDATE_ITEMS));
                    permissions.setPermitApproveShops(rs.getBoolean(StaffPermissions.PERMIT_APPROVE_SHOPS));

                    user.setRt_staff_permissions(permissions);

                    itemList.add(user);
                }

                endPoint.setResults(itemList);

            }


            if(getRowCount)
            {
                statementCount = connection.prepareStatement(queryCount);

                i = 0;

                if(gender!=null)
                {
                    statementCount.setObject(++i,gender);
                }


                resultSetCount = statementCount.executeQuery();

                while(resultSetCount.next())
                {
                    endPoint.setItemCount(resultSetCount.getInt("item_count"));
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
                if(resultSetCount!=null)
                {resultSetCount.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(statementCount!=null)
                {statementCount.close();}
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






    public UserEndpoint getStaffListPublic(
            Double latPickUp, Double lonPickUp,
            Boolean permitProfileUpdate,
            Boolean permitRegistrationAndRenewal,
            Boolean gender,
            String sortBy,
            Integer limit, Integer offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    ) {


        boolean isfirst = true;

        String queryCount = "";


        String queryJoin = "SELECT DISTINCT "


                + "6371 * acos( cos( radians("
                + latPickUp + ")) * cos( radians(" +  StaffPermissions.LAT_CURRENT +  ") ) * cos(radians(" + StaffPermissions.LON_CURRENT +  ") - radians("
                + lonPickUp + "))"
                + " + sin( radians(" + latPickUp + ")) * sin(radians(" + StaffPermissions.LAT_CURRENT + "))) as distance" + ","


                + User.TABLE_NAME + "." + User.USER_ID + ","
                + User.TABLE_NAME + "." + User.USERNAME + ","
                + User.TABLE_NAME + "." + User.E_MAIL + ","
                + User.TABLE_NAME + "." + User.PHONE + ","

                + User.TABLE_NAME + "." + User.NAME + ","
                + User.TABLE_NAME + "." + User.GENDER + ","

                + User.TABLE_NAME + "." + User.PROFILE_IMAGE_URL + ","
                + User.TABLE_NAME + "." + User.IS_ACCOUNT_PRIVATE + ","
                + User.TABLE_NAME + "." + User.ABOUT + ","

                + User.TABLE_NAME + "." + User.TIMESTAMP_CREATED + ","
                + User.TABLE_NAME + "." + User.IS_VERIFIED + ","

                + StaffPermissions.TABLE_NAME + "." + StaffPermissions.DESIGNATION + ","
                + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_CREATE_UPDATE_ITEM_CATEGORIES + ","
                + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_CREATE_UPDATE_ITEMS + ","
                + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_APPROVE_SHOPS + ""

                + " FROM " + User.TABLE_NAME
                + " LEFT OUTER JOIN " + StaffPermissions.TABLE_NAME + " ON (" + StaffPermissions.TABLE_NAME + "." + StaffPermissions.STAFF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " WHERE TRUE "
                + " AND ( " + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_STAFF_CODE
                + " OR "
                + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_ADMIN_CODE + " ) ";


//        + " AND " + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstantsNBS.ROLE_STAFF_CODE;




        if(gender != null)
        {
            queryJoin = queryJoin + " AND " + User.TABLE_NAME + "." + User.GENDER + " = ? ";
        }
//


//        if(permitProfileUpdate!=null && permitProfileUpdate)
//        {
////            queryJoin = queryJoin + " AND " + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_TAXI_PROFILE_UPDATE + " = TRUE ";
//        }
//
//
//        if(permitRegistrationAndRenewal !=null && permitRegistrationAndRenewal)
//        {
//            queryJoin = queryJoin + " AND " + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_TAXI_REGISTRATION_AND_RENEWAL + " = TRUE ";
//        }




        // all the non-aggregate columns which are present in select must be present in group by also.
        queryJoin = queryJoin

                + " group by "
                + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMISSION_ID + ","
                + User.TABLE_NAME + "." + User.USER_ID;


        queryCount = queryJoin;



        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;

//				queryNormal = queryNormal + queryPartSortBy;
                queryJoin = queryJoin + queryPartSortBy;
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


//			queryNormal = queryNormal + queryPartLimitOffset;
            queryJoin = queryJoin + queryPartLimitOffset;
        }






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

        PreparedStatement statementCount = null;
        ResultSet resultSetCount = null;

        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata)
            {
                statement = connection.prepareStatement(queryJoin);


                if(gender!=null)
                {
                    statement.setObject(++i,gender);
                }


                rs = statement.executeQuery();

                while(rs.next())
                {

                    User user = new User();

                    user.setUserID(rs.getInt(User.USER_ID));
                    user.setUsername(rs.getString(User.USERNAME));
                    user.setEmail(rs.getString(User.E_MAIL));
                    user.setPhone(rs.getString(User.PHONE));


                    user.setName(rs.getString(User.NAME));
                    user.setGender(rs.getBoolean(User.GENDER));


                    user.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));
                    user.setAccountPrivate(rs.getBoolean(User.IS_ACCOUNT_PRIVATE));
                    user.setAbout(rs.getString(User.ABOUT));

                    user.setTimestampCreated(rs.getTimestamp(User.TIMESTAMP_CREATED));
                    user.setVerified(rs.getBoolean(User.IS_VERIFIED));



                    StaffPermissions permissions = new StaffPermissions();

                    permissions.setDesignation(rs.getString(StaffPermissions.DESIGNATION));

                    permissions.setPermitCreateUpdateItemCat(rs.getBoolean(StaffPermissions.PERMIT_CREATE_UPDATE_ITEM_CATEGORIES));
                    permissions.setPermitCreateUpdateItems(rs.getBoolean(StaffPermissions.PERMIT_CREATE_UPDATE_ITEMS));
                    permissions.setPermitApproveShops(rs.getBoolean(StaffPermissions.PERMIT_APPROVE_SHOPS));



                    permissions.setRt_distance(rs.getFloat("distance"));

                    user.setRt_staff_permissions(permissions);

                    itemList.add(user);
                }

                endPoint.setResults(itemList);

            }





            if(getRowCount)
            {
                statementCount = connection.prepareStatement(queryCount);

                i = 0;

                if(gender!=null)
                {
                    statementCount.setObject(++i,gender);
                }


                resultSetCount = statementCount.executeQuery();

                while(resultSetCount.next())
                {
                    endPoint.setItemCount(resultSetCount.getInt("item_count"));
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
                if(resultSetCount!=null)
                {resultSetCount.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(statementCount!=null)
                {statementCount.close();}
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






    public int updateDeliveryGuyBySelf(User user)
    {

        String updateStatement = "UPDATE " + User.TABLE_NAME

                + " SET "

//                + User.USERNAME + "=?,"
//                + User.PASSWORD + "=?,"
//                + User.E_MAIL + "=?,"
//                + User.PHONE + "=?,"
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

//            statement.setString(++i,user.getUsername());
//            statement.setString(++i,user.getPassword());
//            statement.setString(++i,user.getEmail());
//            statement.setString(++i,user.getPhone());

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



    public int updateDeliveryGuyByAdmin(User user)
    {

        String updateStatement = "UPDATE " + User.TABLE_NAME

                + " SET "

                + User.NAME + "=?,"
                + User.GENDER + "=?,"

                + User.PROFILE_IMAGE_URL + "=?,"
                + User.IS_ACCOUNT_PRIVATE + "=?,"
                + User.ABOUT + "=?,"
                + User.IS_VERIFIED + "=?"

                + " WHERE " + User.USER_ID + " = ?";






//		String updatePermissions = "UPDATE " + DeliveryGuyData.TABLE_NAME
//				+ " SET " + DeliveryGuyData.CURRENT_BALANCE + "=?"
//
//				+ " WHERE " + DeliveryGuyData.STAFF_USER_ID + " = ?";




        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statementDeliveryGuyData = null;

        int rowCountUpdated = 0;

        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);



            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setString(++i,user.getName());
            statement.setObject(++i,user.getGender());

            statement.setString(++i,user.getProfileImagePath());
            statement.setObject(++i,user.isAccountPrivate());
            statement.setString(++i,user.getAbout());
            statement.setObject(++i,user.isVerified());

            statement.setObject(++i,user.getUserID());


            rowCountUpdated = statement.executeUpdate();
//			System.out.println("Total rows updated: " + rowCountUpdated);


//			statementDeliveryGuyData = connection.prepareStatement(updatePermissions,PreparedStatement.RETURN_GENERATED_KEYS);
//			i = 0;
//
//			DeliveryGuyData data = user.getRt_delivery_guy_data();
//
//
//			if(data!=null)
//			{
//				statementDeliveryGuyData.setObject(++i,data.getLatCurrent());
//				statementDeliveryGuyData.setObject(++i,data.getLonCurrent());
//				statementDeliveryGuyData.setObject(++i,data.getCurrentBalance());
//
//				statementDeliveryGuyData.setObject(++i,user.getUserID());
//
//				statementDeliveryGuyData.executeUpdate();
//			}






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






    public UserEndpoint getDeliveryGuyForShopAdmin(
            Double latPickUp, Double lonPickUp,
            Boolean gender,
            Integer shopID,
            String sortBy,
            Integer limit, Integer offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    ) {


        boolean isfirst = true;

        String queryCount = "";


        String queryJoin = "SELECT DISTINCT "


                + "6371 * acos( cos( radians("
                + latPickUp + ")) * cos( radians(" +  DeliveryGuyData.LAT_CURRENT +  ") ) * cos(radians(" + DeliveryGuyData.LON_CURRENT +  ") - radians("
                + lonPickUp + "))"
                + " + sin( radians(" + latPickUp + ")) * sin(radians(" + DeliveryGuyData.LAT_CURRENT + "))) as distance" + ","

                + User.TABLE_NAME + "." + User.USER_ID + ","
                + User.TABLE_NAME + "." + User.USERNAME + ","
                + User.TABLE_NAME + "." + User.E_MAIL + ","
                + User.TABLE_NAME + "." + User.PHONE + ","

                + User.TABLE_NAME + "." + User.NAME + ","
                + User.TABLE_NAME + "." + User.GENDER + ","

                + User.TABLE_NAME + "." + User.PROFILE_IMAGE_URL + ","
                + User.TABLE_NAME + "." + User.IS_ACCOUNT_PRIVATE + ","
                + User.TABLE_NAME + "." + User.ABOUT + ","

                + User.TABLE_NAME + "." + User.TIMESTAMP_CREATED + ","
                + User.TABLE_NAME + "." + User.IS_VERIFIED + ","

                + DeliveryGuyData.TABLE_NAME + "." + DeliveryGuyData.LAT_CURRENT + ","
                + DeliveryGuyData.TABLE_NAME + "." + DeliveryGuyData.LON_CURRENT + ","
                + DeliveryGuyData.TABLE_NAME + "." + DeliveryGuyData.IS_EMPLOYED_BY_SHOP + ","
                + DeliveryGuyData.TABLE_NAME + "." + DeliveryGuyData.SHOP_ID + ","
                + DeliveryGuyData.TABLE_NAME + "." + DeliveryGuyData.CURRENT_BALANCE + ""

                + " FROM " + User.TABLE_NAME
                + " INNER JOIN " + DeliveryGuyData.TABLE_NAME + " ON (" + DeliveryGuyData.TABLE_NAME + "." + DeliveryGuyData.STAFF_USER_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " WHERE TRUE "
                + " AND ( " + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_DELIVERY_GUY_SELF_CODE
                + " OR " + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_DELIVERY_GUY_CODE +  " ) ";




        if(shopID!=null)
        {
            queryJoin = queryJoin + " AND " + DeliveryGuyData.TABLE_NAME + "." + DeliveryGuyData.SHOP_ID + " = ? ";
        }


        if(gender != null)
        {
            queryJoin = queryJoin + " AND " + User.TABLE_NAME + "." + User.GENDER + " = ?";
        }
//


//        if(permitProfileUpdate!=null && permitProfileUpdate)
//        {
////            queryJoin = queryJoin + " AND " + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_TAXI_PROFILE_UPDATE + " = TRUE ";
//        }
//
//
//        if(permitRegistrationAndRenewal !=null && permitRegistrationAndRenewal)
//        {
//            queryJoin = queryJoin + " AND " + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_TAXI_REGISTRATION_AND_RENEWAL + " = TRUE ";
//        }





        // all the non-aggregate columns which are present in select must be present in group by also.
        queryJoin = queryJoin

                + " group by "
                + DeliveryGuyData.TABLE_NAME + "." + DeliveryGuyData.DATA_ID + ","
                + User.TABLE_NAME + "." + User.USER_ID;


        queryCount = queryJoin;



        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;

//				queryNormal = queryNormal + queryPartSortBy;
                queryJoin = queryJoin + queryPartSortBy;
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


//			queryNormal = queryNormal + queryPartLimitOffset;
            queryJoin = queryJoin + queryPartLimitOffset;
        }






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

        PreparedStatement statementCount = null;
        ResultSet resultSetCount = null;

        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata)
            {
                statement = connection.prepareStatement(queryJoin);


                if(shopID!=null)
                {
                    statement.setObject(++i,shopID);
                }


                if(gender!=null)
                {
                    statement.setObject(++i,gender);
                }


                rs = statement.executeQuery();

                while(rs.next())
                {

                    User user = new User();

                    user.setUserID(rs.getInt(User.USER_ID));
                    user.setUsername(rs.getString(User.USERNAME));
                    user.setEmail(rs.getString(User.E_MAIL));
                    user.setPhone(rs.getString(User.PHONE));


                    user.setName(rs.getString(User.NAME));
                    user.setGender(rs.getBoolean(User.GENDER));


                    user.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));
                    user.setAccountPrivate(rs.getBoolean(User.IS_ACCOUNT_PRIVATE));
                    user.setAbout(rs.getString(User.ABOUT));

                    user.setTimestampCreated(rs.getTimestamp(User.TIMESTAMP_CREATED));
                    user.setVerified(rs.getBoolean(User.IS_VERIFIED));


                    DeliveryGuyData deliveryGuyData = new DeliveryGuyData();

                    deliveryGuyData.setLatCurrent(rs.getDouble(DeliveryGuyData.LAT_CURRENT));
                    deliveryGuyData.setLonCurrent(rs.getDouble(DeliveryGuyData.LON_CURRENT));
                    deliveryGuyData.setEmployedByShop(rs.getBoolean(DeliveryGuyData.IS_EMPLOYED_BY_SHOP));
                    deliveryGuyData.setShopID(rs.getInt(DeliveryGuyData.SHOP_ID));
                    deliveryGuyData.setCurrentBalance(rs.getDouble(DeliveryGuyData.CURRENT_BALANCE));
//					deliveryGuyData.setStaffUserID(rs.getInt(DeliveryGuyData.STAFF_USER_ID));

                    deliveryGuyData.setRt_distance(rs.getDouble("distance"));





                    user.setRt_delivery_guy_data(deliveryGuyData);

                    itemList.add(user);
                }

                endPoint.setResults(itemList);

            }


            if(getRowCount)
            {
                statementCount = connection.prepareStatement(queryCount);

                i = 0;


                if(shopID!=null)
                {
                    statementCount.setObject(++i,shopID);
                }


                if(gender!=null)
                {
                    statementCount.setObject(++i,gender);
                }


                resultSetCount = statementCount.executeQuery();

                while(resultSetCount.next())
                {
                    endPoint.setItemCount(resultSetCount.getInt("item_count"));
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
                if(resultSetCount!=null)
                {resultSetCount.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(statementCount!=null)
                {statementCount.close();}
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



    public int updateShopStaffProfile(User user)
    {

        String updateStatement = "UPDATE " + User.TABLE_NAME

                + " SET "

//                + User.USERNAME + "=?,"
//                + User.PASSWORD + "=?,"
//                + User.E_MAIL + "=?,"
//                + User.PHONE + "=?,"
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

//            statement.setString(++i,user.getUsername());
//            statement.setString(++i,user.getPassword());
//            statement.setString(++i,user.getEmail());
//            statement.setString(++i,user.getPhone());

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




    public int updateShopStaffByAdmin(User user)
    {

        String updateStatement = "UPDATE " + User.TABLE_NAME

                + " SET "

                + User.NAME + "=?,"
                + User.GENDER + "=?,"

                + User.PROFILE_IMAGE_URL + "=?,"
                + User.IS_ACCOUNT_PRIVATE + "=?,"
                + User.ABOUT + "=?,"
                + User.IS_VERIFIED + "=?"

                + " WHERE " + User.USER_ID + " = ?";




        String updatePermissions = "UPDATE " + ShopStaffPermissions.TABLE_NAME
                + " SET "
                + ShopStaffPermissions.DESIGNATION + "=?,"
                + ShopStaffPermissions.ADD_REMOVE_ITEMS_FROM_SHOP + "=?,"

                + ShopStaffPermissions.UPDATE_STOCK + "=?,"
                + ShopStaffPermissions.CANCEL_ORDERS + "=?,"
                + ShopStaffPermissions.CONFIRM_ORDERS + "=?,"

                + ShopStaffPermissions.SET_ORDERS_PACKED + "=?,"
                + ShopStaffPermissions.HANDOVER_TO_DELIVERY + "=?,"
//                + ShopStaffPermissions.MARK_ORDERS_DELIVERED + "=?,"

                + ShopStaffPermissions.ACCEPT_PAYMENTS_FROM_DELIVERY + "=?,"
                + ShopStaffPermissions.ACCEPT_RETURNS + "=?"

                + " WHERE " + ShopStaffPermissions.STAFF_ID + " = ?";





//		String insertStaffPermissions =
//
//				"INSERT INTO " + ShopStaffPermissions.TABLE_NAME
//						+ "("
//						+ ShopStaffPermissions.STAFF_ID + ","
//						+ ShopStaffPermissions.DESIGNATION + ","
//						+ ShopStaffPermissions.ADD_REMOVE_ITEMS_FROM_SHOP + ","
//
//						+ ShopStaffPermissions.UPDATE_STOCK + ","
//						+ ShopStaffPermissions.CANCEL_ORDERS + ","
//						+ ShopStaffPermissions.CONFIRM_ORDERS + ","
//
//						+ ShopStaffPermissions.SET_ORDERS_PACKED + ","
//						+ ShopStaffPermissions.HANDOVER_TO_DELIVERY + ","
//						+ ShopStaffPermissions.MARK_ORDERS_DELIVERED + ","
//
//						+ ShopStaffPermissions.ACCEPT_PAYMENTS_FROM_DELIVERY + ","
//						+ ShopStaffPermissions.ACCEPT_RETURNS + ""
//						+ ") values(?,?,?, ?,?,?, ?,?,?, ?,?)"
//						+ " ON CONFLICT (" + ShopStaffPermissions.STAFF_ID + ")"
//						+ " DO UPDATE "
//						+ " SET "
//						+ ShopStaffPermissions.DESIGNATION + "= excluded." + ShopStaffPermissions.DESIGNATION + " , "
//						+ ShopStaffPermissions.ADD_REMOVE_ITEMS_FROM_SHOP + "= excluded." + ShopStaffPermissions.ADD_REMOVE_ITEMS_FROM_SHOP + " , "
//						+ ShopStaffPermissions.UPDATE_STOCK + "= excluded." + ShopStaffPermissions.UPDATE_STOCK  + ","
//						+ ShopStaffPermissions.CANCEL_ORDERS + "= excluded." + ShopStaffPermissions.CANCEL_ORDERS  + ","
//						+ ShopStaffPermissions.CONFIRM_ORDERS + "= excluded." + ShopStaffPermissions.CONFIRM_ORDERS  + ","
//						+ ShopStaffPermissions.SET_ORDERS_PACKED + "= excluded." + ShopStaffPermissions.SET_ORDERS_PACKED  + ","
//						+ ShopStaffPermissions.HANDOVER_TO_DELIVERY + "= excluded." + ShopStaffPermissions.HANDOVER_TO_DELIVERY  + ","
//						+ ShopStaffPermissions.MARK_ORDERS_DELIVERED + "= excluded." + ShopStaffPermissions.MARK_ORDERS_DELIVERED  + ","
//						+ ShopStaffPermissions.ACCEPT_PAYMENTS_FROM_DELIVERY + "= excluded." + ShopStaffPermissions.ACCEPT_PAYMENTS_FROM_DELIVERY  + ","
//						+ ShopStaffPermissions.ACCEPT_RETURNS + "= excluded." + ShopStaffPermissions.ACCEPT_RETURNS;
//





        Connection connection = null;
        PreparedStatement statement = null;
        PreparedStatement statementUpdatePermissions = null;

        int rowCountUpdated = 0;

        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);



            statement = connection.prepareStatement(updateStatement);

            int i = 0;

            statement.setString(++i,user.getName());
            statement.setObject(++i,user.getGender());

            statement.setString(++i,user.getProfileImagePath());
            statement.setObject(++i,user.isAccountPrivate());
            statement.setString(++i,user.getAbout());
            statement.setObject(++i,user.isVerified());

            statement.setObject(++i,user.getUserID());


            rowCountUpdated = statement.executeUpdate();
            System.out.println("Total rows updated: " + rowCountUpdated);


            statementUpdatePermissions = connection.prepareStatement(updatePermissions,PreparedStatement.RETURN_GENERATED_KEYS);
            i = 0;

            ShopStaffPermissions permissions = user.getRt_shop_staff_permissions();


            if(permissions!=null)
            {


                statementUpdatePermissions.setString(++i,permissions.getDesignation());
                statementUpdatePermissions.setObject(++i,permissions.isPermitAddRemoveItems());

                statementUpdatePermissions.setObject(++i,permissions.isPermitUpdateItemsInShop());
                statementUpdatePermissions.setObject(++i,permissions.isPermitCancelOrders());
                statementUpdatePermissions.setObject(++i,permissions.isPermitConfirmOrders());

                statementUpdatePermissions.setObject(++i,permissions.isPermitSetOrdersPacked());
                statementUpdatePermissions.setObject(++i,permissions.isPermitHandoverToDelivery());
//                statementUpdatePermissions.setObject(++i,permissions.isPermitMarkOrdersDelivered());

                statementUpdatePermissions.setObject(++i,permissions.isPermitAcceptPaymentsFromDelivery());
                statementUpdatePermissions.setObject(++i,permissions.isPermitAcceptReturns());

                statementUpdatePermissions.setObject(++i,user.getUserID());

                statementUpdatePermissions.executeUpdate();
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



    public UserEndpoint getShopStaffForShopAdmin(
            Double latPickUp, Double lonPickUp,
            Boolean permitCreateUpdateItemCat,
            Boolean permitCreateUpdateItems,
            Boolean gender,
            int shopID,
            String sortBy,
            Integer limit, Integer offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    ) {


        boolean isfirst = true;

        String queryCount = "";


        String queryJoin = "SELECT DISTINCT "


                + "6371 * acos( cos( radians("
                + latPickUp + ")) * cos( radians(" +  StaffPermissions.LAT_CURRENT +  ") ) * cos(radians(" + StaffPermissions.LON_CURRENT +  ") - radians("
                + lonPickUp + "))"
                + " + sin( radians(" + latPickUp + ")) * sin(radians(" + StaffPermissions.LAT_CURRENT + "))) as distance" + ","

                + User.TABLE_NAME + "." + User.USER_ID + ","
                + User.TABLE_NAME + "." + User.USERNAME + ","
                + User.TABLE_NAME + "." + User.E_MAIL + ","
                + User.TABLE_NAME + "." + User.PHONE + ","

                + User.TABLE_NAME + "." + User.NAME + ","
                + User.TABLE_NAME + "." + User.GENDER + ","

                + User.TABLE_NAME + "." + User.PROFILE_IMAGE_URL + ","
                + User.TABLE_NAME + "." + User.IS_ACCOUNT_PRIVATE + ","
                + User.TABLE_NAME + "." + User.ABOUT + ","

                + User.TABLE_NAME + "." + User.TIMESTAMP_CREATED + ","
                + User.TABLE_NAME + "." + User.IS_VERIFIED + ","

                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.DESIGNATION + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.ADD_REMOVE_ITEMS_FROM_SHOP + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.UPDATE_STOCK + ","

                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.CANCEL_ORDERS + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.CONFIRM_ORDERS + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.SET_ORDERS_PACKED + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.HANDOVER_TO_DELIVERY + ","
//                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.MARK_ORDERS_DELIVERED + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.ACCEPT_PAYMENTS_FROM_DELIVERY + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.ACCEPT_RETURNS + ""

                + " FROM " + User.TABLE_NAME
                + " LEFT OUTER JOIN " + ShopStaffPermissions.TABLE_NAME + " ON (" + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.STAFF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " WHERE " + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.SHOP_ID + " = ? "
                + " AND ( " + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_SHOP_STAFF_CODE
                + " OR " + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_SHOP_ADMIN_CODE + " ) ";



        if(gender != null)
        {
            queryJoin = queryJoin + " AND " + User.TABLE_NAME + "." + User.GENDER + " = ?";
        }
//


//        if(permitProfileUpdate!=null && permitProfileUpdate)
//        {
////            queryJoin = queryJoin + " AND " + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_TAXI_PROFILE_UPDATE + " = TRUE ";
//        }
//
//
//        if(permitRegistrationAndRenewal !=null && permitRegistrationAndRenewal)
//        {
//            queryJoin = queryJoin + " AND " + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_TAXI_REGISTRATION_AND_RENEWAL + " = TRUE ";
//        }





        // all the non-aggregate columns which are present in select must be present in group by also.
        queryJoin = queryJoin

                + " group by "
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.PERMISSION_ID + ","
                + User.TABLE_NAME + "." + User.USER_ID;


        queryCount = queryJoin;



        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;

//				queryNormal = queryNormal + queryPartSortBy;
                queryJoin = queryJoin + queryPartSortBy;
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


//			queryNormal = queryNormal + queryPartLimitOffset;
            queryJoin = queryJoin + queryPartLimitOffset;
        }






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

        PreparedStatement statementCount = null;
        ResultSet resultSetCount = null;

        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata)
            {
                statement = connection.prepareStatement(queryJoin);


                statement.setObject(++i,shopID);


                if(gender!=null)
                {
                    statement.setObject(++i,gender);
                }


                rs = statement.executeQuery();

                while(rs.next())
                {

                    User user = new User();

                    user.setUserID(rs.getInt(User.USER_ID));
                    user.setUsername(rs.getString(User.USERNAME));
                    user.setEmail(rs.getString(User.E_MAIL));
                    user.setPhone(rs.getString(User.PHONE));


                    user.setName(rs.getString(User.NAME));
                    user.setGender(rs.getBoolean(User.GENDER));


                    user.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));
                    user.setAccountPrivate(rs.getBoolean(User.IS_ACCOUNT_PRIVATE));
                    user.setAbout(rs.getString(User.ABOUT));

                    user.setTimestampCreated(rs.getTimestamp(User.TIMESTAMP_CREATED));
                    user.setVerified(rs.getBoolean(User.IS_VERIFIED));

                    ShopStaffPermissions permissions = new ShopStaffPermissions();

                    permissions.setDesignation(rs.getString(StaffPermissions.DESIGNATION));
                    permissions.setPermitAddRemoveItems(rs.getBoolean(ShopStaffPermissions.ADD_REMOVE_ITEMS_FROM_SHOP));
                    permissions.setPermitUpdateItemsInShop(rs.getBoolean(ShopStaffPermissions.UPDATE_STOCK));

                    permissions.setPermitCancelOrders(rs.getBoolean(ShopStaffPermissions.CANCEL_ORDERS));
                    permissions.setPermitConfirmOrders(rs.getBoolean(ShopStaffPermissions.CONFIRM_ORDERS));
                    permissions.setPermitSetOrdersPacked(rs.getBoolean(ShopStaffPermissions.SET_ORDERS_PACKED));
                    permissions.setPermitHandoverToDelivery(rs.getBoolean(ShopStaffPermissions.HANDOVER_TO_DELIVERY));
//                    permissions.setPermitMarkOrdersDelivered(rs.getBoolean(ShopStaffPermissions.MARK_ORDERS_DELIVERED));
                    permissions.setPermitAcceptPaymentsFromDelivery(rs.getBoolean(ShopStaffPermissions.ACCEPT_PAYMENTS_FROM_DELIVERY));
                    permissions.setPermitAcceptReturns(rs.getBoolean(ShopStaffPermissions.ACCEPT_RETURNS));

                    user.setRt_shop_staff_permissions(permissions);

                    itemList.add(user);
                }

                endPoint.setResults(itemList);

            }


            if(getRowCount)
            {
                statementCount = connection.prepareStatement(queryCount);

                i = 0;

                statementCount.setObject(++i,shopID);


                if(gender!=null)
                {
                    statementCount.setObject(++i,gender);
                }


                resultSetCount = statementCount.executeQuery();

                while(resultSetCount.next())
                {
                    endPoint.setItemCount(resultSetCount.getInt("item_count"));
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
                if(resultSetCount!=null)
                {resultSetCount.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(statementCount!=null)
                {statementCount.close();}
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




    public UserEndpoint getShopStaffListPublic(
            Double latPickUp, Double lonPickUp,
            Boolean permitProfileUpdate,
            Boolean permitRegistrationAndRenewal,
            Boolean gender,
            String sortBy,
            Integer limit, Integer offset,
            boolean getRowCount,
            boolean getOnlyMetadata
    ) {


        boolean isfirst = true;

        String queryCount = "";


        String queryJoin = "SELECT DISTINCT "


                + "6371 * acos( cos( radians("
                + latPickUp + ")) * cos( radians(" +  StaffPermissions.LAT_CURRENT +  ") ) * cos(radians(" + StaffPermissions.LON_CURRENT +  ") - radians("
                + lonPickUp + "))"
                + " + sin( radians(" + latPickUp + ")) * sin(radians(" + StaffPermissions.LAT_CURRENT + "))) as distance" + ","


                + User.TABLE_NAME + "." + User.USER_ID + ","
                + User.TABLE_NAME + "." + User.USERNAME + ","
                + User.TABLE_NAME + "." + User.E_MAIL + ","
                + User.TABLE_NAME + "." + User.PHONE + ","

                + User.TABLE_NAME + "." + User.NAME + ","
                + User.TABLE_NAME + "." + User.GENDER + ","

                + User.TABLE_NAME + "." + User.PROFILE_IMAGE_URL + ","
                + User.TABLE_NAME + "." + User.IS_ACCOUNT_PRIVATE + ","
                + User.TABLE_NAME + "." + User.ABOUT + ","

                + User.TABLE_NAME + "." + User.TIMESTAMP_CREATED + ","
                + User.TABLE_NAME + "." + User.IS_VERIFIED + ","

                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.DESIGNATION + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.ADD_REMOVE_ITEMS_FROM_SHOP + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.UPDATE_STOCK + ","

                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.CANCEL_ORDERS + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.CONFIRM_ORDERS + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.SET_ORDERS_PACKED + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.HANDOVER_TO_DELIVERY + ","
//                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.MARK_ORDERS_DELIVERED + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.ACCEPT_PAYMENTS_FROM_DELIVERY + ","
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.ACCEPT_RETURNS + ""

                + " FROM " + User.TABLE_NAME
                + " LEFT OUTER JOIN " + StaffPermissions.TABLE_NAME + " ON (" + StaffPermissions.TABLE_NAME + "." + StaffPermissions.STAFF_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
                + " WHERE TRUE "
                + " AND ( " + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_SHOP_STAFF_CODE
                + " OR "
                + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_SHOP_ADMIN_CODE + " ) ";


//        + " AND " + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstantsNBS.ROLE_STAFF_CODE;




        if(gender != null)
        {
            queryJoin = queryJoin + " AND " + User.TABLE_NAME + "." + User.GENDER + " = ? ";
        }
//


//        if(permitProfileUpdate!=null && permitProfileUpdate)
//        {
////            queryJoin = queryJoin + " AND " + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_TAXI_PROFILE_UPDATE + " = TRUE ";
//        }
//
//
//        if(permitRegistrationAndRenewal !=null && permitRegistrationAndRenewal)
//        {
//            queryJoin = queryJoin + " AND " + StaffPermissions.TABLE_NAME + "." + StaffPermissions.PERMIT_TAXI_REGISTRATION_AND_RENEWAL + " = TRUE ";
//        }




        // all the non-aggregate columns which are present in select must be present in group by also.
        queryJoin = queryJoin

                + " group by "
                + ShopStaffPermissions.TABLE_NAME + "." + ShopStaffPermissions.PERMISSION_ID + ","
                + User.TABLE_NAME + "." + User.USER_ID;


        queryCount = queryJoin;



        if(sortBy!=null)
        {
            if(!sortBy.equals(""))
            {
                String queryPartSortBy = " ORDER BY " + sortBy;

//				queryNormal = queryNormal + queryPartSortBy;
                queryJoin = queryJoin + queryPartSortBy;
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


//			queryNormal = queryNormal + queryPartLimitOffset;
            queryJoin = queryJoin + queryPartLimitOffset;
        }






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

        PreparedStatement statementCount = null;
        ResultSet resultSetCount = null;

        try {

            connection = dataSource.getConnection();

            int i = 0;


            if(!getOnlyMetadata)
            {
                statement = connection.prepareStatement(queryJoin);


                if(gender!=null)
                {
                    statement.setObject(++i,gender);
                }


                rs = statement.executeQuery();

                while(rs.next())
                {

                    User user = new User();

                    user.setUserID(rs.getInt(User.USER_ID));
                    user.setUsername(rs.getString(User.USERNAME));
                    user.setEmail(rs.getString(User.E_MAIL));
                    user.setPhone(rs.getString(User.PHONE));


                    user.setName(rs.getString(User.NAME));
                    user.setGender(rs.getBoolean(User.GENDER));


                    user.setProfileImagePath(rs.getString(User.PROFILE_IMAGE_URL));
                    user.setAccountPrivate(rs.getBoolean(User.IS_ACCOUNT_PRIVATE));
                    user.setAbout(rs.getString(User.ABOUT));

                    user.setTimestampCreated(rs.getTimestamp(User.TIMESTAMP_CREATED));
                    user.setVerified(rs.getBoolean(User.IS_VERIFIED));



                    ShopStaffPermissions permissions = new ShopStaffPermissions();

                    permissions.setDesignation(rs.getString(StaffPermissions.DESIGNATION));

                    permissions.setPermitAddRemoveItems(rs.getBoolean(ShopStaffPermissions.ADD_REMOVE_ITEMS_FROM_SHOP));
                    permissions.setPermitUpdateItemsInShop(rs.getBoolean(ShopStaffPermissions.UPDATE_STOCK));

                    permissions.setPermitCancelOrders(rs.getBoolean(ShopStaffPermissions.CANCEL_ORDERS));
                    permissions.setPermitConfirmOrders(rs.getBoolean(ShopStaffPermissions.CONFIRM_ORDERS));
                    permissions.setPermitSetOrdersPacked(rs.getBoolean(ShopStaffPermissions.SET_ORDERS_PACKED));
                    permissions.setPermitHandoverToDelivery(rs.getBoolean(ShopStaffPermissions.HANDOVER_TO_DELIVERY));
//                    permissions.setPermitMarkOrdersDelivered(rs.getBoolean(ShopStaffPermissions.MARK_ORDERS_DELIVERED));
                    permissions.setPermitAcceptPaymentsFromDelivery(rs.getBoolean(ShopStaffPermissions.ACCEPT_PAYMENTS_FROM_DELIVERY));
                    permissions.setPermitAcceptReturns(rs.getBoolean(ShopStaffPermissions.ACCEPT_RETURNS));



                    permissions.setRt_distance(rs.getFloat("distance"));

                    user.setRt_shop_staff_permissions(permissions);

                    itemList.add(user);
                }

                endPoint.setResults(itemList);

            }





            if(getRowCount)
            {
                statementCount = connection.prepareStatement(queryCount);

                i = 0;

                if(gender!=null)
                {
                    statementCount.setObject(++i,gender);
                }


                resultSetCount = statementCount.executeQuery();

                while(resultSetCount.next())
                {
                    endPoint.setItemCount(resultSetCount.getInt("item_count"));
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
                if(resultSetCount!=null)
                {resultSetCount.close();}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {

                if(statementCount!=null)
                {statementCount.close();}
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
