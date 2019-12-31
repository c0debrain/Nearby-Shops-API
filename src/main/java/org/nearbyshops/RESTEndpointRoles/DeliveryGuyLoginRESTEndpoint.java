package org.nearbyshops.RESTEndpointRoles;


import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.ModelRoles.DeliveryGuyData;
import org.nearbyshops.ModelRoles.Endpoints.UserEndpoint;
import org.nearbyshops.ModelRoles.ShopStaffPermissions;
import org.nearbyshops.ModelRoles.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.nearbyshops.Globals.Globals.daoDeliveryGuy;
import static org.nearbyshops.Globals.Globals.daoShopStaff;

/**
 * Created by sumeet on 30/8/17.
 */


@Path("/api/v1/User/DeliveryGuy")
public class DeliveryGuyLoginRESTEndpoint {



    @PUT
    @Path("/UpdateLocation")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({GlobalConstants.ROLE_DELIVERY_GUY,GlobalConstants.ROLE_DELIVERY_GUY_SELF})
    public Response updateLocation(DeliveryGuyData deliveryGuyData)
    {

        deliveryGuyData.setStaffUserID(((User)Globals.accountApproved).getUserID());
        int rowCount = daoDeliveryGuy.updateDeliveryGuyLocation(deliveryGuyData);


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



}
