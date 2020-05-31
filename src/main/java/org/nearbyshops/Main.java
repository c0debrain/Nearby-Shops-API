package org.nearbyshops;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.nearbyshops.Globals.GlobalConfig;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.*;
import org.nearbyshops.Model.ModelAnalytics.ItemAnalytics;
import org.nearbyshops.Model.ModelAnalytics.ShopAnalytics;
import org.nearbyshops.Model.ModelBilling.Transaction;
import org.nearbyshops.Model.ModelDelivery.DeliveryAddress;
import org.nearbyshops.Model.ModelItemSpecification.ItemSpecificationItem;
import org.nearbyshops.Model.ModelItemSpecification.ItemSpecificationName;
import org.nearbyshops.Model.ModelItemSpecification.ItemSpecificationValue;
import org.nearbyshops.Model.ModelOneSignal.OneSignalIDs;
import org.nearbyshops.Model.ModelReviewItem.FavouriteItem;
import org.nearbyshops.Model.ModelReviewItem.ItemReview;
import org.nearbyshops.Model.ModelReviewItem.ItemReviewThanks;
import org.nearbyshops.Model.ModelReviewShop.FavouriteShop;
import org.nearbyshops.Model.ModelReviewShop.ShopReview;
import org.nearbyshops.Model.ModelReviewShop.ShopReviewThanks;
import org.nearbyshops.Model.ModelRoles.*;
import org.nearbyshops.Model.ModelSettings.ServiceConfigurationLocal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;




public class Main {





    public static void startJettyServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in org.taxireferral.api package
        final ResourceConfig rc = new ResourceConfig().packages("org.nearbyshops");


        System.out.println("Base URL : " + GlobalConstants.BASE_URI);
        JettyHttpContainerFactory.createServer(URI.create(GlobalConstants.BASE_URI),rc);
    }




    public static void main(String[] args) throws IOException {



        GlobalConfig.loadGlobalConfiguration();

//        createDB();
        upgradeTables();


        createTables();
        startJettyServer();


        setupPing();


        setupFirebaseAdminSDK();
    }




    

    private static void createTables()
    {

        Connection connection = null;
        Statement statement = null;



        try {

            connection = DriverManager.getConnection(GlobalConstants.POSTGRES_CONNECTION_URL,
                    GlobalConstants.POSTGRES_USERNAME, GlobalConstants.POSTGRES_PASSWORD);


            statement = connection.createStatement();

            statement.executeUpdate(User.createTable);
            statement.executeUpdate(UserTokens.createTable);
            statement.executeUpdate(UserMarkets.createTable);
            statement.executeUpdate(OneSignalIDs.createTable);
            statement.executeUpdate(StaffPermissions.createTablePostgres);
            statement.executeUpdate(EmailVerificationCode.createTablePostgres);
            statement.executeUpdate(PhoneVerificationCode.createTablePostgres);

            statement.executeUpdate(Transaction.createTablePostgres);


            statement.executeUpdate(ItemCategory.createTableItemCategoryPostgres);
            statement.executeUpdate(Item.createTableItemPostgres);
            statement.executeUpdate(Shop.createTableShopPostgres);
            statement.executeUpdate(ShopItem.createTableShopItemPostgres);

            statement.executeUpdate(ShopImage.createTablePostgres);

            statement.executeUpdate(ShopStaffPermissions.createTablePostgres);
            statement.executeUpdate(DeliveryGuyData.createTablePostgres);

            statement.executeUpdate(Cart.createTableCartPostgres);
            statement.executeUpdate(CartItem.createtableCartItemPostgres);
            statement.executeUpdate(DeliveryAddress.createTableDeliveryAddressPostgres);

            statement.executeUpdate(Order.createTableOrderPostgres);
            statement.executeUpdate(OrderItem.createtableOrderItemPostgres);



            // tables for shop reviews
            statement.executeUpdate(ShopReview.createTableShopReviewPostgres);
            statement.executeUpdate(FavouriteShop.createTableFavouriteBookPostgres);
            statement.executeUpdate(ShopReviewThanks.createTableShopReviewThanksPostgres);

            // tables for Item reviews
            statement.executeUpdate(ItemReview.createTableItemReviewPostgres);
            statement.executeUpdate(FavouriteItem.createTableFavouriteItemPostgres);
            statement.executeUpdate(ItemReviewThanks.createTableItemReviewThanksPostgres);



            statement.executeUpdate(ItemImage.createTableItemImagesPostgres);


            statement.executeUpdate(ItemSpecificationName.createTableItemSpecNamePostgres);
            statement.executeUpdate(ItemSpecificationValue.createTableItemSpecificationValuePostgres);
            statement.executeUpdate(ItemSpecificationItem.createTableItemSpecificationItemPostgres);


            statement.executeUpdate(ServiceConfigurationLocal.createTablePostgres);



            // tables for storing analytics data
            statement.executeUpdate(ItemAnalytics.createTable);
            statement.executeUpdate(ShopAnalytics.createTable);





//            statement.executeUpdate(User.createTable);
//            statement.executeUpdate(StaffPermissions.createTablePostgres);



            System.out.println("Tables Created ... !");


            // developers Note: whenever adding a table please check that tables it depends on are created first


            // Create admin account with given username and password if it does not exit | or update in case admin account exist

            User admin = new User();
            admin.setEmail(GlobalConstants.ADMIN_EMAIL);
            admin.setRole(1);
            admin.setPassword(GlobalConstants.ADMIN_PASSWORD);


            System.out.println("Admin Username : " + GlobalConstants.ADMIN_EMAIL + " | " + " Admin Password : " + GlobalConstants.ADMIN_PASSWORD);




//            boolean adminRoleExist = Globals.daoUserUtility.checkRoleExists(GlobalConstants.ROLE_ADMIN_CODE);
            int userID = Globals.daoUserUtility.getUserID(GlobalConstants.ADMIN_EMAIL);

            if(userID==-1)
            {
                // user does not exist
                Globals.daoUserUtility.createAdminUsingEmail(admin,true);
            }
            else
            {
                // user exists so upgrade the user role to admin
                Globals.daoUserUtility.updateUserRole(userID);
            }




//            User admin = new User();
//            admin.setUsername("admin");
//            admin.setRole(1);
//            admin.setPassword("password");
//
//
//
//            try
//            {
//                int rowCount = Globals.daoUserSignUp.registerUsingUsername(admin,true);
//
//                if(rowCount==1)
//                {
//                    System.out.println("Admin Account created !");
//                }
//            }
//            catch (Exception ex)
//            {
//                System.out.println(ex.toString());
//            }




            // Insert the root category whose ID is 1

            String insertItemCategory = "";

            // The root ItemCategory has id 1. If the root category does not exist then insert it.
            if(Globals.itemCategoryDAO.checkRoot(1) == null)
            {

                insertItemCategory = "INSERT INTO "
                        + ItemCategory.TABLE_NAME
                        + "("
                        + ItemCategory.ITEM_CATEGORY_ID + ","
                        + ItemCategory.ITEM_CATEGORY_NAME + ","
                        + ItemCategory.PARENT_CATEGORY_ID + ","
                        + ItemCategory.ITEM_CATEGORY_DESCRIPTION + ","
                        + ItemCategory.IMAGE_PATH + ","
                        + ItemCategory.IS_LEAF_NODE + ") VALUES("
                        + "" + "1"	+ ","
                        + "'" + "ROOT"	+ "',"
                        + "" + "NULL" + ","
                        + "'" + "This is the root Category. Do not modify it." + "',"
                        + "'" + " " + "',"
                        + "'" + "FALSE" + "'"
                        + ")";



                statement.executeUpdate(insertItemCategory);

            }






            // Insert Default Service Configuration

            String insertServiceConfig = "";

            if(Globals.serviceConfigDAO.getServiceConfiguration(null,null)==null)
            {

                ServiceConfigurationLocal defaultConfiguration = new ServiceConfigurationLocal();

//                defaultConfiguration.setServiceLevel(GlobalConstants.SERVICE_LEVEL_CITY);
//                defaultConfiguration.setServiceType(GlobalConstants.SERVICE_TYPE_NONPROFIT);
                defaultConfiguration.setServiceID(1);
                defaultConfiguration.setServiceName("DEFAULT_CONFIGURATION");
                defaultConfiguration.setISOCountryCode("IN");
                defaultConfiguration.setCountry("India");
                defaultConfiguration.setServiceRange(30000);



                Globals.serviceConfigDAO.saveService(defaultConfiguration);

/*
				insertServiceConfig = "INSERT INTO "
						+ ServiceConfigurationLocal.TABLE_NAME
						+ "("
						+ ServiceConfigurationLocal.SERVICE_CONFIGURATION_ID + ","
						+ ServiceConfigurationLocal.SERVICE_NAME + ") VALUES ("
						+ "" + "1" + ","
						+ "'" + "ROOT_CONFIGURATION" + "')";


				stmt.executeUpdate(insertServiceConfig);*/
            }





            // create directory images

            final java.nio.file.Path BASE_DIR = Paths.get("./data/images");

            File theDir = new File(BASE_DIR.toString());

            // if the directory does not exist, create it
            if (!theDir.exists()) {

                System.out.println("Creating directory: " + BASE_DIR.toString());

                boolean result = false;

                try{
                    theDir.mkdir();
                    result = true;
                }
                catch(Exception se){
                    //handle it
                }
                if(result) {
                    System.out.println("DIR created");
                }
            }





        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        finally{


            // close the connection and statement accountApproved

            if(statement !=null)
            {

                try {
                    statement.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }


            if(connection!=null)
            {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }



    private static void upgradeTables()
    {

        Connection connection = null;
        Statement statement = null;


//        Configuration configuration = GlobalConfig.getConfiguration();
//
//
//        if(configuration==null)
//        {
//            System.out.println("Configuration is null : Upgrade Tables !");
//
//            return;
//        }
//
//
//        String connection_url = configuration.getString(ConfigurationKeys.CONNECTION_URL);
//        String username = configuration.getString(ConfigurationKeys.POSTGRES_USERNAME);
//        String password = configuration.getString(ConfigurationKeys.POSTGRES_PASSWORD);


        try {

//            connection = DriverManager.getConnection(connection_url, username,password);

            connection = DriverManager.getConnection(GlobalConstants.POSTGRES_CONNECTION_URL,
                    GlobalConstants.POSTGRES_USERNAME, GlobalConstants.POSTGRES_PASSWORD);


            statement = connection.createStatement();


            statement.executeUpdate(User.upgradeTableSchema);
            statement.executeUpdate(User.removeNotNullforPassword);
            statement.executeUpdate(ShopItem.addColumns);


//            statement.executeUpdate(Shop.removeNotNull);





            System.out.println("Tables Upgrade Complete ... !");


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        finally{


            // close the connection and statement accountApproved

            if(statement !=null)
            {

                try {
                    statement.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }


            if(connection!=null)
            {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }



    private static void setupFirebaseAdminSDK() {

        FileInputStream serviceAccount = null;


        try {


//            "/media/sumeet/data/aNearbyShops/NearbyShopsAPI/firebase/nearbyshops-f7b77-firebase-adminsdk-phmoy-50db87dde4.json"
//            "https://nearbyshops-f7b77.firebaseio.com"

            serviceAccount = new FileInputStream(GlobalConstants.fcm_config_file_path);


            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(GlobalConstants.fcm_database_url)
                    .build();


            FirebaseApp.initializeApp(options);





//
////            GlobalConstants.market_id_for_fcm +
//                    // The topic name can be optionally prefixed with "/topics/".
//            String topic = "end_user_4";
//
//            System.out.println(topic);
//
//            // See documentation on defining a message payload.
//            Message message = Message.builder()
//                    .setNotification(new Notification("Initialization","Notifications are initialized"))
//                    .setTopic(topic)
//                    .build();
//
//
////             Send a message to the devices subscribed to the provided topic.
//            String response = FirebaseMessaging.getInstance().send(message);
//
////             Response is a message ID string.
//            System.out.println("Successfully sent message: " + response);




        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }



    }





    private static void setupPing()
    {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {


                for(String url : GlobalConstants.trusted_market_aggregators_value)
                {
                    // for each url send a ping
                    sendPing(url);
                }

            }
        };




        Timer timer = new Timer();

        // send ping to the sds server at every 3 hours
        timer.scheduleAtFixedRate(timerTask,0,3*60*60*1000);
    }


    private static final OkHttpClient client = new OkHttpClient();


    static void sendPing(String sdsURL)
    {

//        String credentials = Credentials.basic(username, password);


        String url = "";
        url = sdsURL + "/api/v1/ServiceConfiguration/Ping?ServiceURL=" + GlobalConstants.domain_name_for_api;


//        System.out.println("Ping URL" + url);


        Request request = new Request.Builder()
                .url(url)
                .build();



        try (okhttp3.Response response = client.newCall(request).execute()) {


//            if (!response.isSuccessful())
//            {
//            }

//            Headers responseHeaders = response.headers();
//            for (int i = 0; i < responseHeaders.size(); i++) {
//                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//            }



            System.out.println("Ping Response Code : " + response.code());


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

