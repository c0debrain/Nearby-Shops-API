package org.nearbyshops.RESTEndpoints.RESTEndpointRoles;


import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.Model.ModelRoles.ShopStaffPermissions;
import org.nearbyshops.Model.ModelRoles.StaffPermissions;
import org.nearbyshops.Model.ModelRoles.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.nearbyshops.Globals.Globals.*;
import static org.nearbyshops.Globals.Globals.daoShopStaff;

/**
 * Created by sumeet on 30/8/17.
 */


@Path("/api/v1/User/StaffLogin")
public class StaffLoginRESTEndpoint {




    @PUT
    @Path("/UpdateStaffLocation")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({GlobalConstants.ROLE_STAFF})
    public Response updateStaffLocation(StaffPermissions permissions)
    {

        permissions.setStaffUserID(((User)Globals.accountApproved).getUserID());
        int rowCount = daoStaff.updateStaffLocation(permissions);


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
    @Path("/UpgradeUser/{emailorphone}/{SecretCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({GlobalConstants.ROLE_ADMIN})
    public Response upgradeUserToShopStaff(@PathParam("emailorphone")String emailorphone,
                                           @PathParam("SecretCode")int secretCode)
    {

        int userID = daoUserUtility.getUserID(emailorphone);

        int rowCount = daoStaff.upgradeUserToStaff(userID,secretCode,GlobalConstants.ROLE_STAFF_CODE);


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
    @RolesAllowed({GlobalConstants.ROLE_ADMIN})
    public Response updateStaffPermissions(StaffPermissions permissions)
    {

        int rowCount = daoStaff.updateStaffPermissions(permissions);


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





    @GET
    @Path("/GetStaffPermissions/{UserID}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({GlobalConstants.ROLE_ADMIN})
    public Response getPermissionDetails(@PathParam("UserID")int userID)
    {

        StaffPermissions permissions = daoStaff.getStaffPermissions(userID);



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
