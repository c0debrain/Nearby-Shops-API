package org.nearbyshops.DAOs.DAOOrders;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Item;
import org.nearbyshops.Model.Order;
import org.nearbyshops.Model.OrderItem;
import org.nearbyshops.Model.ModelEndpoint.OrderItemEndPoint;

import java.sql.*;
import java.util.ArrayList;


public class OrderItemService {



	private HikariDataSource dataSource = Globals.getDataSource();
	
	
	
	public int saveOrderItem(OrderItem orderItem)
	{	
		
		Connection connection = null;
		PreparedStatement statement = null;
		int rowCount = -1;



		String insertOrderItem = "INSERT INTO "
				+ OrderItem.TABLE_NAME
				+ "("
				+ OrderItem.ORDER_ID + ","
				+ OrderItem.ITEM_ID + ","
				+ OrderItem.ITEM_QUANTITY + ","
				+ OrderItem.ITEM_PRICE_AT_ORDER + ""
				+ ") VALUES(?,?,?,?)";


		/*+ "" + orderItem.getOrderID() + ","
				+ "" + orderItem.getItemID() + ","
				+ "" + orderItem.getItemQuantity() + ","
				+ "" + orderItem.getItemPriceAtOrder() +
		*/


		try {
			
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(insertOrderItem,Statement.RETURN_GENERATED_KEYS);

			statement.setObject(1,orderItem.getOrderID());
			statement.setObject(2,orderItem.getItemID());
			statement.setObject(3,orderItem.getItemQuantity());
			statement.setObject(4,orderItem.getItemPriceAtOrder());

			rowCount = statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();

			//if(rs.next())
			//{
			//	rowCount = rs.getInt(1);
			//}
			

			
			
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
	


	public int updateOrderItem(OrderItem orderItem)
	{

		String updateStatement = "UPDATE "
				+ OrderItem.TABLE_NAME
				+ " SET "
				+ OrderItem.ITEM_ID + " = " + orderItem.getItemID() + ","
				+ OrderItem.ORDER_ID + " = " + orderItem.getOrderID() + ","
				+ OrderItem.ITEM_QUANTITY + " = " + orderItem.getItemQuantity() + ","
				+ OrderItem.ITEM_PRICE_AT_ORDER + " = " + orderItem.getItemPriceAtOrder()

				+ " WHERE "
				+ OrderItem.ORDER_ID + " = " + orderItem.getOrderID()
				+ " AND "
				+ OrderItem.ITEM_ID + " = " + orderItem.getItemID();



		Connection connection = null;
		Statement statement = null;
		int updatedRows = -1;
		
		try {
			
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			
			updatedRows = statement.executeUpdate(updateStatement);
			
			
			System.out.println("Total rows updated: " + updatedRows);	
			
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
	



	public int deleteOrderItem(int itemID,int orderID)
	{

		String deleteStatement = "DELETE FROM " + OrderItem.TABLE_NAME;


		boolean isFirst = true;

		if(itemID > 0)
		{
			deleteStatement = deleteStatement + " WHERE " + OrderItem.ITEM_ID + " = " + itemID;
			isFirst = false;
		}

		if(orderID > 0)
		{
			if(isFirst)
			{
				deleteStatement = deleteStatement + " WHERE " + OrderItem.ORDER_ID + " = " + orderID;
			}else
			{
				deleteStatement = deleteStatement + " AND " + OrderItem.ORDER_ID + " = " + orderID;
			}

		}




		Connection conn= null;
		Statement stmt = null;
		int rowsCountDeleted = 0;
		try {
			
			conn = dataSource.getConnection();
			stmt = conn.createStatement();

			rowsCountDeleted = stmt.executeUpdate(deleteStatement);
			
			System.out.println(" Deleted Count: " + rowsCountDeleted);	
			
			conn.close();	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally
		
		{
			
			try {
			
				if(stmt!=null)
				{stmt.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				
				if(conn!=null)
				{conn.close();}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
		return rowsCountDeleted;
	}
	
	
	
	
	
	public OrderItemEndPoint getOrderItem(Integer orderID,
                                             Integer itemID,
                                             String searchString,
                                             String sortBy,
                                             Integer limit, Integer offset)
	{



		String query = "SELECT "

				+ OrderItem.TABLE_NAME + "." + OrderItem.ORDER_ID + ","
				+ OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID + ","
				+ OrderItem.TABLE_NAME + "." + OrderItem.ITEM_PRICE_AT_ORDER + ","
				+ OrderItem.TABLE_NAME + "." + OrderItem.ITEM_QUANTITY + ","

				+ Item.TABLE_NAME + "." + Item.ITEM_ID + ","
				+ Item.TABLE_NAME + "." + Item.QUANTITY_UNIT + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_NAME + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_IMAGE_URL + ""

				+ " FROM " + OrderItem.TABLE_NAME
				+ " LEFT OUTER JOIN " + Item.TABLE_NAME + " ON ( " + OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID + " ) "
				+ " WHERE TRUE ";



		if(orderID != null)
		{
			query = query + " AND " + OrderItem.ORDER_ID + " = " + orderID;
		}




		if(itemID != null)
		{
			query = query + " AND " + OrderItem.ITEM_ID + " = " + itemID;
		}




		if(sortBy!=null && !sortBy.equals(""))
		{
			query = query + " ORDER BY " + sortBy;
		}





		if(limit != null)
		{
			query = query + " LIMIT " + limit + " " + " OFFSET " + offset;
		}



		ArrayList<OrderItem> orderItemList = new ArrayList<>();

		OrderItemEndPoint endPoint = new OrderItemEndPoint();


		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try {
			
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			
			rs = statement.executeQuery(query);
			
			while(rs.next())
			{

				OrderItem orderItem = new OrderItem();

				orderItem.setOrderID(rs.getInt(OrderItem.ORDER_ID));
				orderItem.setItemID(rs.getInt(OrderItem.ITEM_ID));
				orderItem.setItemPriceAtOrder(rs.getDouble(OrderItem.ITEM_PRICE_AT_ORDER));
				orderItem.setItemQuantity(rs.getDouble(OrderItem.ITEM_QUANTITY));


				if(itemID==null)
				{
					Item item = new Item();

					item.setItemID(rs.getInt(Item.ITEM_ID));
					item.setItemName(rs.getString(Item.ITEM_NAME));
					item.setQuantityUnit(rs.getString(Item.QUANTITY_UNIT));
					item.setItemImageURL(rs.getString(Item.ITEM_IMAGE_URL));




//					item.setItemDescription(rs.getString(Item.ITEM_DESC));
//					item.setItemCategoryID(rs.getInt(Item.ITEM_CATEGORY_ID));
//					item.setItemDescriptionLong(rs.getString(Item.ITEM_DESCRIPTION_LONG));
//					item.setDateTimeCreated(rs.getTimestamp(Item.DATE_TIME_CREATED));


					orderItem.setItem(item);
				}




				orderItemList.add(orderItem);

			}


			endPoint.setResults(orderItemList);
			

			
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



}
