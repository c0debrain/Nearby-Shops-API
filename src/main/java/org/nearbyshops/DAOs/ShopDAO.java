package org.nearbyshops.DAOs;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Item;
import org.nearbyshops.Model.ItemCategory;
import org.nearbyshops.Model.Shop;
import org.nearbyshops.Model.ShopItem;
import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
import org.nearbyshops.Model.ModelReviewShop.ShopReview;
import org.nearbyshops.Model.ModelRoles.User;

import java.sql.*;
import java.util.ArrayList;

public class ShopDAO {


	private HikariDataSource dataSource = Globals.getDataSource();



	public int insertShop(Shop shop, int shopAdminID)
	{
		
		Connection connection = null;
		PreparedStatement statement = null;
		int rowIdOfInsertedRow = -1;
		int rowCount = -1;


		// add joining credit to the users account
		String updateRole =  " UPDATE " + User.TABLE_NAME
				+ " SET " + User.ROLE + " = " + GlobalConstants.ROLE_SHOP_ADMIN_CODE
				+ " WHERE " + User.TABLE_NAME + "." + User.USER_ID + " = ? "
				+ " AND " + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_END_USER_CODE ;





		String insertShop = "INSERT INTO "
				+ Shop.TABLE_NAME
				+ "("  
				+ Shop.SHOP_NAME + ","
				+ Shop.DELIVERY_RANGE + ","

				+ Shop.LAT_CENTER + ","
				+ Shop.LON_CENTER + ","

				+ Shop.DELIVERY_CHARGES + ","
				+ Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + ","
				+ Shop.PICK_FROM_SHOP_AVAILABLE + ","
				+ Shop.HOME_DELIVERY_AVAILABLE + ","

				+ Shop.SHOP_ENABLED + ","
				+ Shop.SHOP_WAITLISTED + ","

				+ Shop.LOGO_IMAGE_PATH + ","

				+ Shop.SHOP_ADDRESS + ","
				+ Shop.CITY + ","
				+ Shop.PINCODE + ","
				+ Shop.LANDMARK + ","

				+ Shop.CUSTOMER_HELPLINE_NUMBER + ","
				+ Shop.DELIVERY_HELPLINE_NUMBER + ","

				+ Shop.SHORT_DESCRIPTION + ","
				+ Shop.LONG_DESCRIPTION + ","

//				+ Shop.TIMESTAMP_CREATED + ","
				+ Shop.IS_OPEN + ","
				+ Shop.SHOP_ADMIN_ID + ""

				+ " ) VALUES (?,? ,?,? ,?,?,?,? ,?,?, ?, ?,?,?,? ,?,? ,?,? ,?,?)";
		
		try {
			
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			int i = 0;


			statement = connection.prepareStatement(updateRole);

			statement.setObject(++i,shopAdminID);
			rowCount = statement.executeUpdate();


			if(rowCount==1)
			{
				statement = connection.prepareStatement(insertShop,PreparedStatement.RETURN_GENERATED_KEYS);
				i = 0;


				statement.setObject(++i,shop.getShopName());

				statement.setObject(++i,shop.getDeliveryRange());
				statement.setObject(++i,shop.getLatCenter());
				statement.setObject(++i,shop.getLonCenter());

				statement.setObject(++i,shop.getDeliveryCharges());
				statement.setObject(++i,shop.getBillAmountForFreeDelivery());
				statement.setObject(++i,shop.getPickFromShopAvailable());
				statement.setObject(++i,shop.getHomeDeliveryAvailable());

				statement.setObject(++i,null);
				statement.setObject(++i,false);

				statement.setString(++i,shop.getLogoImagePath());

				statement.setString(++i,shop.getShopAddress());
				statement.setString(++i,shop.getCity());
				statement.setObject(++i,shop.getPincode());
				statement.setObject(++i,shop.getLandmark());

				statement.setObject(++i,shop.getCustomerHelplineNumber());
				statement.setObject(++i,shop.getDeliveryHelplineNumber());

				statement.setObject(++i,shop.getShortDescription());
				statement.setObject(++i,shop.getLongDescription());


				statement.setObject(++i,shop.isOpen());
				statement.setObject(++i,shopAdminID);


				rowIdOfInsertedRow = statement.executeUpdate();

				ResultSet rs = statement.getGeneratedKeys();

				if(rs.next())
				{
					rowIdOfInsertedRow = rs.getInt(1);
				}


			}


			connection.commit();


		} catch (SQLException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

			if (connection != null) {
				try {


					rowCount = 0;
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

		
		return rowIdOfInsertedRow;
	}


	public int deleteShop(int shopID)
	{

		int shopAdminID = Globals.daoUserUtility.getUserIDforShopAdmin(shopID);



		String deleteStatement = "DELETE FROM " + Shop.TABLE_NAME
				+ " WHERE " + Shop.SHOP_ID + "= ?";


		// shop is deleted therefore change users role from shop admin to end user
		String updateRole =  " UPDATE " + User.TABLE_NAME
				+ " SET " + User.ROLE + " = " + GlobalConstants.ROLE_END_USER_CODE
				+ " WHERE " + User.TABLE_NAME + "." + User.USER_ID + " = ? "
				+ " AND " + User.TABLE_NAME + "." + User.ROLE + " = " + GlobalConstants.ROLE_SHOP_ADMIN_CODE;




		Connection connection= null;
		PreparedStatement statement = null;
		int rowCountDeleted = 0;
		try {

			connection = dataSource.getConnection();

			statement = connection.prepareStatement(deleteStatement);
			statement.setObject(1,shopID);

			rowCountDeleted = statement.executeUpdate();


			if(rowCountDeleted==1)
			{
				statement = connection.prepareStatement(updateRole);

				statement.setObject(1,shopAdminID);
				statement.executeUpdate();
			}


			connection.close();

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


		return rowCountDeleted;
	}



	public int updateShop(Shop shop)
	{
		
		
		String updateStatement = "UPDATE " + Shop.TABLE_NAME
				+ " SET "

				+ Shop.SHOP_NAME + " = ?,"

				+ Shop.DELIVERY_RANGE + " = ?,"
				+ Shop.LAT_CENTER + " = ?,"
				+ Shop.LON_CENTER + " = ?,"

				+ Shop.DELIVERY_CHARGES + " = ?,"
				+ Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + " = ?,"
				+ Shop.PICK_FROM_SHOP_AVAILABLE + " = ?,"
				+ Shop.HOME_DELIVERY_AVAILABLE + " = ?,"

				+ Shop.SHOP_ENABLED + " = ?,"
				+ Shop.SHOP_WAITLISTED + " = ?,"

				+ Shop.LOGO_IMAGE_PATH + " = ?,"

				+ Shop.SHOP_ADDRESS + " = ?,"
				+ Shop.CITY + " = ?,"
				+ Shop.PINCODE + " = ?,"
				+ Shop.LANDMARK + " = ?,"

				+ Shop.CUSTOMER_HELPLINE_NUMBER + " = ?,"
				+ Shop.DELIVERY_HELPLINE_NUMBER + " = ?,"

				+ Shop.SHORT_DESCRIPTION + " = ?,"
				+ Shop.LONG_DESCRIPTION + " = ?,"

//				+ Shop.TIMESTAMP_CREATED + " = ?,"
				+ Shop.IS_OPEN + " = ?"

				+ " WHERE " + Shop.SHOP_ID + " = ?";
		
		
		
		Connection connection = null;
		PreparedStatement statement = null;
		int updatedRows = -1;
		
		try {
			
			connection = dataSource.getConnection();
			
			statement = connection.prepareStatement(updateStatement);


//			statement.setString(1,shop.getShopName());
//			statement.setObject(2,shop.getDeliveryCharges());

			statement.setObject(1,shop.getShopName());

			statement.setObject(2,shop.getDeliveryRange());
			statement.setObject(3,shop.getLatCenter());
			statement.setObject(4,shop.getLonCenter());

			statement.setObject(5,shop.getDeliveryCharges());
			statement.setObject(6,shop.getBillAmountForFreeDelivery());
			statement.setObject(7,shop.getPickFromShopAvailable());
			statement.setObject(8,shop.getHomeDeliveryAvailable());

			statement.setObject(9,shop.getShopEnabled());
			statement.setObject(10,shop.getShopWaitlisted());

			statement.setString(11,shop.getLogoImagePath());

			statement.setString(12,shop.getShopAddress());
			statement.setString(13,shop.getCity());
			statement.setObject(14,shop.getPincode());
			statement.setObject(15,shop.getLandmark());

			statement.setObject(16,shop.getCustomerHelplineNumber());
			statement.setObject(17,shop.getDeliveryHelplineNumber());

			statement.setObject(18,shop.getShortDescription());
			statement.setObject(19,shop.getLongDescription());

//			statement.setObject(20,shop.getTimestampCreated());
			statement.setObject(20,shop.isOpen());

			statement.setObject(21,shop.getShopID());

			updatedRows = statement.executeUpdate();
			
			
//			System.out.println("Total rows updated: " + updatedRows);
			
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




	public int updateShopByAdmin(Shop shop)
	{


		String updateStatement = "UPDATE " + Shop.TABLE_NAME
				+ " SET "
				+ Shop.SHOP_ENABLED + " = ?,"
				+ Shop.SHOP_WAITLISTED + " = ?,"
				+ Shop.IS_OPEN + " = ?,"
				+ Shop.EXTENDED_CREDIT_LIMIT + " = ?,"

				+ Shop.SHOP_NAME + " = ?,"
				+ Shop.DELIVERY_RANGE + " = ?,"
				+ Shop.LAT_CENTER + " = ?,"
				+ Shop.LON_CENTER + " = ?,"

				+ Shop.DELIVERY_CHARGES + " = ?,"
				+ Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + " = ?,"
				+ Shop.PICK_FROM_SHOP_AVAILABLE + " = ?,"
				+ Shop.HOME_DELIVERY_AVAILABLE + " = ?,"

				+ Shop.LOGO_IMAGE_PATH + " = ?,"

				+ Shop.SHOP_ADDRESS + " = ?,"
				+ Shop.CITY + " = ?,"
				+ Shop.PINCODE + " = ?,"
				+ Shop.LANDMARK + " = ?,"

				+ Shop.CUSTOMER_HELPLINE_NUMBER + " = ?,"
				+ Shop.DELIVERY_HELPLINE_NUMBER + " = ?,"

				+ Shop.SHORT_DESCRIPTION + " = ?,"
				+ Shop.LONG_DESCRIPTION + " = ?"

				+ " WHERE " + Shop.SHOP_ID + " = ?";



		Connection connection = null;
		PreparedStatement statement = null;
		int updatedRows = -1;


		try {



			connection = dataSource.getConnection();
			statement = connection.prepareStatement(updateStatement);
			int i = 0;

			statement.setObject(++i,shop.getShopEnabled());
			statement.setObject(++i,shop.getShopWaitlisted());
			statement.setObject(++i,shop.isOpen());
			statement.setObject(++i,shop.getExtendedCreditLimit());

			statement.setObject(++i,shop.getShopName());

			statement.setObject(++i,shop.getDeliveryRange());
			statement.setObject(++i,shop.getLatCenter());
			statement.setObject(++i,shop.getLonCenter());

			statement.setObject(++i,shop.getDeliveryCharges());
			statement.setObject(++i,shop.getBillAmountForFreeDelivery());
			statement.setObject(++i,shop.getPickFromShopAvailable());
			statement.setObject(++i,shop.getHomeDeliveryAvailable());

			statement.setString(++i,shop.getLogoImagePath());

			statement.setString(++i,shop.getShopAddress());
			statement.setString(++i,shop.getCity());
			statement.setObject(++i,shop.getPincode());
			statement.setObject(++i,shop.getLandmark());

			statement.setObject(++i,shop.getCustomerHelplineNumber());
			statement.setObject(++i,shop.getDeliveryHelplineNumber());

			statement.setObject(++i,shop.getShortDescription());
			statement.setObject(++i,shop.getLongDescription());

			statement.setObject(++i,shop.getShopID());

			updatedRows = statement.executeUpdate();


//			System.out.println("Total rows updated: " + updatedRows);

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



	public int updateShopBySelf(Shop shop)
	{


		String updateStatement = "UPDATE " + Shop.TABLE_NAME
				+ " SET "

				+ Shop.SHOP_NAME + " = ?,"

				+ Shop.DELIVERY_RANGE + " = ?,"
				+ Shop.LAT_CENTER + " = ?,"
				+ Shop.LON_CENTER + " = ?,"

				+ Shop.DELIVERY_CHARGES + " = ?,"
				+ Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + " = ?,"
				+ Shop.PICK_FROM_SHOP_AVAILABLE + " = ?,"
				+ Shop.HOME_DELIVERY_AVAILABLE + " = ?,"

//				+ Shop.SHOP_ENABLED + " = ?,"
//				+ Shop.SHOP_WAITLISTED + " = ?,"

				+ Shop.LOGO_IMAGE_PATH + " = ?,"

				+ Shop.SHOP_ADDRESS + " = ?,"
				+ Shop.CITY + " = ?,"
				+ Shop.PINCODE + " = ?,"
				+ Shop.LANDMARK + " = ?,"

				+ Shop.CUSTOMER_HELPLINE_NUMBER + " = ?,"
				+ Shop.DELIVERY_HELPLINE_NUMBER + " = ?,"

				+ Shop.SHORT_DESCRIPTION + " = ?,"
				+ Shop.LONG_DESCRIPTION + " = ?,"

//				+ Shop.TIMESTAMP_CREATED + " = ?,"
				+ Shop.IS_OPEN + " = ?"

				+ " WHERE " + Shop.SHOP_ADMIN_ID + " = ?";



		Connection connection = null;
		PreparedStatement statement = null;
		int updatedRows = -1;

		try {

			connection = dataSource.getConnection();

			statement = connection.prepareStatement(updateStatement);
			int i = 0;


//			statement.setString(1,shop.getShopName());
//			statement.setObject(2,shop.getDeliveryCharges());

			statement.setObject(++i,shop.getShopName());

			statement.setObject(++i,shop.getDeliveryRange());
			statement.setObject(++i,shop.getLatCenter());
			statement.setObject(++i,shop.getLonCenter());

			statement.setObject(++i,shop.getDeliveryCharges());
			statement.setObject(++i,shop.getBillAmountForFreeDelivery());
			statement.setObject(++i,shop.getPickFromShopAvailable());
			statement.setObject(++i,shop.getHomeDeliveryAvailable());

			statement.setString(++i,shop.getLogoImagePath());

			statement.setString(++i,shop.getShopAddress());
			statement.setString(++i,shop.getCity());
			statement.setObject(++i,shop.getPincode());
			statement.setObject(++i,shop.getLandmark());

			statement.setObject(++i,shop.getCustomerHelplineNumber());
			statement.setObject(++i,shop.getDeliveryHelplineNumber());

			statement.setObject(++i,shop.getShortDescription());
			statement.setObject(++i,shop.getLongDescription());

			statement.setObject(++i,shop.isOpen());

			statement.setObject(++i,shop.getShopAdminID());

			updatedRows = statement.executeUpdate();


//			System.out.println("Total rows updated: " + updatedRows);

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




	public Shop getShopDetails(int ShopID,
							   Double latCenter, Double lonCenter)
	{

		String query = " ";



		query = "SELECT "
				+ " (6371.01 * acos(cos( radians(" + latCenter + ")) * cos( radians(" + Shop.LAT_CENTER + " )) * cos(radians( "
				+ Shop.LON_CENTER + ") - radians("
				+ lonCenter + "))" + " + sin( radians(" + latCenter + ")) * sin(radians(" + Shop.LAT_CENTER + "))))" + " as distance ,"


				+ User.TABLE_NAME + "." + User.PHONE + ","
				+ User.TABLE_NAME + "." + User.NAME + ","

				+ Shop.TABLE_NAME + "." + Shop.ACCOUNT_BALANCE + ","
				+ Shop.TABLE_NAME + "." + Shop.EXTENDED_CREDIT_LIMIT + ","
				+ Shop.TABLE_NAME + "." + Shop.SHOP_WAITLISTED + ","
				+ Shop.TABLE_NAME + "." + Shop.SHOP_ENABLED + ","

				+ Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
				+ Shop.TABLE_NAME + "." + Shop.SHOP_ADMIN_ID + ","
				+ Shop.TABLE_NAME + "." + Shop.SHOP_NAME + ","
				+ Shop.TABLE_NAME + "." + Shop.LON_CENTER + ","
				+ Shop.TABLE_NAME + "." + Shop.LAT_CENTER + ","
				+ Shop.TABLE_NAME + "." + Shop.DELIVERY_RANGE + ","
				+ Shop.TABLE_NAME + "." + Shop.DELIVERY_CHARGES + ","

				+ Shop.TABLE_NAME + "." + Shop.SHOP_ADDRESS + ","
				+ Shop.TABLE_NAME + "." + Shop.CITY + ","
				+ Shop.TABLE_NAME + "." + Shop.PINCODE + ","
				+ Shop.TABLE_NAME + "." + Shop.LANDMARK + ","
				+ Shop.TABLE_NAME + "." + Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + ","

				+ Shop.TABLE_NAME + "." + Shop.CUSTOMER_HELPLINE_NUMBER + ","
				+ Shop.TABLE_NAME + "." + Shop.DELIVERY_HELPLINE_NUMBER + ","
				+ Shop.TABLE_NAME + "." + Shop.SHORT_DESCRIPTION + ","
				+ Shop.TABLE_NAME + "." + Shop.LONG_DESCRIPTION + ","
				+ Shop.TABLE_NAME + "." + Shop.IS_OPEN + ","
				+ Shop.TABLE_NAME + "." + Shop.LOGO_IMAGE_PATH + ","
				+ Shop.TABLE_NAME + "." + Shop.TIMESTAMP_CREATED + ","
				+ Shop.TABLE_NAME + "." + Shop.PICK_FROM_SHOP_AVAILABLE + ","
				+ Shop.TABLE_NAME + "." + Shop.HOME_DELIVERY_AVAILABLE + ","


				+  "avg(" + ShopReview.TABLE_NAME + "." + ShopReview.RATING + ") as avg_rating" + ","
				+  "count( DISTINCT " + ShopReview.TABLE_NAME + "." + ShopReview.END_USER_ID + ") as rating_count" + ""

				+ " FROM " + Shop.TABLE_NAME
				+ " INNER JOIN " + User.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." + Shop.SHOP_ADMIN_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
				+ " LEFT OUTER JOIN " + ShopReview.TABLE_NAME  + " ON (" + ShopReview.TABLE_NAME + "." + ShopReview.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ")"
				+ " WHERE "	+ Shop.TABLE_NAME + "." + Shop.SHOP_ID + "= " + ShopID;


//				+ " FROM " + ShopReview.TABLE_NAME
//				+ " INNER JOIN " + User.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." + Shop.SHOP_ADMIN_ID + " = " + User.TABLE_NAME + "." + User.USER_ID + ")"
//				+ " RIGHT OUTER JOIN " + Shop.TABLE_NAME + " ON (" + ShopReview.TABLE_NAME + "." + ShopReview.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ")"
//				+ " WHERE "	+ Shop.TABLE_NAME + "." + Shop.SHOP_ID + "= " + ShopID;




		query = query

				+ " group by "
				+ Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
				+ User.TABLE_NAME + "." + User.USER_ID ;

		/*
		+ ","
				+ Shop.TABLE_NAME + "." + Shop.SHOP_NAME + ","
				+ Shop.TABLE_NAME + "." + Shop.LON_CENTER + ","
				+ Shop.TABLE_NAME + "." + Shop.LAT_CENTER + ","
				+ Shop.TABLE_NAME + "." + Shop.DELIVERY_RANGE + ","
				+ Shop.TABLE_NAME + "." + Shop.DELIVERY_CHARGES + ","
				+ Shop.TABLE_NAME + "." + Shop.DISTRIBUTOR_ID + ","
				+ Shop.TABLE_NAME + "." + Shop.IMAGE_PATH + ","
				+ Shop.TABLE_NAME + "." + Shop.LAT_MAX + ","
				+ Shop.TABLE_NAME + "." + Shop.LAT_MIN + ","
				+ Shop.TABLE_NAME + "." + Shop.LON_MAX + ","
				+ Shop.TABLE_NAME + "." + Shop.LON_MIN + ","

				+ Shop.TABLE_NAME + "." + Shop.SHOP_ADDRESS + ","
				+ Shop.TABLE_NAME + "." + Shop.CITY + ","
				+ Shop.TABLE_NAME + "." + Shop.PINCODE + ","
				+ Shop.TABLE_NAME + "." + Shop.LANDMARK + ","
				+ Shop.TABLE_NAME + "." + Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + ","
				+ Shop.TABLE_NAME + "." + Shop.CUSTOMER_HELPLINE_NUMBER + ","
				+ Shop.TABLE_NAME + "." + Shop.DELIVERY_HELPLINE_NUMBER + ","
				+ Shop.TABLE_NAME + "." + Shop.SHORT_DESCRIPTION + ","
				+ Shop.TABLE_NAME + "." + Shop.LONG_DESCRIPTION + ","
				+ Shop.TABLE_NAME + "." + Shop.IS_OPEN + ","
				+ Shop.TABLE_NAME + "." + Shop.TIMESTAMP_CREATED + ""*/





//			distancePreset = true;

//		}

		/*else
		{
			query = "SELECT * FROM " + Shop.TABLE_NAME
					+ " WHERE "	+  Shop.ITEM_ID + "= " + ShopID;

		}*/


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


				shop.setRt_distance(rs.getDouble("distance"));
				shop.setRt_rating_avg(rs.getFloat("avg_rating"));
				shop.setRt_rating_count(rs.getFloat("rating_count"));

				shop.setShopID(rs.getInt(Shop.SHOP_ID));
				shop.setShopAdminID(rs.getInt(Shop.SHOP_ADMIN_ID));
				shop.setShopName(rs.getString(Shop.SHOP_NAME));
				shop.setLatCenter(rs.getFloat(Shop.LAT_CENTER));
				shop.setLonCenter(rs.getFloat(Shop.LON_CENTER));
				shop.setDeliveryCharges(rs.getFloat(Shop.DELIVERY_CHARGES));

				shop.setDeliveryRange(rs.getDouble(Shop.DELIVERY_RANGE));

				shop.setLogoImagePath(rs.getString(Shop.LOGO_IMAGE_PATH));
				shop.setShopAddress(rs.getString(Shop.SHOP_ADDRESS));
				shop.setCity(rs.getString(Shop.CITY));
				shop.setPincode(rs.getLong(Shop.PINCODE));
				shop.setLandmark(rs.getString(Shop.LANDMARK));
				shop.setBillAmountForFreeDelivery(rs.getInt(Shop.BILL_AMOUNT_FOR_FREE_DELIVERY));
				shop.setCustomerHelplineNumber(rs.getString(Shop.CUSTOMER_HELPLINE_NUMBER));
				shop.setDeliveryHelplineNumber(rs.getString(Shop.DELIVERY_HELPLINE_NUMBER));
				shop.setShortDescription(rs.getString(Shop.SHORT_DESCRIPTION));
				shop.setLongDescription(rs.getString(Shop.LONG_DESCRIPTION));
				shop.setTimestampCreated(rs.getTimestamp(Shop.TIMESTAMP_CREATED));
				shop.setOpen(rs.getBoolean(Shop.IS_OPEN));
				shop.setPickFromShopAvailable(rs.getBoolean(Shop.PICK_FROM_SHOP_AVAILABLE));
				shop.setHomeDeliveryAvailable(rs.getBoolean(Shop.HOME_DELIVERY_AVAILABLE));

				shop.setAccountBalance(rs.getFloat(Shop.ACCOUNT_BALANCE));
				shop.setExtendedCreditLimit(rs.getFloat(Shop.EXTENDED_CREDIT_LIMIT));
				shop.setShopEnabled(rs.getBoolean(Shop.SHOP_ENABLED));
				shop.setShopWaitlisted(rs.getBoolean(Shop.SHOP_WAITLISTED));



				User shopAdmin = new User();
				shopAdmin.setPhone(rs.getString(User.PHONE));
				shopAdmin.setName(rs.getString(User.NAME));
				shopAdmin.setUserID(rs.getInt(Shop.SHOP_ADMIN_ID));


				shop.setShopAdminProfile(shopAdmin);



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





	public int setShopOpen(boolean isOpen,int shopAdminID)
	{


		String updateStatement = "UPDATE " + Shop.TABLE_NAME
				+ " SET "
				+ Shop.IS_OPEN + " = ?"

				+ " WHERE " + Shop.SHOP_ADMIN_ID + " = ?";



		Connection connection = null;
		PreparedStatement statement = null;
		int updatedRows = -1;

		try {

			connection = dataSource.getConnection();

			statement = connection.prepareStatement(updateStatement);

			statement.setObject(1,isOpen);
			statement.setObject(2,shopAdminID);

			updatedRows = statement.executeUpdate();


//			System.out.println("Total rows updated: " + updatedRows);

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






	public ShopEndPoint getShopsListQuerySimple(
			Boolean underReview,
			Boolean enabled, Boolean waitlisted,
			Double latCenter, Double lonCenter,
			String searchString,
			String sortBy,
			int limit, int offset,
			boolean getRowCount,
			boolean getOnlyMetadata
	)
	{

		String query = "";


		String queryNormal = "SELECT "

				+ "6371 * acos( cos( radians("
				+ latCenter + ")) * cos( radians( lat_center) ) * cos(radians( lon_center ) - radians("
				+ lonCenter + "))"
				+ " + sin( radians(" + latCenter + ")) * sin(radians(lat_center))) as distance" + ","


				+ Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
				+ Shop.SHOP_NAME + ","

				+  "avg(" + ShopReview.TABLE_NAME + "." + ShopReview.RATING + ") as avg_rating" + ","
				+  "count( DISTINCT " + ShopReview.TABLE_NAME + "." + ShopReview.END_USER_ID + ") as rating_count" + ","

				+ "count(*) over() AS full_count " + ","

				+ Shop.DELIVERY_CHARGES + ","
				+ Shop.PICK_FROM_SHOP_AVAILABLE + ","
				+ Shop.HOME_DELIVERY_AVAILABLE + ","


				+ Shop.LOGO_IMAGE_PATH + ","
				+ Shop.SHOP_ADDRESS + ","
				+ Shop.CITY + ","

				+ Shop.IS_OPEN + ""

				+ " FROM " + Shop.TABLE_NAME
				+ " LEFT OUTER JOIN " + ShopReview.TABLE_NAME  + " ON (" + ShopReview.TABLE_NAME + "." + ShopReview.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ")"
				+ " WHERE TRUE ";






		if(searchString !=null)
		{
			String queryPartSearch = Shop.TABLE_NAME + "." + Shop.SHOP_NAME +" ilike '%" + searchString + "%'"
					+ " or " + Shop.TABLE_NAME + "." + Shop.LONG_DESCRIPTION + " ilike '%" + searchString + "%'"
					+ " or " + Shop.TABLE_NAME + "." + Shop.SHOP_ADDRESS + " ilike '%" + searchString + "%'"
					+ " or CAST ( " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + " AS text )" + " ilike '%" + searchString + "%'" + "";


			queryNormal = queryNormal + " AND " + queryPartSearch;

		}





		if(underReview !=null)
		{
			queryNormal = queryNormal + " AND " + Shop.SHOP_ENABLED + " IS NULL ";
		}



		if(enabled !=null)
		{
			queryNormal = queryNormal + " AND " + Shop.SHOP_ENABLED + " = "  + enabled;
		}


		if(waitlisted !=null)
		{
			queryNormal = queryNormal + " AND " + Shop.SHOP_WAITLISTED + " = "  + waitlisted;
		}





		String queryGroupBy = "";

		queryGroupBy = queryGroupBy + " group by "
				+ Shop.TABLE_NAME + "." + Shop.SHOP_ID ;


		queryNormal = queryNormal + queryGroupBy;




		// Applying Filters



		if(sortBy!=null)
		{
			if(!sortBy.equals(""))
			{
				String queryPartSortBy = " ORDER BY " + sortBy;

				queryNormal = queryNormal + queryPartSortBy;
			}
		}




		queryNormal = queryNormal + " LIMIT " + limit + " " + " OFFSET " + offset;



		query = queryNormal;





		ShopEndPoint endPoint = new ShopEndPoint();
		endPoint.setItemCount(0);

		ArrayList<Shop> shopList = new ArrayList<Shop>();


		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			connection = dataSource.getConnection();
			statement = connection.createStatement();

			rs = statement.executeQuery(query);

			while(rs.next())
			{

				Shop shop = new Shop();

				shop.setRt_distance(rs.getDouble("distance"));
				shop.setRt_rating_avg(rs.getFloat("avg_rating"));
				shop.setRt_rating_count(rs.getFloat("rating_count"));

				shop.setShopID(rs.getInt(Shop.SHOP_ID));

				shop.setShopName(rs.getString(Shop.SHOP_NAME));

				shop.setDeliveryCharges(rs.getFloat(Shop.DELIVERY_CHARGES));
				shop.setPickFromShopAvailable(rs.getBoolean(Shop.PICK_FROM_SHOP_AVAILABLE));
				shop.setHomeDeliveryAvailable(rs.getBoolean(Shop.HOME_DELIVERY_AVAILABLE));

				shop.setLogoImagePath(rs.getString(Shop.LOGO_IMAGE_PATH));

				shop.setShopAddress(rs.getString(Shop.SHOP_ADDRESS));
				shop.setCity(rs.getString(Shop.CITY));

				shop.setOpen(rs.getBoolean(Shop.IS_OPEN));

				endPoint.setItemCount(rs.getInt("full_count"));


				shopList.add(shop);

			}

//			System.out.println("Total Shops queried " + shopList.size());


			endPoint.setResults(shopList);


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





	public ShopEndPoint getShopListQueryJoin(
			Integer itemCategoryID,
			Double latCenter, Double lonCenter,
			Double deliveryRangeMin,Double deliveryRangeMax,
			Double proximity,
			String searchString,
			String sortBy,
			int limit, int offset,
			boolean getRowCount,
			boolean getOnlyMetadata
	)
	{

		String queryCount = "";
		String queryJoin = "";

		queryJoin = "SELECT "
				+ "6371 * acos(cos( radians("
				+ latCenter + ")) * cos( radians( lat_center) ) * cos(radians( lon_center ) - radians("
				+ lonCenter + "))" + " + sin( radians(" + latCenter + ")) * sin(radians(lat_center))) as distance" + ","

				+ Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
				+ Shop.SHOP_NAME + ","
				+ Shop.DELIVERY_CHARGES + ","
				+ Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + ","
				+ Shop.PICK_FROM_SHOP_AVAILABLE + ","
				+ Shop.HOME_DELIVERY_AVAILABLE + ","
				+ Shop.LOGO_IMAGE_PATH + ","
				+ Shop.SHOP_ADDRESS + ","
				+ Shop.CITY + ","
				+ Shop.IS_OPEN + ","


				+  "avg(" + ShopReview.TABLE_NAME + "." + ShopReview.RATING + ") as avg_rating" + ","
				+  "count( DISTINCT " + ShopReview.TABLE_NAME + "." + ShopReview.END_USER_ID + ") as rating_count" + ""

				+ " FROM " + Shop.TABLE_NAME
				+ " LEFT OUTER JOIN " + ShopReview.TABLE_NAME + " ON (" + ShopReview.TABLE_NAME + "." + ShopReview.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ")"
				+ "," + ShopItem.TABLE_NAME + "," + Item.TABLE_NAME + "," + ItemCategory.TABLE_NAME

				+ " WHERE " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + "=" + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID
				+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + "=" + Item.TABLE_NAME + "." + Item.ITEM_ID
				+ " AND " + Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + "=" + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID
				+ " AND " + Shop.TABLE_NAME + "." + Shop.IS_OPEN + " = TRUE "
				+ " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ENABLED + " = TRUE "
				+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + " > 0 "
				+ " AND " + Shop.TABLE_NAME + "." + Shop.ACCOUNT_BALANCE + ">=" + GlobalConstants.min_account_balance_for_shop;




		// Visibility Filter : Shops should not be visible outside of their delivery range
		if(latCenter != null && lonCenter != null)
		{
			// Applying shop visibility filter. Gives all the shops which are visible at the given location defined by
			// latCenter and lonCenter. For more information see the API documentation.


			queryJoin = queryJoin + " AND (6371.01 * acos(cos( radians("
					+ latCenter + ")) * cos( radians(" + Shop.LAT_CENTER + " )) * cos(radians( " + Shop.LON_CENTER
					+ ") - radians(" + lonCenter
					+ "))" + " + sin( radians(" + latCenter + ")) * sin(radians(" + Shop.LAT_CENTER
					+ ")))) <= " + Shop.DELIVERY_RANGE ;;
		}




		// Delivery Range Filter : apply
		if(deliveryRangeMin != null || deliveryRangeMax != null){
			// apply delivery range filter
			queryJoin = queryJoin + " AND " + Shop.TABLE_NAME + "." + Shop.DELIVERY_RANGE
					+ " BETWEEN " + deliveryRangeMin + " AND " + deliveryRangeMax;

		}




		// Proximity Filter
		if(proximity != null)
		{
			// filter by proximity using Haversine formula

			queryJoin = queryJoin + " AND (6371.01 * acos(cos( radians(" + latCenter + ")) * cos( radians(" + Shop.LAT_CENTER
					+ " )) * cos(radians( " + Shop.LON_CENTER + ") - radians(" + lonCenter + "))"
					+ " + sin( radians(" + latCenter + ")) * sin(radians(" + Shop.LAT_CENTER + ")))) <= " + proximity ;;
		}





		if(searchString !=null)
		{
			String queryPartSearch = Shop.TABLE_NAME + "." + Shop.SHOP_NAME +" ilike '%" + searchString + "%'"
					+ " or " + Shop.TABLE_NAME + "." + Shop.LONG_DESCRIPTION + " ilike '%" + searchString + "%'"
					+ " or " + Shop.TABLE_NAME + "." + Shop.SHOP_ADDRESS + " ilike '%" + searchString + "%'";


			queryJoin = queryJoin + " AND " + queryPartSearch;
		}




		if(itemCategoryID != null)
		{
			// filter shops by Item Category ID
			queryJoin = queryJoin + " AND "
					+ ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID + " = " + itemCategoryID;
		}



		queryJoin = queryJoin + " group by "
				+ Shop.TABLE_NAME + "." + Shop.SHOP_ID ;;



		queryCount = queryJoin;


		// Applying Filters



		if(sortBy!=null)
		{
			if(!sortBy.equals(""))
			{
				String queryPartSortBy = " ORDER BY " + sortBy;

				queryJoin = queryJoin + queryPartSortBy;
			}
		}




		queryJoin = queryJoin + " LIMIT " + limit + " " + " OFFSET " + offset;



		queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";





		ShopEndPoint endPoint = new ShopEndPoint();

		
		ArrayList<Shop> shopList = new ArrayList<>();
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try {
			
			connection = dataSource.getConnection();


			if(!getOnlyMetadata) {

				statement = connection.createStatement();

				rs = statement.executeQuery(queryJoin);

				while(rs.next())
				{

					Shop shop = new Shop();

					shop.setRt_distance(rs.getDouble("distance"));
					shop.setRt_rating_avg(rs.getFloat("avg_rating"));
					shop.setRt_rating_count(rs.getFloat("rating_count"));

					shop.setShopID(rs.getInt(Shop.SHOP_ID));
					shop.setShopName(rs.getString(Shop.SHOP_NAME));
					shop.setDeliveryCharges(rs.getFloat(Shop.DELIVERY_CHARGES));
					shop.setBillAmountForFreeDelivery(rs.getInt(Shop.BILL_AMOUNT_FOR_FREE_DELIVERY));
					shop.setPickFromShopAvailable(rs.getBoolean(Shop.PICK_FROM_SHOP_AVAILABLE));
					shop.setHomeDeliveryAvailable(rs.getBoolean(Shop.HOME_DELIVERY_AVAILABLE));
					shop.setLogoImagePath(rs.getString(Shop.LOGO_IMAGE_PATH));
					shop.setShopAddress(rs.getString(Shop.SHOP_ADDRESS));
					shop.setCity(rs.getString(Shop.CITY));
					shop.setOpen(rs.getBoolean(Shop.IS_OPEN));

					shopList.add(shop);
				}



				endPoint.setResults(shopList);
			}



			if(getRowCount)
			{
				statement = connection.createStatement();
				rs = statement.executeQuery(queryCount);

				while(rs.next())
				{

					endPoint.setItemCount(rs.getInt("item_count"));
//					System.out.println("Item Count ItemDAO : " + String.valueOf(endPoint.getItemCount()));
				}
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

		return endPoint;
	}








	public ShopEndPoint filterShopsByItemCategory(Integer itemCategoryID,
                                                     Double latCenter, Double lonCenter,
                                                     Double deliveryRangeMin, Double deliveryRangeMax,
                                                     Double proximity,
                                                     String sortBy,
													 int limit, int offset,
													 boolean getRowCount,
													 boolean getOnlyMetadata)
	{




			// a recursive CTE (Common table Expression) query. This query is used for retrieving hierarchical / tree set data.

			String withRecursiveStart = "WITH RECURSIVE category_tree("
					+ ItemCategory.ITEM_CATEGORY_ID + ","
					+ ItemCategory.PARENT_CATEGORY_ID
					+ ") AS (";


			String queryJoin = "SELECT DISTINCT "

					+ ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID + ","
					+ ItemCategory.TABLE_NAME + "." + ItemCategory.PARENT_CATEGORY_ID

					+ " FROM "
					+ ItemCategory.TABLE_NAME

					+ " WHERE "
					+ ItemCategory.ITEM_CATEGORY_ID  + " = " + itemCategoryID;


			String union = " UNION ";

			String querySelect = " SELECT "

					+ "cat." + ItemCategory.ITEM_CATEGORY_ID + ","
					+ "cat." + ItemCategory.PARENT_CATEGORY_ID

					+ " FROM category_tree tempCat," + 	ItemCategory.TABLE_NAME + " cat"
					+ " WHERE cat." + ItemCategory.PARENT_CATEGORY_ID
					+ " = tempcat." + ItemCategory.ITEM_CATEGORY_ID
					+ " )";


			String queryLast = " SELECT "
					+ ItemCategory.ITEM_CATEGORY_ID
					+ " FROM category_tree";



			String queryRecursive = withRecursiveStart + queryJoin + union + querySelect +  queryLast;





//		System.out.println(query);

		String queryCount = "";


		queryJoin = "SELECT DISTINCT "

				+ "6371 * acos(cos( radians("
				+ latCenter + ")) * cos( radians( lat_center) ) * cos(radians( lon_center ) - radians("
				+ lonCenter + "))"
				+ " + sin( radians(" + latCenter + ")) * sin(radians(lat_center))) as distance" + ","

				+ Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
				+ Shop.TABLE_NAME + "." + Shop.SHOP_NAME + ","
				+ Shop.TABLE_NAME + "." + Shop.DELIVERY_CHARGES + ","
				+ Shop.TABLE_NAME + "." + Shop.SHOP_ADDRESS + ","
				+ Shop.TABLE_NAME + "." + Shop.CITY + ","
				+ Shop.TABLE_NAME + "." + Shop.LOGO_IMAGE_PATH + ","


//				+ Shop.TABLE_NAME + "." + Shop.LON_CENTER + ","
//				+ Shop.TABLE_NAME + "." + Shop.LAT_CENTER + ","
//				+ Shop.TABLE_NAME + "." + Shop.DELIVERY_RANGE + ","
//				+ Shop.TABLE_NAME + "." + Shop.PINCODE + ","
//				+ Shop.TABLE_NAME + "." + Shop.LANDMARK + ","
//				+ Shop.TABLE_NAME + "." + Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + ","
//				+ Shop.TABLE_NAME + "." + Shop.CUSTOMER_HELPLINE_NUMBER + ","
//				+ Shop.TABLE_NAME + "." + Shop.DELIVERY_HELPLINE_NUMBER + ","
//				+ Shop.TABLE_NAME + "." + Shop.SHORT_DESCRIPTION + ","
//				+ Shop.TABLE_NAME + "." + Shop.LONG_DESCRIPTION + ","
//				+ Shop.TABLE_NAME + "." + Shop.IS_OPEN + ","
//				+ Shop.TABLE_NAME + "." + Shop.TIMESTAMP_CREATED + ","

				+  "avg(" + ShopReview.TABLE_NAME + "." + ShopReview.RATING + ") as avg_rating" + ","
				+  "count( DISTINCT " + ShopReview.TABLE_NAME + "." + ShopReview.END_USER_ID + ") as rating_count" + ""

				+ " FROM " + Shop.TABLE_NAME
				+ " LEFT OUTER JOIN " + ShopReview.TABLE_NAME + " ON (" + ShopReview.TABLE_NAME + "." + ShopReview.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ")"
				+ " INNER JOIN " + ShopItem.TABLE_NAME + " ON (" + Shop.TABLE_NAME + "." + Shop.SHOP_ID + "=" + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + ")"
				+ " INNER JOIN " + Item.TABLE_NAME + " ON (" + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + "=" + Item.TABLE_NAME + "." + Item.ITEM_ID + ")"
				+ " INNER JOIN " + ItemCategory.TABLE_NAME + " ON (" + Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + "=" + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID + ")"
				+ " AND " + Shop.TABLE_NAME + "." + Shop.IS_OPEN + " = TRUE "
				+ " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ENABLED + " = TRUE "
				+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + " > 0 ";


		queryJoin = queryJoin
				+ " AND " + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID + " IN " + " (" + queryRecursive + ")";



		// Visibility Filter : Apply
		if(latCenter != null && lonCenter != null)
		{
			// Applying shop visibility filter. Gives all the shops which are visible at the given location defined by
			// latCenter and lonCenter. For more information see the API documentation.


			queryJoin = queryJoin + " AND (6371.01 * acos(cos( radians("
					+ latCenter + ")) * cos( radians(" + Shop.LAT_CENTER
					+ " )) * cos(radians( " + Shop.LON_CENTER + ") - radians("
					+ lonCenter + "))" + " + sin( radians(" + latCenter + ")) * sin(radians("
					+ Shop.LAT_CENTER + ")))) <= " + Shop.DELIVERY_RANGE ;
		}



		// Delivery Range Filter : apply
		if(deliveryRangeMin != null && deliveryRangeMax != null){
			// apply delivery range filter
			queryJoin = queryJoin + " AND " + Shop.TABLE_NAME + "." + Shop.DELIVERY_RANGE + " BETWEEN " + deliveryRangeMin + " AND " + deliveryRangeMax;

		}




		// Proximity Filter
		if(proximity != null)
		{
			queryJoin = queryJoin + " AND (6371.01 * acos(cos( radians(" + latCenter
					+ ")) * cos( radians(" + Shop.LAT_CENTER + " )) * cos(radians( " + Shop.LON_CENTER
					+ ") - radians(" + lonCenter + "))" + " + sin( radians(" + latCenter + ")) * sin(radians("
					+ Shop.LAT_CENTER + ")))) <= " + proximity ;;
		}



		queryJoin = queryJoin + " group by " + "distance," + Shop.TABLE_NAME + "." + Shop.SHOP_ID ;

		queryCount = queryJoin;





		if(sortBy!=null)
		{
			if(!sortBy.equals(""))
			{
				String queryPartSortBy = " ORDER BY " + sortBy;

				queryJoin = queryJoin + queryPartSortBy;
			}
		}




		queryJoin = queryJoin + " LIMIT " + limit + " " + " OFFSET " + offset;

		queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";



		ShopEndPoint endPoint = new ShopEndPoint();

		ArrayList<Shop> shopList = new ArrayList<>();

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			connection = dataSource.getConnection();

			if(!getOnlyMetadata) {

				statement = connection.createStatement();

				rs = statement.executeQuery(queryJoin);

				while(rs.next())
				{

					Shop shop = new Shop();
					shop.setRt_distance(rs.getDouble("distance"));
					shop.setRt_rating_avg(rs.getFloat("avg_rating"));
					shop.setRt_rating_count(rs.getFloat("rating_count"));

					shop.setShopID(rs.getInt(Shop.SHOP_ID));
					shop.setShopName(rs.getString(Shop.SHOP_NAME));
					shop.setDeliveryCharges(rs.getFloat(Shop.DELIVERY_CHARGES));
					shop.setLogoImagePath(rs.getString(Shop.LOGO_IMAGE_PATH));
					shop.setShopAddress(rs.getString(Shop.SHOP_ADDRESS));
					shop.setCity(rs.getString(Shop.CITY));




//				shop.setDeliveryRange(rs.getDouble(Shop.DELIVERY_RANGE));
//				shop.setLatCenter(rs.getFloat(Shop.LAT_CENTER));
//				shop.setLonCenter(rs.getFloat(Shop.LON_CENTER));

//				shop.setPincode(rs.getLong(Shop.PINCODE));
//				shop.setLandmark(rs.getString(Shop.LANDMARK));
//				shop.setBillAmountForFreeDelivery(rs.getInt(Shop.BILL_AMOUNT_FOR_FREE_DELIVERY));
//				shop.setCustomerHelplineNumber(rs.getString(Shop.CUSTOMER_HELPLINE_NUMBER));
//				shop.setDeliveryHelplineNumber(rs.getString(Shop.DELIVERY_HELPLINE_NUMBER));
//				shop.setShortDescription(rs.getString(Shop.SHORT_DESCRIPTION));
//				shop.setLongDescription(rs.getString(Shop.LONG_DESCRIPTION));
//				shop.setTimestampCreated(rs.getTimestamp(Shop.TIMESTAMP_CREATED));
//				shop.setOpen(rs.getBoolean(Shop.IS_OPEN));


					shopList.add(shop);

				}


				endPoint.setResults(shopList);
			}



			if(getRowCount)
			{
				statement = connection.createStatement();
				rs = statement.executeQuery(queryCount);

				while(rs.next())
				{

					endPoint.setItemCount(rs.getInt("item_count"));
//					System.out.println("Item Count ItemDAO : " + String.valueOf(endPoint.getItemCount()));
				}
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



			return endPoint;
	}










//	String searchString,
//	String sortBy,
//	Integer limit, Integer offset
	public ArrayList<Shop> getShopsForShopFilters(
			Double latCenter, Double lonCenter,
			Double deliveryRangeMin,Double deliveryRangeMax,
			Double proximity)
	{

		String query = "";
		String queryJoin = "";

		queryJoin = "SELECT DISTINCT "

				+ Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
				+ Shop.SHOP_NAME + ","

				+ Shop.DELIVERY_RANGE + ","
				+ Shop.LAT_CENTER + ","
				+ Shop.LON_CENTER + ""

				+ " FROM " + Shop.TABLE_NAME + "," + ShopItem.TABLE_NAME + "," + Item.TABLE_NAME + "," + ItemCategory.TABLE_NAME
				+ " WHERE " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + "=" + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID
				+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID
				+ " AND " + Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + " = " + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID;


		// Visibility Filter : Apply
		if(latCenter != null && lonCenter != null)
		{
			// Applying shop visibility filter. Gives all the shops which are visible at the given location defined by
			// latCenter and lonCenter. For more information see the API documentation.
			String queryPartlatLonCenter = "";

			String queryPartProximity = null;

			queryPartProximity = " ((6371.01 * acos(cos( radians( "
					+ latCenter
					+ " )) * cos( radians( "
					+ Shop.LAT_CENTER
					+ " )) * cos(radians( "
					+ Shop.LON_CENTER
					+ " ) - radians( "
					+ lonCenter
					+ " )) "
					+ " + sin( radians( "
					+ latCenter
					+ " )) * sin(radians( "
					+ Shop.LAT_CENTER
					+ " )))) <= "
					+ Shop.DELIVERY_RANGE + " ) " ;


			queryPartlatLonCenter = queryPartlatLonCenter + " 6371.01 * acos( cos( radians("
					+ latCenter + ")) * cos( radians( lat_center) ) * cos(radians( lon_center ) - radians("
					+ lonCenter + "))"
					+ " + sin( radians(" + latCenter + ")) * sin(radians(lat_center))) <= delivery_range ";

			queryJoin = queryJoin + " AND " + queryPartProximity;

		}



		// Delivery Range Filter : apply
		if(deliveryRangeMin != null && deliveryRangeMax != null){

			// apply delivery range filter
			String queryPartDeliveryRange = "";


			queryPartDeliveryRange = queryPartDeliveryRange + Shop.TABLE_NAME
					+ "."
					+ Shop.DELIVERY_RANGE
					+ " BETWEEN " + deliveryRangeMin + " AND " + deliveryRangeMax;
			//+ " <= " + deliveryRange;

			queryJoin = queryJoin + " AND " + queryPartDeliveryRange;

		}


		// Proximity Filter
		if(proximity != null)
		{
			// proximity > 0 && (deliveryRangeMax==0 || (deliveryRangeMax > 0 && proximity <= deliveryRangeMax))

			String queryPartProximity = "";


			// filter using Haversine formula using SQL math functions
			queryPartProximity = queryPartProximity
					+ " (6371.01 * acos(cos( radians("
					+ latCenter
					+ ")) * cos( radians("
					+ Shop.LAT_CENTER
					+ " )) * cos(radians( "
					+ Shop.LON_CENTER
					+ ") - radians("
					+ lonCenter
					+ "))"
					+ " + sin( radians("
					+ latCenter
					+ ")) * sin(radians("
					+ Shop.LAT_CENTER
					+ ")))) <= "
					+ proximity ;



			queryJoin = queryJoin + " AND " + queryPartProximity;
		}

		query = queryJoin;





		ArrayList<Shop> shopList = new ArrayList<Shop>();


		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {

			connection = dataSource.getConnection();
			statement = connection.createStatement();

			rs = statement.executeQuery(query);

			while(rs.next())
			{

				Shop shop = new Shop();

				shop.setShopID(rs.getInt(Shop.SHOP_ID));
				shop.setShopName(rs.getString(Shop.SHOP_NAME));
				shop.setDeliveryRange(rs.getDouble(Shop.DELIVERY_RANGE));
				shop.setLatCenter(rs.getFloat(Shop.LAT_CENTER));
				shop.setLonCenter(rs.getFloat(Shop.LON_CENTER));

				shopList.add(shop);

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

		return shopList;
	}




	public ArrayList<Shop> getShopsForShopFiltersPrepared(
			Double latCenter, Double lonCenter,
			Double deliveryRangeMin,Double deliveryRangeMax,
			Double proximity)
	{

		String query = "";
		String queryJoin = "";

		queryJoin = "SELECT DISTINCT "
				+ Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
				+ Shop.SHOP_NAME + ","

				+ Shop.DELIVERY_RANGE + ","
				+ Shop.LAT_CENTER + ","
				+ Shop.LON_CENTER + ""

				+ " FROM " + Shop.TABLE_NAME + "," + ShopItem.TABLE_NAME + "," + Item.TABLE_NAME + "," + ItemCategory.TABLE_NAME
				+ " WHERE " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + "=" + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID
				+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID
				+ " AND " + Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + " = " + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID

				+ " AND " // Visibility Filter
				+ " ((6371.01 * acos(cos( radians( ? )) * cos( radians( " + Shop.LAT_CENTER + " )) * cos(radians( "
				+ Shop.LON_CENTER + " ) - radians( ? )) " + " + sin( radians( ? )) * sin(radians( "
				+ Shop.LAT_CENTER + " )))) <= " + Shop.DELIVERY_RANGE + " ) ";


//		if(latCenter!=null && lonCenter!=null)
//		{
//			queryJoin = queryJoin
//		}


		if(deliveryRangeMin!=null && deliveryRangeMax!=null)
		{
			queryJoin = queryJoin + " AND "  // Delivery Range Filter
					+ Shop.TABLE_NAME + "." + Shop.DELIVERY_RANGE + " BETWEEN ? AND ? ";
		}


		if(proximity!=null)
		{
			queryJoin = queryJoin + " AND " // Proximity Filter
					+ " (6371.01 * acos(cos( radians( ? )) * cos( radians(" + Shop.LAT_CENTER + " )) * cos(radians( "
					+ Shop.LON_CENTER + ") - radians( ? ))" + " + sin( radians( ? )) * sin(radians("
					+ Shop.LAT_CENTER + ")))) <= ?";
		}



		//VisibilityFilter 1. latCenter : 2. lonCenter : 3. latCenter
		//Proximity Filter 1. latCenter : 2. lonCenter : 3. latCenter 4: proximity


		String queryCount = "SELECT COUNT(*) as item_count FROM (" + query + ") AS temp";

		query = queryJoin;

//		ShopEndPoint endPoint = getShopsForShopFiltersPrepared(latCenter,lonCenter,deliveryRangeMin,deliveryRangeMax,proximity);
		ArrayList<Shop> shopList = new ArrayList<Shop>();

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {

			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);


			// case 1 : No filters
			// Case 2 : Delivery Range Filter OFF and Proximity Filter ON
			// Case 3 : Proximity Filter OFF and Delivery Range FIlter ON
			// Case 4 : Both filters ON



			statement.setDouble(1,latCenter);
			statement.setDouble(2,lonCenter);
			statement.setDouble(3,latCenter);

			int i = 3;
			if(deliveryRangeMin!=null && deliveryRangeMax!=null)
			{
				statement.setObject(++i,deliveryRangeMin);
				statement.setObject(++i,deliveryRangeMax);
			}

			if(proximity!=null)
			{
				statement.setDouble(++i,latCenter);
				statement.setDouble(++i,lonCenter);
				statement.setDouble(++i,latCenter);

				statement.setObject(++i,proximity);
			}




			/*if(!(deliveryRangeMin!=null && deliveryRangeMax!=null) && (proximity != null))
			{
				statement.setDouble(4,latCenter);
				statement.setDouble(5,lonCenter);
				statement.setDouble(6,latCenter);

				statement.setObject(7,proximity);
			}
			else if((deliveryRangeMin!=null && deliveryRangeMax!=null) && (proximity==null))
			{
				statement.setObject(4,deliveryRangeMin);
				statement.setObject(5,deliveryRangeMax);
			}
			else if((deliveryRangeMin!=null && deliveryRangeMax!=null) && (proximity!=null))
			{
				statement.setObject(4,deliveryRangeMin);
				statement.setObject(5,deliveryRangeMax);

				statement.setDouble(6,latCenter);
				statement.setDouble(7,lonCenter);
				statement.setDouble(8,latCenter);

				statement.setObject(9,proximity);
			}
				*/


			rs = statement.executeQuery();


			while(rs.next())
			{

				Shop shop = new Shop();
				shop.setShopID(rs.getInt(Shop.SHOP_ID));
				shop.setShopName(rs.getString(Shop.SHOP_NAME));
				shop.setDeliveryRange(rs.getDouble(Shop.DELIVERY_RANGE));
				shop.setLatCenter(rs.getFloat(Shop.LAT_CENTER));
				shop.setLonCenter(rs.getFloat(Shop.LON_CENTER));

//				endPoint.setItemCount(rs.getInt("full_count"));

				shopList.add(shop);
			}


//			endPoint.setResults(shopList);

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

		return shopList;
	}

}
