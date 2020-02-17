package org.nearbyshops.DAOs;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.*;
import org.nearbyshops.Model.ModelEndpoint.ShopEndPoint;
import org.nearbyshops.Model.ModelEndpoint.ShopItemEndPoint;
import org.nearbyshops.Model.ModelReviewShop.ShopReview;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ShopItemByItemDAO {


	private HikariDataSource dataSource = Globals.getDataSource();



	public ShopItemEndPoint getShopItems(Integer itemCategoryID,
											Integer itemID,
											Double latCenter, Double lonCenter,
											Double deliveryRangeMin, Double deliveryRangeMax,
											Double proximity,
											Integer endUserID, Boolean isFilledCart,
											Boolean isOutOfStock, Boolean priceEqualsZero,
											String sortBy,
											int limit, int offset,
											boolean getRowCount,
											boolean getOnlyMetadata

	)
	{

			String queryCount = "";

			String queryJoin = "SELECT DISTINCT "
					+ "6371 * acos(cos( radians("
					+ latCenter + ")) * cos( radians( lat_center) ) * cos(radians( lon_center ) - radians("
					+ lonCenter + "))"
					+ " + sin( radians(" + latCenter + ")) * sin(radians(lat_center))) as distance" + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + ","
					+ ShopItem.TABLE_NAME + "." + ShopItem.AVAILABLE_ITEM_QUANTITY + ","

//					+ ShopItem.TABLE_NAME + "." + ShopItem.EXTRA_DELIVERY_CHARGE + ","
//					+ ShopItem.TABLE_NAME + "." + ShopItem.DATE_TIME_ADDED + ","
//					+ ShopItem.TABLE_NAME + "." + ShopItem.LAST_UPDATE_DATE_TIME + ","

					+  "avg(" + ShopReview.TABLE_NAME + "." + ShopReview.RATING + ") as avg_rating" + ","
					+  "count( DISTINCT " + ShopReview.TABLE_NAME + "." + ShopReview.END_USER_ID + ") as rating_count" + ","
					+  "(avg(" + ShopReview.TABLE_NAME + "." + ShopReview.RATING + ")* count( DISTINCT " + ShopReview.TABLE_NAME + "." + ShopReview.END_USER_ID + ") ) as popularity" + ","

					+ Shop.TABLE_NAME + "." + Shop.SHOP_ID + ","
					+ Shop.TABLE_NAME + "." + Shop.SHOP_NAME + ","
					+ Shop.TABLE_NAME + "." + Shop.DELIVERY_CHARGES + ","
					+ Shop.TABLE_NAME + "." + Shop.SHOP_ADDRESS + ","
					+ Shop.TABLE_NAME + "." + Shop.CITY + ","
					+ Shop.TABLE_NAME + "." + Shop.PICK_FROM_SHOP_AVAILABLE + ","
					+ Shop.TABLE_NAME + "." + Shop.HOME_DELIVERY_AVAILABLE + ","
					+ Shop.TABLE_NAME + "." + Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + ","
					+ Shop.TABLE_NAME + "." + Shop.LOGO_IMAGE_PATH + ""



//					+ Shop.TABLE_NAME + "." + Shop.LON_CENTER + ","
//					+ Shop.TABLE_NAME + "." + Shop.LAT_CENTER + ","
//					+ Shop.TABLE_NAME + "." + Shop.DELIVERY_RANGE + ","


//					+ Shop.TABLE_NAME + "." + Shop.PINCODE + ","
//					+ Shop.TABLE_NAME + "." + Shop.LANDMARK + ","



//					+ Shop.TABLE_NAME + "." + Shop.CUSTOMER_HELPLINE_NUMBER + ","
//					+ Shop.TABLE_NAME + "." + Shop.DELIVERY_HELPLINE_NUMBER + ","
//					+ Shop.TABLE_NAME + "." + Shop.SHORT_DESCRIPTION + ","
//					+ Shop.TABLE_NAME + "." + Shop.LONG_DESCRIPTION + ","
//					+ Shop.TABLE_NAME + "." + Shop.IS_OPEN + ","

//					+ Shop.TABLE_NAME + "." + Shop.TIMESTAMP_CREATED + ""


					+ " FROM " + ShopReview.TABLE_NAME
					+ " RIGHT OUTER JOIN " + Shop.TABLE_NAME
					+ " ON (" + ShopReview.TABLE_NAME + "." + ShopReview.SHOP_ID + " = " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + ")"
					+ ","+ ShopItem.TABLE_NAME + "," + Item.TABLE_NAME + "," + ItemCategory.TABLE_NAME
					+ " WHERE " + Shop.TABLE_NAME + "." + Shop.SHOP_ID + "=" + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID
					+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + "=" + Item.TABLE_NAME + "." + Item.ITEM_ID
					+ " AND " + Item.TABLE_NAME + "." + Item.ITEM_CATEGORY_ID + "=" + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID
					+ " AND " + Shop.TABLE_NAME + "." + Shop.IS_OPEN + " = TRUE "
					+ " AND " + Shop.TABLE_NAME + "." + Shop.SHOP_ENABLED + " = TRUE "
					+ " AND " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + " > 0 "
					+ " AND " + Shop.TABLE_NAME + "." + Shop.ACCOUNT_BALANCE + ">=" + GlobalConstants.min_account_balance_for_shop;






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



		if(itemID !=null)
		{
			queryJoin = queryJoin + " AND " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + " = " + itemID;
		}



		if(itemCategoryID !=null)
		{
			queryJoin = queryJoin + " AND " + ItemCategory.TABLE_NAME + "." + ItemCategory.ITEM_CATEGORY_ID + " = " + itemCategoryID;
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


			String queryPartlatLonCenter = "";

			queryPartlatLonCenter = queryPartlatLonCenter + " 6371.01 * acos( cos( radians("
					+ latCenter + ")) * cos( radians( lat_center) ) * cos(radians( lon_center ) - radians("
					+ lonCenter + "))"
					+ " + sin( radians(" + latCenter + ")) * sin(radians(lat_center))) <= delivery_range ";

			queryJoin = queryJoin + " AND " + queryPartlatLonCenter;
		}




		if(deliveryRangeMin !=null && deliveryRangeMax!=null){

			// apply delivery range filter
			queryJoin = queryJoin + " AND " + Shop.TABLE_NAME + "." + Shop.DELIVERY_RANGE + " BETWEEN " + deliveryRangeMin + " AND " + deliveryRangeMax;
		}




		// proximity cannot be greater than the delivery range if the delivery range is supplied. Otherwise this condition is
		// not required.
		if(proximity !=null)
		{
			// generate bounding coordinates for the shop based on the required location and its




			queryJoin = queryJoin + " AND " + " (6371.01 * acos(cos( radians("
					+ latCenter + ")) * cos( radians(" + Shop.LAT_CENTER + " )) * cos(radians( " + Shop.LON_CENTER
					+ ") - radians(" + lonCenter + "))"
					+ " + sin( radians(" + latCenter + ")) * sin(radians("
					+ Shop.LAT_CENTER + ")))) <= " + proximity ;;

		}




		String queryGroupBy = "";
		queryGroupBy = queryGroupBy + " group by "
				+ ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ITEM_ID + ","
				+ ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + ","
				+ ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + ","
				+ Shop.TABLE_NAME + "." + Shop.SHOP_ID ;

		queryJoin = queryJoin + queryGroupBy;





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

//						shopItem.setDateTimeAdded(rs.getTimestamp(ShopItem.DATE_TIME_ADDED));
//						shopItem.setLastUpdateDateTime(rs.getTimestamp(ShopItem.LAST_UPDATE_DATE_TIME));
//						shopItem.setExtraDeliveryCharge(rs.getInt(ShopItem.EXTRA_DELIVERY_CHARGE));


						Shop shop = new Shop();
						shop.setRt_distance(rs.getDouble("distance"));
						shop.setShopID(rs.getInt(Shop.SHOP_ID));
						shop.setShopName(rs.getString(Shop.SHOP_NAME));
						shop.setDeliveryCharges(rs.getFloat(Shop.DELIVERY_CHARGES));
						shop.setLogoImagePath(rs.getString(Shop.LOGO_IMAGE_PATH));
						shop.setShopAddress(rs.getString(Shop.SHOP_ADDRESS));
						shop.setCity(rs.getString(Shop.CITY));
						shop.setBillAmountForFreeDelivery(rs.getInt(Shop.BILL_AMOUNT_FOR_FREE_DELIVERY));
						shop.setHomeDeliveryAvailable(rs.getBoolean(Shop.HOME_DELIVERY_AVAILABLE));
						shop.setPickFromShopAvailable(rs.getBoolean(Shop.PICK_FROM_SHOP_AVAILABLE));


//						shop.setLatCenter(rs.getFloat(Shop.LAT_CENTER));
//						shop.setLonCenter(rs.getFloat(Shop.LON_CENTER));
//						shop.setPincode(rs.getLong(Shop.PINCODE));
//						shop.setLandmark(rs.getString(Shop.LANDMARK));
//						shop.setCustomerHelplineNumber(rs.getString(Shop.CUSTOMER_HELPLINE_NUMBER));
//						shop.setDeliveryHelplineNumber(rs.getString(Shop.DELIVERY_HELPLINE_NUMBER));
//						shop.setShortDescription(rs.getString(Shop.SHORT_DESCRIPTION));
//						shop.setLongDescription(rs.getString(Shop.LONG_DESCRIPTION));
//						shop.setTimestampCreated(rs.getTimestamp(Shop.TIMESTAMP_CREATED));
//						shop.setOpen(rs.getBoolean(Shop.IS_OPEN));

						shop.setRt_rating_avg(rs.getFloat("avg_rating"));
						shop.setRt_rating_count(rs.getFloat("rating_count"));

						shopItem.setShop(shop);
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
