package org.nearbyshops.DAOs;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.Item;
import org.nearbyshops.Model.ShopItem;
import org.nearbyshops.Model.ModelEndpoint.ItemEndPoint;
import org.nearbyshops.Model.ModelReviewItem.ItemReview;
import org.nearbyshops.Model.ModelStats.ItemStats;

import java.sql.*;
import java.util.ArrayList;


public class ItemDAOJoinOuter {


	private HikariDataSource dataSource = Globals.getDataSource();



	public ItemEndPoint getItems(
					Integer itemCategoryID,
					Boolean parentIsNull,
					String searchString,
					String sortBy,
					int limit, int offset,
					boolean getRowCount,
					boolean getOnlyMetadata
	) {



		String queryCount = "";

		
		String queryJoin = "SELECT DISTINCT "
				+ "min(" + ShopItem.ITEM_PRICE + ") as min_price" + ","
				+ "max(" + ShopItem.ITEM_PRICE + ") as max_price" + ","
				+ "avg(" + ShopItem.ITEM_PRICE + ") as avg_price" + ","
				+ "count( DISTINCT " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + ") as shop_count" + ","

				+ Item.TABLE_NAME + "." + Item.ITEM_ID + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_NAME + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_IMAGE_URL + ","
				+ Item.TABLE_NAME + "." + Item.QUANTITY_UNIT + ","
				+ Item.TABLE_NAME + "." + Item.LIST_PRICE + ","

				+  "avg(" + ItemReview.TABLE_NAME + "." + ItemReview.RATING + ") as avg_rating" + ","
				+  "count( DISTINCT " + ItemReview.TABLE_NAME + "." + ItemReview.END_USER_ID + ") as rating_count" + ""

				+ " FROM " + Item.TABLE_NAME
				+ " LEFT OUTER JOIN " + ShopItem.TABLE_NAME + " ON (" + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + "=" + Item.TABLE_NAME + "." + Item.ITEM_ID + ") "
				+ " LEFT OUTER JOIN " + ItemReview.TABLE_NAME +  " ON (" + ItemReview.TABLE_NAME + "." + ItemReview.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID + ")" + ""
				+ " WHERE TRUE ";




		if(itemCategoryID != null)
		{
			queryJoin = queryJoin + " AND " + Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + " = " + itemCategoryID;
		}



		if(parentIsNull!=null&& parentIsNull)
		{
			queryJoin = queryJoin + " AND " + Item.ITEM_CATEGORY_ID + " IS NULL";
		}




		if(searchString !=null)
		{
			String queryPartSearch = " ( " + Item.TABLE_NAME + "." + Item.ITEM_DESC +" ilike '%" + searchString + "%'"
					+ " or " + Item.TABLE_NAME + "." + Item.ITEM_DESCRIPTION_LONG + " ilike '%" + searchString + "%'"
					+ " or " + Item.TABLE_NAME + "." + Item.ITEM_NAME + " ilike '%" + searchString + "%'" + ") ";

			queryJoin = queryJoin + " AND " + queryPartSearch;
		}




		// all the non-aggregate columns which are present in select must be present in group by also.
		queryJoin = queryJoin
				+ " group by "
				+ Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + ","
				+ Item.TABLE_NAME + "." + Item.ITEM_ID ;



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





		/*

		Applying filters Ends

		 */




		queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";


		ItemEndPoint endPoint = new ItemEndPoint();
		ArrayList<Item> itemList = new ArrayList<Item>();


		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			
			connection = dataSource.getConnection();


			if(!getOnlyMetadata)
			{

				statement = connection.prepareStatement(queryJoin);


				rs = statement.executeQuery();

				while(rs.next())
				{
					Item item = new Item();


					item.setItemID(rs.getInt(Item.ITEM_ID));
					item.setItemName(rs.getString(Item.ITEM_NAME));
					item.setItemImageURL(rs.getString(Item.ITEM_IMAGE_URL));
					item.setQuantityUnit(rs.getString(Item.QUANTITY_UNIT));
					item.setListPrice(rs.getFloat(Item.LIST_PRICE));


					ItemStats itemStats = new ItemStats();
					itemStats.setMax_price(rs.getDouble("max_price"));
					itemStats.setMin_price(rs.getDouble("min_price"));
					itemStats.setAvg_price(rs.getDouble("avg_price"));
					itemStats.setShopCount(rs.getInt("shop_count"));

					itemStats.setRating_avg(rs.getDouble("avg_rating"));
					itemStats.setRatingCount(rs.getInt("rating_count"));

					item.setItemStats(itemStats);

					itemList.add(item);
				}


				endPoint.setResults(itemList);

			}




			if(getRowCount)
			{
				statement = connection.prepareStatement(queryCount);

				rs = statement.executeQuery();

				while(rs.next())
				{
					endPoint.setItemCount(rs.getInt("item_count"));
				}
			}






//			System.out.println("Item By CategoryID " + itemList.size());
			
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
			

}
