package org.nearbyshops.RESTEndpointRoles;


import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.ModelRoles.ShopStaffPermissions;
import org.nearbyshops.ModelRoles.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.nearbyshops.Globals.Globals.daoShopStaff;
import static org.nearbyshops.Globals.Globals.daoUserUtility;

/**
 * Created by sumeet on 30/8/17.
 */


@Path("/api/v1/User/ShopStaffLogin")
public class ShopStaffLoginRESTEndpoint {



    @PUT
    @Path("/UpdateStaffLocation")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN,GlobalConstants.ROLE_SHOP_STAFF})
    public Response updateStaffLocation(ShopStaffPermissions permissions)
    {

        permissions.setStaffUserID(((User)Globals.accountApproved).getUserID());
        int rowCount = daoShopStaff.updateShopStaffLocation(permissions);


        if(rowCount >= 1)
        {
            return Response.status(Response.Status.OK)
                    .build();
        }
        if(rowCount == 0)
        {

            return Response.status(Response.Status.NOT_MODIFIED)
                    .build();
        }

        return null;
    }




    @PUT
    @Path("/UpdateStaffPermissions")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
    public Response updateStaffPermissions(ShopStaffPermissions permissions)
    {

        int shopAdminID = ((User)Globals.accountApproved).getUserID();
        int shopID = daoUserUtility.getShopIDForShopAdmin(shopAdminID);
        permissions.setShopID(shopID);


        int rowCount = daoShopStaff.updateShopStaffPermissions(permissions);


        if(rowCount >= 1)
        {
            return Response.status(Response.Status.OK)
                    .build();
        }
        else if(rowCount == 0)
        {

            return Response.status(Response.Status.NOT_MODIFIED)
                    .build();
        }

        return null;
    }




    @PUT
    @Path("/UpgradeUser/{UserID}/{Role}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
    public Response upgradeUserToShopStaff(@PathParam("UserID")int userID,@PathParam("Role")int role)
    {

        int shopAdminID = ((User)Globals.accountApproved).getUserID();
        int shopID = daoUserUtility.getShopIDForShopAdmin(shopAdminID);


        int rowCount = daoShopStaff.upgradeUserToStaff(userID,shopID,0,role);


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




        if(rowCount >= 1)
        {
            return Response.status(Response.Status.OK)
                    .build();
        }
        if(rowCount == 0)
        {

            return Response.status(Response.Status.NOT_MODIFIED)
                    .build();
        }

        return null;
    }





    @GET
    @Path("/GetUserDetails/{UserID}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
    public Response getUserDetails(@PathParam("UserID")int userID)
    {

        ShopStaffPermissions permissions = daoShopStaff.getShopStaffPermissions(userID);


//        GsonBuilder gsonBuilder = new GsonBuilder();
//
//        Gson gson =  gsonBuilder
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//                .create();

//        System.out.println(gson.toJson(permissions));



        if(permissions!=null)
        {
            return Response.status(Response.Status.OK)
                    .entity(permissions)
                    .build();
        }
        else
        {
            return Response.status(Response.Status.NOT_MODIFIED)
                    .build();
        }

    }




}
