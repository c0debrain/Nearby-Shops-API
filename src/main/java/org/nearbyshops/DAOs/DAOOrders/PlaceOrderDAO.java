package org.nearbyshops.DAOs.DAOOrders;

import com.zaxxer.hikari.HikariDataSource;
import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.*;
import org.nearbyshops.Model.ModelStats.CartStats;

import java.sql.*;
import java.util.List;


/**
 * Created by sumeet on 7/6/16.
 */
public class PlaceOrderDAO {


    private HikariDataSource dataSource = Globals.getDataSource();


    public int placeOrderNew(Order order, int cartID) {

        Connection connection = null;
        PreparedStatement statement = null;



        Cart cart = Globals.cartService.readCart(cartID);
        List<CartStats> cartStats = Globals.cartStatsDAO.getCartStats(cart.getEndUserID(),cartID,cart.getShopID());
        Shop shop = Globals.daoOrderUtility.getShopDetailsForCreateOrder(cart.getShopID());
//        Shop shop = Globals.shopDAO.getShopDetails(cart.getShopID(),null,null);


        int orderID = -1;
        int copiedItemsCount = -1;
        int updatedItemsCount = -1;


        String copyCartToOrder = " insert into " + Order.TABLE_NAME
                + " ( "
                + Order.END_USER_ID + ","
                + Order.SHOP_ID + ","

                + " " + Order.STATUS_HOME_DELIVERY + ","
                + " " + Order.STATUS_PICK_FROM_SHOP + ","

//                + " " + Order.PAYMENT_RECEIVED + ","
//                + " " + Order.DELIVERY_RECEIVED + ","

                + " " + Order.ITEM_COUNT + ","
                + " " + Order.ITEM_TOTAL + ","
                + " " + Order.APP_SERVICE_CHARGE + ","
                + " " + Order.DELIVERY_CHARGES + ","
                + " " + Order.NET_PAYABLE + ","


                + " " + Order.DELIVERY_ADDRESS_ID + ","
                + Order.PICK_FROM_SHOP + ""
                + " ) " +
                " select "
                + Cart.END_USER_ID + ","
                + Cart.SHOP_ID + ","
                + " 1 " + ","
                + " 1 " + ","

//                + " false " + ","
//                + " false " + ","

                + " ? " + ","
                + " ? " + ","
                + " ? " + ","
                + " ? " + ","
                + " ? " + ","

                + " ? " + ","
                + " ? " + ""
                + " from " + Cart.TABLE_NAME
                + " where " + Cart.CART_ID + " = ?";




        String copyCartItemToOrderItem =

                "insert into " + OrderItem.TABLE_NAME +
                        " ("
                        + OrderItem.ORDER_ID  + ","
                        + OrderItem.ITEM_ID + ","
                        + OrderItem.ITEM_PRICE_AT_ORDER + ","
                        + OrderItem.ITEM_QUANTITY + ") " +


                        " select " + " ? " + ","
                        + ShopItem.TABLE_NAME+ "." + ShopItem.ITEM_ID + ","
                        + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_PRICE + ","
                        + CartItem.TABLE_NAME + "." + CartItem.ITEM_QUANTITY
                        + " from "
                        + CartItem.TABLE_NAME + ","
                        + Cart.TABLE_NAME + ","
                        + ShopItem.TABLE_NAME  +
                        " where "
                        + Cart.TABLE_NAME + "." + Cart.CART_ID + " = " + CartItem.TABLE_NAME + "." + CartItem.CART_ID +
                        " and "
                        + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " = " + Cart.TABLE_NAME + "." + Cart.SHOP_ID +
                        " and "
                        + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID + " = " + CartItem.TABLE_NAME + "." + CartItem.ITEM_ID +
                        " and "
                        + Cart.TABLE_NAME + "." + Cart.CART_ID + " = ? ";




        // reduce the item available quantity from the inventory
        String updateQuantity =
                        " Update " + ShopItem.TABLE_NAME +
                        " SET " +  ShopItem.AVAILABLE_ITEM_QUANTITY + " = " +  ShopItem.AVAILABLE_ITEM_QUANTITY + " - " +  OrderItem.ITEM_QUANTITY +
                        " from " +  OrderItem.TABLE_NAME + "," + Order.TABLE_NAME +
                        " where " + OrderItem.TABLE_NAME + "." + OrderItem.ITEM_ID + " = " + ShopItem.TABLE_NAME + "." + ShopItem.ITEM_ID +
                        " and " + Order.TABLE_NAME+ "." + Order.ORDER_ID + " = " + OrderItem.TABLE_NAME+ "."  + OrderItem.ORDER_ID +
                        " and " + ShopItem.TABLE_NAME + "." + ShopItem.SHOP_ID + " = " + Order.TABLE_NAME + "." + Order.SHOP_ID +
                        " and " + Order.TABLE_NAME + "." + Order.ORDER_ID + " = ?";


        String deleteCartItems = " DELETE FROM " + CartItem.TABLE_NAME +
                            " WHERE " + Cart.CART_ID + " = ?";


        String deleteCart = " DELETE FROM " + Cart.TABLE_NAME +
                                 " WHERE " + Cart.CART_ID + " = ?";




        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            int itemCount = 0;
            double itemTotal = 0;
            double appServiceCharge = 0;
            double deliveryCharges = 0;
            double netPayable = 0;



            // calculate and set different kind of charges
            if(cartStats.size()==1)
            {

                itemCount = cartStats.get(0).getItemsInCart();
                itemTotal = cartStats.get(0).getCart_Total();
//                    appServiceCharge = 10;

                if(order.isPickFromShop())
                {

                    deliveryCharges = 0;
                    appServiceCharge = GlobalConstants.app_service_charge_pick_for_shop_value;
                }
                else
                {


                    if(cartStats.get(0).getCart_Total() < shop.getBillAmountForFreeDelivery())
                    {
                        deliveryCharges = shop.getDeliveryCharges();
                    }
                    else
                    {
                        deliveryCharges = 0; // delivery free above this amount
                    }

                    appServiceCharge = GlobalConstants.app_service_charge_home_delivery_value;
                }

                netPayable = itemTotal + appServiceCharge + deliveryCharges;

            }



            statement = connection.prepareStatement(copyCartToOrder,PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setInt(1,itemCount); // item count
            statement.setDouble(2,itemTotal); // item total
            statement.setDouble(3,appServiceCharge); // app service charge
            statement.setDouble(4,deliveryCharges); // delivery charge
            statement.setDouble(5,netPayable); // net payable

            statement.setInt(6,order.getDeliveryAddressID());
            statement.setBoolean(7,order.isPickFromShop());
            statement.setInt(8,cartID);


            statement.executeUpdate();

            ResultSet rsCopyCartToOrder = statement.getGeneratedKeys();

            if(rsCopyCartToOrder.next())
            {
                orderID = rsCopyCartToOrder.getInt(1);
            }


            statement = connection.prepareStatement(copyCartItemToOrderItem,PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1,orderID);
            statement.setInt(2,cartID);
            copiedItemsCount = statement.executeUpdate();


            statement = connection.prepareStatement(updateQuantity,PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1,orderID);
            updatedItemsCount = statement.executeUpdate();



            statement = connection.prepareStatement(deleteCart,PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1,cartID);
            statement.executeUpdate();


            statement = connection.prepareStatement(deleteCartItems,PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1,cartID);
            statement.executeUpdate();



            connection.commit();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (connection != null) {
                try {

//                    rowIdUserID = -1;
                    orderID = -1;
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

        } finally {



            if (statement != null) {
                try {

                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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



        return orderID;
    }

}
