package org.nearbyshops.DAOs.DAORoles;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.ModelRoles.DeliveryGuyData;
import org.nearbyshops.Model.ModelRoles.ShopStaffPermissions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DAODeliveryGuy {


	private HikariDataSource dataSource = Globals.getDataSource();



	public int getShopIDforDeliveryGuy(int staffUserID) {

		String query = "SELECT " + DeliveryGuyData.SHOP_ID + ""
					+ " FROM "   + DeliveryGuyData.TABLE_NAME
					+ " WHERE "  + DeliveryGuyData.STAFF_USER_ID + " = ?";



		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;


		//Distributor distributor = null;
		int shopID = -1;

		try {

			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);

			statement.setObject(1,staffUserID);

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


//	public void logMessage(String message)
//	{
//		System.out.println(message);
//	}



	public int updateDeliveryGuyLocation(DeliveryGuyData data)
	{


		String updateLocation = "UPDATE " + DeliveryGuyData.TABLE_NAME
				             + " SET " + DeliveryGuyData.LAT_CURRENT + "=?,"
									  + DeliveryGuyData.LON_CURRENT + "=?"
							 + " WHERE " + DeliveryGuyData.STAFF_USER_ID + " = ?";


		Connection connection = null;
		PreparedStatement statement = null;

		int rowCountUpdated = 0;

		try {

			connection = dataSource.getConnection();
			connection.setAutoCommit(false);


			statement = connection.prepareStatement(updateLocation,PreparedStatement.RETURN_GENERATED_KEYS);
			int i = 0;


			if(data!=null)
			{
				statement.setObject(++i,data.getLatCurrent());
				statement.setObject(++i,data.getLonCurrent());
				statement.setObject(++i,data.getStaffUserID());

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




	public DeliveryGuyData getDeliveryGuyData(int userID)
	{

		boolean isFirst = true;

		String query = "SELECT "

				+ DeliveryGuyData.LAT_CURRENT + ","
				+ DeliveryGuyData.LON_CURRENT + ","
				+ DeliveryGuyData.IS_EMPLOYED_BY_SHOP + ","
				+ DeliveryGuyData.SHOP_ID + ","
				+ DeliveryGuyData.CURRENT_BALANCE + ","

				+ " FROM "  + DeliveryGuyData.TABLE_NAME
				+ " WHERE " + DeliveryGuyData.STAFF_USER_ID  + " = ? ";



		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;


		//Distributor distributor = null;
		DeliveryGuyData deliveryGuyData = null;

		try {

			connection = dataSource.getConnection();
			statement = connection.prepareStatement(query);

			int i = 0;


			statement.setObject(++i,userID); // username


			rs = statement.executeQuery();

			while(rs.next())
			{
				deliveryGuyData = new DeliveryGuyData();

				deliveryGuyData.setLatCurrent(rs.getDouble(DeliveryGuyData.LAT_CURRENT));
				deliveryGuyData.setLonCurrent(rs.getDouble(DeliveryGuyData.LON_CURRENT));
				deliveryGuyData.setEmployedByShop(rs.getBoolean(DeliveryGuyData.IS_EMPLOYED_BY_SHOP));
				deliveryGuyData.setShopID(rs.getInt(DeliveryGuyData.SHOP_ID));
				deliveryGuyData.setCurrentBalance(rs.getDouble(DeliveryGuyData.CURRENT_BALANCE));

				deliveryGuyData.setStaffUserID(rs.getInt(ShopStaffPermissions.STAFF_ID));

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

		return deliveryGuyData;
	}

}
