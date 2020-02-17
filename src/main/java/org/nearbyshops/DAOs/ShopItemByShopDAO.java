package org.nearbyshops.DAOs;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.*;
import org.nearbyshops.Model.ModelEndpoint.ShopItemEndPoint;
import org.nearbyshops.Model.ModelReviewItem.ItemReview;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ShopItemByShopDAO {


	private HikariDataSource dataSource = Globals.getDataSource();



	public ShopItemEndPoint getShopItems(Integer itemCategoryID,
											Integer shopID,
											Double latCenter, Double lonCenter,
											Double deliveryRangeMin, Double deliveryRangeMax,
											Double proximity,
											Integer endUserID, Boolean isFilledCart,
											Boolean isOutOfStock, Boolean priceEqualsZero,
											Boolean shopEnabled,
											String searchString,
											String sortBy,
											int limit, int offset,
											boolean getRowCount,
											boolean getOnlyMetadata

	)
	{

		String queryCount = "";


		String queryJoin = "SELECT "

					+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.AVAILABLE_ITEM_QUANTITY + ","

//					+ ShopItem.TABLE_NAME + "." + ShopItem.EXTRA_DELIVERY_CHARGE + ","
//					+ ShopItem.TABLE_NAME + "." + ShopItem.DATE_TIME_ADDED + ","
//					+ ShopItem.TABLE_NAME + "." + ShopItem.LAST_UPDATE_DATE_TIME + ","


//					+ Item.TABLE_NAME + "." + Item.ITEM_ID + ","
					+ Item.TABLE_NAME + "." + Item.ITEM_IMAGE_URL + ","
					+ Item.TABLE_NAME + "." + Item.ITEM_NAME + ","
					+ Item.TABLE_NAME + "." + Item.QUANTITY_UNIT + ","

//					+ Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + ","
//					+ Item.TABLE_NAME + "." + Item.ITEM_DESC + ","
//					+ Item.TABLE_NAME + "." + Item.DATE_TIME_CREATED + ","
//					+ Item.TABLE_NAME + "." + Item.ITEM_DESCRIPTION_LONG + ","


					+  "avg(" + ItemReview.TABLE_NAME + "." + ItemReview.RATING + ") as avg_rating" + ","
					+  "count( DISTINCT " + ItemReview.TABLE_NAME + "." + ItemReview.END_USER_ID + ") as rating_count" + ","
					+  "(avg(" + ItemReview.TABLE_NAME + "." + ItemReview.RATING + ")* count( DISTINCT " + ItemReview.TABLE_NAME + "." + ItemReview.END_USER_ID + ") ) as popularity" + ""

					+ " FROM " + Shop.TABLE_NAME
					+ " INNER JOIN " + ShopItem.TABLE_NAME + " ON ( " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + "=" + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + ") "
					+ " INNER JOIN " + Item.TABLE_NAME + " ON ( " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + "=" + Item.TABLE_NAME + "." + Item.ITEM_ID + ") "
					+ " LEFT OUTER JOIN " + ItemCategory.TABLE_NAME + " ON ( " + Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + "=" + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID + ") "
					+ " LEFT OUTER JOIN " + ItemReview.TABLE_NAME + " ON (" + ItemReview.TABLE_NAME + "." + ItemReview.ITEM_ID + " = " + Item.TABLE_NAME + "." + Item.ITEM_ID + ")"
					+ " WHERE TRUE " ;




		if(shopEnabled!=null && shopEnabled)
		{
			queryJoin = queryJoin + " AND " + Shop.TABLE_NAME + "." + Shop.IS_OPEN + " = TRUE "
						+ " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ENABLED + " = TRUE "
						+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + " > 0 "
						+ " AND " + Shop.TABLE_NAME + "." + Shop.ACCOUNT_BALANCE + ">=" + GlobalConstants.min_account_balance_for_shop;
		}



		if(endUserID!=null)
		{

			if(isFilledCart!=null)
			{
				if(isFilledCart)
				{
					queryJoin = queryJoin + " AND "
							+ ShopItem.TABLE_NAME
							+ "."
							+ ShopItem.SHOP_ID + " IN "
							+ " (SELECT " + Cart.SHOP_ID + " FROM " + Cart.TABLE_NAME + " WHERE "
							+ Cart.END_USER_ID + " = " + endUserID + ")";
				}else
				{
					queryJoin = queryJoin + " AND "
							+ ShopItem.TABLE_NAME
							+ "."
							+ ShopItem.SHOP_ID + " NOT IN "
							+ " (SELECT " + Cart.SHOP_ID + " FROM " + Cart.TABLE_NAME + " WHERE "
							+ Cart.END_USER_ID + " = " + endUserID + ")";

				}

			}
		}




		if(shopID !=null)
		{
				queryJoin = queryJoin + " AND " + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " = " + shopID;
		}



		if(searchString !=null)
		{
			String queryPartSearch = " ( " + Item.TABLE_NAME + "." + Item.ITEM_DESC +" ilike '%" + searchString + "%'"
					+ " or " + Item.TABLE_NAME + "." + Item.ITEM_DESCRIPTION_LONG + " ilike '%" + searchString + "%'"
					+ " or " + Item.TABLE_NAME + "." + Item.ITEM_NAME + " ilike '%" + searchString + "%'" + ") ";

			queryJoin = queryJoin + " AND " + queryPartSearch;
		}



		if(itemCategoryID !=null)
		{

			queryJoin = queryJoin + " AND "
					+ ItemCategory.TABLE_NAME
					+ "."
					+ ItemCategory.ITEM_CATEGORY_ID + " = " + itemCategoryID;
		}


		if(priceEqualsZero!=null && priceEqualsZero)
		{
			queryJoin = queryJoin + " AND "
					+ ShopItem.TABLE_NAME  + "." + ShopItem.ITEM_PRICE + " = " + 0;
		}




		if(isOutOfStock!=null && isOutOfStock)
		{
			queryJoin = queryJoin + " AND "
					+ ShopItem.TABLE_NAME  + "." + ShopItem.AVAILABLE_ITEM_QUANTITY + " = " + 0;
		}



		/*
				Applying Filters
		 */



		if(latCenter != null && lonCenter != null)
		{
			// Applying shop visibility filter. Gives all the shops which are visible at the given location defined by
			// latCenter and lonCenter. For more information see the API documentation.


			queryJoin = queryJoin + " AND " + " (6371.01 * acos(cos( radians("
					+ latCenter + ")) * cos( radians(" + Shop.LAT_CENTER
					+ " )) * cos(radians( " + Shop.LON_CENTER + ") - radians("
					+ lonCenter + "))" + " + sin( radians(" + latCenter
					+ ")) * sin(radians(" + Shop.LAT_CENTER + ")))) <= " + Shop.DELIVERY_RANGE;
		}




		if(deliveryRangeMin !=null && deliveryRangeMax!=null){

			// apply delivery range filter
			queryJoin = queryJoin + " AND " + Shop.TABLE_NAME + "." + Shop.DELIVERY_RANGE
					+ " BETWEEN " + deliveryRangeMin + " AND " + deliveryRangeMax;
		}





		// proximity cannot be greater than the delivery range if the delivery range is supplied.
		if(proximity !=null)
		{
			// filter using Haversine formula
			queryJoin = queryJoin + " AND " + " (6371.01 * acos(cos( radians("
					+ latCenter + ")) * cos( radians(" + Shop.LAT_CENTER
					+ " )) * cos(radians( " + Shop.LON_CENTER + ") - radians(" + lonCenter
					+ "))" + " + sin( radians(" + latCenter + ")) * sin(radians("
					+ Shop.LAT_CENTER + ")))) <= " + proximity ;
		}




		queryJoin = queryJoin

				+ " group by "
				+ ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ITEM_ID + ","
				+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + ","
				+ ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + ","
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
				Applying Filters Ends
		 */




		queryCount = "SELECT COUNT(*) as item_count FROM (" + queryCount + ") AS temp";


		ShopItemEndPoint endPoint = new ShopItemEndPoint();





		ArrayList<ShopItem> shopItemList = new ArrayList<>();


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

					ShopItem shopItem = new ShopItem();
					shopItem.setShopID(rs.getInt(ShopItem.SHOP_ID));
					shopItem.setItemID(rs.getInt(ShopItem.ITEM_ID));
					shopItem.setAvailableItemQuantity(rs.getInt(ShopItem.AVAILABLE_ITEM_QUANTITY));
					shopItem.setItemPrice(rs.getDouble(ShopItem.ITEM_PRICE));

//					shopItem.setDateTimeAdded(rs.getTimestamp(ShopItem.DATE_TIME_ADDED));
//					shopItem.setLastUpdateDateTime(rs.getTimestamp(ShopItem.LAST_UPDATE_DATE_TIME));
//					shopItem.setExtraDeliveryCharge(rs.getInt(ShopItem.EXTRA_DELIVERY_CHARGE));

					Item item = new Item();

//					item.setItemID(rs.getInt(Item.ITEM_ID));
					item.setItemID(shopItem.getItemID());
					item.setItemName(rs.getString(Item.ITEM_NAME));
					item.setItemImageURL(rs.getString(Item.ITEM_IMAGE_URL));
					item.setQuantityUnit(rs.getString(Item.QUANTITY_UNIT));

//					item.setItemCategoryID(rs.getInt(Item.ITEM_CATEGORY_ID));
//					item.setItemDescriptionLong(rs.getString(Item.ITEM_DESCRIPTION_LONG));
//					item.setItemDescription(rs.getString(Item.ITEM_DESC));
//					item.setDateTimeCreated(rs.getTimestamp(Item.DATE_TIME_CREATED));

					item.setRt_rating_avg(rs.getFloat("avg_rating"));
					item.setRt_rating_count(rs.getFloat("rating_count"));


					shopItem.setItem(item);
					shopItemList.add(shopItem);

				}


				endPoint.setResults(shopItemList);
			}




			if(getRowCount)
			{
				statement = connection.createStatement();
				rs = statement.executeQuery(queryCount);

				while(rs.next())
				{
					endPoint.setItemCount(rs.getInt("item_count"));
				}
			}




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
