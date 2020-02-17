//package org.nearbyshops.RESTEndpointRoles.Deprecated;
//
//import org.nearbyshops.Globals.GlobalConstants;
//import org.nearbyshops.Globals.Globals;
//import org.nearbyshops.Globals.SendSMS;
//import org.nearbyshops.ModelEndpoint.OrderEndPoint;
//import org.nearbyshops.ModelRoles.DeliveryGuyData;
//import org.nearbyshops.ModelRoles.Endpoints.UserEndpoint;
//import org.nearbyshops.ModelRoles.ShopStaffPermissions;
//import org.nearbyshops.ModelRoles.User;
//import org.simplejavamail.email.Email;
//import org.simplejavamail.email.EmailBuilder;
//
//import javax.annotation.security.RolesAllowed;
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import static org.nearbyshops.Globals.Globals.*;
//import static org.nearbyshops.Globals.Globals.daoDeliveryGuy;
//
//public class DeprecatedRESTMethodsRoles {
//
//
//
//
//
//    @PUT
//    @Path("/UpdateProfileShopAdmin")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
//    public Response updateProfileShopAdmin(User user)
//    {
////        /{UserID}
////        , @PathParam("UserID")int userID
//
//        user.setUserID(((User)Globals.accountApproved).getUserID());
//        int rowCount = daoUser.updateShopAdmin(user);
//
//
//        if(rowCount >= 1)
//        {
//
//            return Response.status(Response.Status.OK)
//                    .build();
//        }
//        if(rowCount == 0)
//        {
//
//            return Response.status(Response.Status.NOT_MODIFIED)
//                    .build();
//        }
//
//        return null;
//    }
//
//
//
//
//
//    /*
//    * UserLoginRESTEndpoint Ends
//    *
//    * */
//
//
//
//    /*
//    * Staff Login Begins
//    * */
//
//
//
//    @GET
//    @Path("/GetStaffForAdmin")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_ADMIN})
//    public Response getStaffForAdmin(
//            @QueryParam("latCurrent") Double latPickUp, @QueryParam("lonCurrent") Double lonPickUp,
//            @QueryParam("PermitProfileUpdate") Boolean permitProfileUpdate,
//            @QueryParam("PermitRegistrationAndRenewal") Boolean permitRegistrationAndRenewal,
//            @QueryParam("Gender") Boolean gender,
//            @QueryParam("SortBy") String sortBy,
//            @QueryParam("Limit")Integer limit, @QueryParam("Offset")Integer offset,
//            @QueryParam("GetRowCount")boolean getRowCount,
//            @QueryParam("MetadataOnly")boolean getOnlyMetaData)
//    {
//
//
//        if(limit!=null)
//        {
//            if(limit >= GlobalConstants.max_limit)
//            {
//                limit = GlobalConstants.max_limit;
//            }
//
//            if(offset==null)
//            {
//                offset = 0;
//            }
//        }
//        else
//        {
//            limit = GlobalConstants.max_limit;
//        }
//
//
//
//        UserEndpoint endPoint = daoStaff.getStaffForAdmin(
//                latPickUp,lonPickUp,
//                permitProfileUpdate,permitRegistrationAndRenewal,
//                gender,
//                sortBy,limit,offset,
//                getRowCount,getOnlyMetaData
//        );
//
//
//
//        endPoint.setLimit(limit);
//        endPoint.setOffset(offset);
//        endPoint.setMax_limit(GlobalConstants.max_limit);
//
//
//        //Marker
//        return Response.status(Response.Status.OK)
//                .entity(endPoint)
//                .build();
//    }
//
//
//
//
//    @GET
//    @Path("/GetStaffListPublic")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getStaffListPublic(
//            @QueryParam("latCurrent") Double latPickUp, @QueryParam("lonCurrent") Double lonPickUp,
//            @QueryParam("PermitProfileUpdate") Boolean permitProfileUpdate,
//            @QueryParam("PermitRegistrationAndRenewal") Boolean permitRegistrationAndRenewal,
//            @QueryParam("PermitAcceptPayments") Boolean permitAcceptPayments,
//            @QueryParam("Gender") Boolean gender,
//            @QueryParam("SortBy") String sortBy,
//            @QueryParam("Limit")Integer limit, @QueryParam("Offset")Integer offset,
//            @QueryParam("GetRowCount")boolean getRowCount,
//            @QueryParam("MetadataOnly")boolean getOnlyMetaData)
//    {
//
//
//        if(limit!=null)
//        {
//            if(limit >= GlobalConstants.max_limit)
//            {
//                limit = GlobalConstants.max_limit;
//            }
//
//            if(offset==null)
//            {
//                offset = 0;
//            }
//        }
//
//
//
//        UserEndpoint endPoint = daoStaff.getStaffListPublic(
//                latPickUp,lonPickUp,
//                permitProfileUpdate,
//                permitRegistrationAndRenewal,
//                gender,
//                sortBy,limit,offset,
//                getRowCount,getOnlyMetaData
//        );
//
//
//
//
//
//        if(limit!=null)
//        {
//            endPoint.setLimit(limit);
//            endPoint.setOffset(offset);
//            endPoint.setMax_limit(GlobalConstants.max_limit);
//        }
//
//
//
//
//        //Marker
//        return Response.status(Response.Status.OK)
//                .entity(endPoint)
//                .build();
//    }
//
//
//
//
//    @PUT
//    @Path("/UpdateProfileStaff")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_STAFF,GlobalConstants.ROLE_ADMIN})
//    public Response updateProfileStaff(User user)
//    {
////        /{UserID}
////        @PathParam("UserID")int userID
//
//        user.setUserID(((User) Globals.accountApproved).getUserID());
//        int rowCount = daoStaff.updateStaffProfile(user);
//
//        if(rowCount >= 1)
//        {
//
//            return Response.status(Response.Status.OK)
//                    .build();
//        }
//        if(rowCount == 0)
//        {
//
//            return Response.status(Response.Status.NOT_MODIFIED)
//                    .build();
//        }
//
//        return null;
//    }
//
//
//
//
//    @PUT
//    @Path("/{UserID}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_ADMIN})
//    public Response updateStaffByAdmin(User user, @PathParam("UserID")int userID)
//    {
//
//        user.setUserID(userID);
//        int rowCount = daoStaff.updateStaffByAdmin(user);
//
//
//        if(rowCount >= 1)
//        {
//
//            return Response.status(Response.Status.OK)
//                    .build();
//        }
//        if(rowCount == 0)
//        {
//
//            return Response.status(Response.Status.NOT_MODIFIED)
//                    .build();
//        }
//
//        return null;
//    }
//
//
//
//
//
//
//    /*
//    * StaffLoginRESTEndpoint Ends
//    *
//    * */
//
//
//
//
//    /*
//     * Shop Staff Login Rest Endpoint Ends
//     * */
//
//
//
//
//    @GET
//    @Path("/GetShopStaffForShopAdmin")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
//    public Response getShopStaffForShopAdmin(
//            @QueryParam("latCurrent") Double latPickUp, @QueryParam("lonCurrent") Double lonPickUp,
//            @QueryParam("PermitProfileUpdate") Boolean permitProfileUpdate,
//            @QueryParam("PermitRegistrationAndRenewal") Boolean permitRegistrationAndRenewal,
//            @QueryParam("Gender") Boolean gender,
//            @QueryParam("SortBy") String sortBy,
//            @QueryParam("Limit")Integer limit, @QueryParam("Offset")Integer offset,
//            @QueryParam("GetRowCount")boolean getRowCount,
//            @QueryParam("MetadataOnly")boolean getOnlyMetaData)
//    {
//
//
//        User user = (User) Globals.accountApproved;
//
//        int shopID = Globals.shopDAO.getShopIDForShopAdmin(user.getUserID()).getShopID();
//
////        System.out.println("Get Shop Staff !");
//
//
//        if(limit!=null)
//        {
//            if(limit >= GlobalConstants.max_limit)
//            {
//                limit = GlobalConstants.max_limit;
//            }
//
//            if(offset==null)
//            {
//                offset = 0;
//            }
//        }
//        else
//        {
//            limit = GlobalConstants.max_limit;
//        }
//
//
//
//        UserEndpoint endPoint = daoShopStaff.getShopStaffForShopAdmin(
//                latPickUp,lonPickUp,
//                permitProfileUpdate,permitRegistrationAndRenewal,
//                gender,
//                shopID,
//                sortBy,limit,offset,
//                getRowCount,getOnlyMetaData
//        );
//
//
//
//        endPoint.setLimit(limit);
//        endPoint.setOffset(offset);
//        endPoint.setMax_limit(GlobalConstants.max_limit);
//
//
//        //Marker
//        return Response.status(Response.Status.OK)
//                .entity(endPoint)
//                .build();
//    }
//
//
//
//
//
//
//    @GET
//    @Path("/GetShopStaffListPublic")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getStaffListPublic(
//            @QueryParam("latCurrent") Double latPickUp, @QueryParam("lonCurrent") Double lonPickUp,
//            @QueryParam("PermitProfileUpdate") Boolean permitProfileUpdate,
//            @QueryParam("PermitRegistrationAndRenewal") Boolean permitRegistrationAndRenewal,
//            @QueryParam("PermitAcceptPayments") Boolean permitAcceptPayments,
//            @QueryParam("Gender") Boolean gender,
//            @QueryParam("SortBy") String sortBy,
//            @QueryParam("Limit")Integer limit, @QueryParam("Offset")Integer offset,
//            @QueryParam("GetRowCount")boolean getRowCount,
//            @QueryParam("MetadataOnly")boolean getOnlyMetaData)
//    {
//
//
//        if(limit!=null)
//        {
//            if(limit >= GlobalConstants.max_limit)
//            {
//                limit = GlobalConstants.max_limit;
//            }
//
//            if(offset==null)
//            {
//                offset = 0;
//            }
//        }
//
//
//
//        UserEndpoint endPoint = daoShopStaff.getShopStaffListPublic(
//                latPickUp,lonPickUp,
//                permitProfileUpdate,
//                permitRegistrationAndRenewal,
//                gender,
//                sortBy,limit,offset,
//                getRowCount,getOnlyMetaData
//        );
//
//
//
//
//
//        if(limit!=null)
//        {
//            endPoint.setLimit(limit);
//            endPoint.setOffset(offset);
//            endPoint.setMax_limit(GlobalConstants.max_limit);
//        }
//
//
//
//
//        //Marker
//        return Response.status(Response.Status.OK)
//                .entity(endPoint)
//                .build();
//    }
//
//
//
//
//    @PUT
//    @Path("/UpdateProfileStaff")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_SHOP_STAFF})
//    public Response updateProfileStaff(User user)
//    {
////        /{UserID}
////        @PathParam("UserID")int userID
//
//        user.setUserID(((User) Globals.accountApproved).getUserID());
//        int rowCount = daoShopStaff.updateShopStaffProfile(user);
//
//
//        if(rowCount >= 1)
//        {
//
//            return Response.status(Response.Status.OK)
//                    .build();
//        }
//        if(rowCount == 0)
//        {
//
//            return Response.status(Response.Status.NOT_MODIFIED)
//                    .build();
//        }
//
//        return null;
//    }
//
//
//
//
//    @PUT
//    @Path("/{UserID}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
//    public Response updateStaffByAdmin(User user, @PathParam("UserID")int userID)
//    {
//
////        user.setUserID(userID);
//        int rowCount = daoShopStaff.updateShopStaffByAdmin(user);
//
//
//        if(rowCount >= 1)
//        {
//
//            return Response.status(Response.Status.OK)
//                    .build();
//        }
//        if(rowCount == 0)
//        {
//
//            return Response.status(Response.Status.NOT_MODIFIED)
//                    .build();
//        }
//
//        return null;
//    }
//
//
//
//
//    /*
//    * Shop Staff Login Rest Endpoint Ends
//    * */
//
//
//
//    /*
//    * For DeliveryGuyLoginRESTEndpoint Begin
//    * */
//
//
//
//
//    @PUT
//    @Path("/UpdateProfileBySelf")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_DELIVERY_GUY_SELF,GlobalConstants.ROLE_DELIVERY_GUY})
//    public Response updateProfileBySelf(User user)
//    {
////        /{UserID}
////        @PathParam("UserID")int userID
//
//        user.setUserID(((User) Globals.accountApproved).getUserID());
//        int rowCount = daoDeliveryGuy.updateDeliveryGuyBySelf(user);
//
//
//        if(rowCount >= 1)
//        {
//
//            return Response.status(Response.Status.OK)
//                    .build();
//        }
//        if(rowCount == 0)
//        {
//
//            return Response.status(Response.Status.NOT_MODIFIED)
//                    .build();
//        }
//
//        return null;
//    }
//
//
//
//
//    @PUT
//    @Path("/{UserID}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
//    public Response updateDeliveryGuyByAdmin(User user, @PathParam("UserID")int userID)
//    {
//
////        user.setUserID(userID);
//        int rowCount = daoDeliveryGuy.updateDeliveryGuyByAdmin(user);
//
//
//        if(rowCount >= 1)
//        {
//
//            return Response.status(Response.Status.OK)
//                    .build();
//        }
//        if(rowCount == 0)
//        {
//
//            return Response.status(Response.Status.NOT_MODIFIED)
//                    .build();
//        }
//
//        return null;
//    }
//
//
//
//
//
//    @GET
//    @Path("/GetDeliveryGuyForShopAdmin")
//    @Produces(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN,GlobalConstants.ROLE_SHOP_STAFF})
//    public Response getDeliveryGuyForShopAdmin(
//            @QueryParam("latCurrent") Double latPickUp, @QueryParam("lonCurrent") Double lonPickUp,
//            @QueryParam("Gender") Boolean gender,
//            @QueryParam("SortBy") String sortBy,
//            @QueryParam("Limit")Integer limit, @QueryParam("Offset")Integer offset,
//            @QueryParam("GetRowCount")boolean getRowCount,
//            @QueryParam("MetadataOnly")boolean getOnlyMetaData)
//    {
//
//
//        User user = (User) Globals.accountApproved;
//        int shopID = 0;
//
//
//        if(user.getRole()==GlobalConstants.ROLE_SHOP_ADMIN_CODE)
//        {
//            shopID = daoShopStaff.getShopIDForShopAdmin(user.getUserID()).getShopID();
//        }
//        else if(user.getRole()==GlobalConstants.ROLE_SHOP_STAFF_CODE)
//        {
//            shopID = Globals.daoShopStaff.getShopIDforShopStaff(user.getUserID());
//        }
//
//
//
////        System.out.println("Get Shop Staff !");
//
//
//        if(limit!=null)
//        {
//            if(limit >= GlobalConstants.max_limit)
//            {
//                limit = GlobalConstants.max_limit;
//            }
//
//            if(offset==null)
//            {
//                offset = 0;
//            }
//        }
//        else
//        {
//            limit = GlobalConstants.max_limit;
//        }
//
//
//
//        UserEndpoint endPoint = daoDeliveryGuy.getDeliveryGuyForShopAdmin(
//                latPickUp,lonPickUp,
//                gender,
//                shopID,
//                sortBy,limit,offset,
//                getRowCount,getOnlyMetaData
//        );
//
//
//
//        endPoint.setLimit(limit);
//        endPoint.setOffset(offset);
//        endPoint.setMax_limit(GlobalConstants.max_limit);
//
//
//        //Marker
//        return Response.status(Response.Status.OK)
//                .entity(endPoint)
//                .build();
//    }
//
//
//
//
//
//
//
//    /*
//     * For DeliveryGuyLoginRESTEndpoint Begin
//     * */
//
//
//
//
//
//
//    @POST
//    @Path("/StaffRegistration")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_ADMIN})
//    public Response insertStaff(User user)
//    {
//        user.setRole(GlobalConstants.ROLE_STAFF_CODE);
//        return userRegistration(user,GlobalConstants.ROLE_STAFF_CODE);
//    }
//
//
//
//
//
//
//
//    @POST
//    @Path("/DeliveryGuySelfRegistration")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
//    public Response deliveryGuySelfRegistration(User user)
//    {
//        if(user==null)
//        {
//            throw new WebApplicationException();
//        }
//
//
//        User shopAdmin = ((User) Globals.accountApproved);
//
//        int shopID = Globals.shopDAO.getShopIDForShopAdmin(shopAdmin.getUserID()).getShopID();
//
//        user.setRole(GlobalConstants.ROLE_DELIVERY_GUY_SELF_CODE);
//
//        DeliveryGuyData deliveryGuyData = new DeliveryGuyData();
//        deliveryGuyData.setShopID(shopID);
//        deliveryGuyData.setEmployedByShop(true);
//        user.setRt_delivery_guy_data(deliveryGuyData);
//
////        ShopStaffPermissions permissions = new ShopStaffPermissions();
////        permissions.setShopID(shopID);
////        user.setRt_shop_staff_permissions(permissions);
//
//
//
//
//        int idOfInsertedRow =-1;
//
//
//
//        if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
//        {
//            idOfInsertedRow = Globals.daoUserSignUp.registerUsingEmailNoCredits(user,false);
//
//
////            System.out.println("Email : " + user.getEmail()
////                    + "\nPassword : " + user.getPassword()
////                    + "\nRegistration Mode : " + user.getRt_registration_mode()
////                    + "\nName : " + user.getName()
////                    + "\nInsert Count : " + idOfInsertedRow
////                    + "\nVerificationCode : " + user.getRt_email_verification_code()
////            );
//
//
//            if(idOfInsertedRow>=1)
//            {
//
//
//                String message = "<h2>Your account has been Created for your e-mail id : "+ user.getEmail() + ".</h2>"
//                        + "<p>You can now login with your email and password that you have provided. Thank you for creating your account.<p>";
//
//
////                Globals.sendEmail(user.getEmail(),user.getEmail(),"Registration successful for your account",message);
//
//
//
//                // registration successful therefore send email to notify the user
//                Email email = EmailBuilder.startingBlank()
//                        .from(GlobalConstants.EMAIL_SENDER_NAME, GlobalConstants.EMAIL_ADDRESS_FOR_SENDER)
//                        .to(user.getName(),user.getEmail())
//                        .withSubject("Registration successful for your account")
//                        .withHTMLText(message)
//                        .buildEmail();
//
//
//                getMailerInstance().sendMail(email,true);
//
//
//
//            }
//
//        }
//        else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
//        {
//            idOfInsertedRow = daoUser.registerUsingPhoneNoCredits(user,false);
//
//
////            System.out.println("Phone : " + user.getPhone()
////                    + "\nEmail : " + user.getEmail()
////                    + "\nPassword : " + user.getPassword()
////                    + "\nRegistration Mode : " + user.getRt_registration_mode()
////                    + "\nName : " + user.getName()
////                    + "\nInsert Count : " + idOfInsertedRow
////                    + "\nVerificationCode : " + user.getRt_phone_verification_code()
////            );
//
//            // send notification to the mobile number via SMS
//
//            if(idOfInsertedRow>=1)
//            {
//
//                SendSMS.sendSMS("Congratulations your account has been registered with " + GlobalConstants.service_name_for_sms_value,
//                        user.getPhone());
//            }
//
//        }
//
//
//        user.setUserID(idOfInsertedRow);
//
//
//        if(idOfInsertedRow >=1)
//        {
//
//            return Response.status(Response.Status.CREATED)
//                    .entity(user)
//                    .build();
//
//        }else {
//
//            return Response.status(Response.Status.NOT_MODIFIED)
//                    .build();
//        }
//
//
//    }
//
//
//
//
//
//    @POST
//    @Path("/DeliveryGuyRegistration")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_ADMIN})
//    public Response deliveryGuyRegistration(User user)
//    {
//        if(user==null)
//        {
//            throw new WebApplicationException();
//        }
//
//
//        user.setRole(GlobalConstants.ROLE_DELIVERY_GUY_CODE);
//
//        DeliveryGuyData deliveryGuyData = new DeliveryGuyData();
//        deliveryGuyData.setShopID(0);
//        deliveryGuyData.setEmployedByShop(false);
//        user.setRt_delivery_guy_data(deliveryGuyData);
//
//
//
//        int idOfInsertedRow =-1;
//
//
//
//        if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
//        {
//            idOfInsertedRow = Globals.daoUserSignUp.registerUsingEmailNoCredits(user,false);
//
//
////            System.out.println("Email : " + user.getEmail()
////                    + "\nPassword : " + user.getPassword()
////                    + "\nRegistration Mode : " + user.getRt_registration_mode()
////                    + "\nName : " + user.getName()
////                    + "\nInsert Count : " + idOfInsertedRow
////                    + "\nVerificationCode : " + user.getRt_email_verification_code()
////            );
//
//
//            if(idOfInsertedRow>=1)
//            {
//
//
//                String message = "<h2>Your account has been Created for your e-mail id : "+ user.getEmail() + ".</h2>"
//                        + "<p>You can now login with your email and password that you have provided. Thank you for creating your account.<p>";
//
//
////                Globals.sendEmail(user.getEmail(),user.getEmail(),"Registration successful for your account",message);
//
//
//
//                // registration successful therefore send email to notify the user
//                Email email = EmailBuilder.startingBlank()
//                        .from(GlobalConstants.EMAIL_SENDER_NAME, GlobalConstants.EMAIL_ADDRESS_FOR_SENDER)
//                        .to(user.getName(),user.getEmail())
//                        .withSubject("Registration successful for your account")
//                        .withHTMLText(message)
//                        .buildEmail();
//
//
//                getMailerInstance().sendMail(email,true);
//
//
//
//            }
//
//
//        }
//        else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
//        {
//            idOfInsertedRow = daoUser.registerUsingPhoneNoCredits(user,false);
//
//
//
//            System.out.println("Phone : " + user.getPhone()
//                    + "\nEmail : " + user.getEmail()
//                    + "\nPassword : " + user.getPassword()
//                    + "\nRegistration Mode : " + user.getRt_registration_mode()
//                    + "\nName : " + user.getName()
//                    + "\nInsert Count : " + idOfInsertedRow
//                    + "\nVerificationCode : " + user.getRt_phone_verification_code()
//            );
//
//            // send notification to the mobile number via SMS
//
//            if(idOfInsertedRow>=1)
//            {
//
//                SendSMS.sendSMS("Congratulations your account has been registered with Nearby Shops.",
//                        user.getPhone());
//            }
//
//        }
//
//
//
//
//        user.setUserID(idOfInsertedRow);
//
//
//        if(idOfInsertedRow >=1)
//        {
//
//            return Response.status(Response.Status.CREATED)
//                    .entity(user)
//                    .build();
//
//
//        }else {
//
//            return Response.status(Response.Status.NOT_MODIFIED)
//                    .build();
//        }
//
//
//    }
//
//
//
//
//
//    @POST
//    @Path("/ShopStaffRegistration")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @RolesAllowed({GlobalConstants.ROLE_SHOP_ADMIN})
//    public Response shopStaffRegistration(User user)
//    {
//
//        if(user==null)
//        {
//            throw new WebApplicationException();
//        }
//
//
//        User shopAdmin = ((User) Globals.accountApproved);
//
//        int shopID = Globals.shopDAO.getShopIDForShopAdmin(shopAdmin.getUserID()).getShopID();
//
//        user.setRole(GlobalConstants.ROLE_SHOP_STAFF_CODE);
//
//        ShopStaffPermissions permissions = new ShopStaffPermissions();
//        permissions.setShopID(shopID);
//        user.setRt_shop_staff_permissions(permissions);
//
//
//
//
//        int idOfInsertedRow =-1;
//
//
//
//        if(user.getRt_registration_mode()==User.REGISTRATION_MODE_EMAIL)
//        {
//            idOfInsertedRow = Globals.daoUserSignUp.registerUsingEmailNoCredits(user,false);
//
//
////            System.out.println("Email : " + user.getEmail()
////                    + "\nPassword : " + user.getPassword()
////                    + "\nRegistration Mode : " + user.getRt_registration_mode()
////                    + "\nName : " + user.getName()
////                    + "\nInsert Count : " + idOfInsertedRow
////                    + "\nVerificationCode : " + user.getRt_email_verification_code()
////            );
//
//
//            if(idOfInsertedRow>=1)
//            {
//
//
//                String message = "<h2>Your account has been Created for your e-mail id : "+ user.getEmail() + ".</h2>"
//                        + "<p>You can now login with your email and password that you have provided. Thank you for creating your account.<p>";
//
//
////                Globals.sendEmail(user.getEmail(),user.getEmail(),"Registration successful for your account",message);
//
//
//
//                // registration successful therefore send email to notify the user
//                Email email = EmailBuilder.startingBlank()
//                        .from(GlobalConstants.EMAIL_SENDER_NAME, GlobalConstants.EMAIL_ADDRESS_FOR_SENDER)
//                        .to(user.getName(),user.getEmail())
//                        .withSubject("Registration successful for your account")
//                        .withHTMLText(message)
//                        .buildEmail();
//
//
//                getMailerInstance().sendMail(email,true);
//
//
//
//            }
//
//
//        }
//        else if(user.getRt_registration_mode()==User.REGISTRATION_MODE_PHONE)
//        {
//            idOfInsertedRow = daoUser.registerUsingPhoneNoCredits(user,false);
//
//
////            System.out.println("Phone : " + user.getPhone()
////                    + "\nEmail : " + user.getEmail()
////                    + "\nPassword : " + user.getPassword()
////                    + "\nRegistration Mode : " + user.getRt_registration_mode()
////                    + "\nName : " + user.getName()
////                    + "\nInsert Count : " + idOfInsertedRow
////                    + "\nVerificationCode : " + user.getRt_phone_verification_code()
////            );
//
//            // send notification to the mobile number via SMS
//
//            if(idOfInsertedRow>=1)
//            {
//
//                SendSMS.sendSMS("Congratulations your account has been registered with Nearby Shops.",
//                        user.getPhone());
//            }
//
//        }
//
//
//        user.setUserID(idOfInsertedRow);
//
//
//        if(idOfInsertedRow >=1)
//        {
//
//            return Response.status(Response.Status.CREATED)
//                    .entity(user)
//                    .build();
//
//
//        }else {
//
//            return Response.status(Response.Status.NOT_MODIFIED)
//                    .build();
//        }
//
//
//    }
//
//
//
//}
