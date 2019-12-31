package org.nearbyshops.RESTEndpointRoles;


import org.nearbyshops.Globals.GlobalConstants;
import org.nearbyshops.Globals.Globals;
import org.nearbyshops.ModelRoles.Endpoints.UserEndpoint;
import org.nearbyshops.ModelRoles.StaffPermissions;
import org.nearbyshops.ModelRoles.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.nearbyshops.Globals.Globals.daoStaff;

/**
 * Created by sumeet on 30/8/17.
 */


@Path("/api/v1/User/StaffLogin")
public class StaffLoginRESTEndpoint {




    @PUT
    @Path("/UpdateStaffLocation")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({GlobalConstants.ROLE_ADMIN,GlobalConstants.ROLE_STAFF})
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



}
