package org.nearbyshops.ModelRoles;

import org.nearbyshops.ModelSettings.ServiceConfigurationLocal;

import java.sql.Timestamp;

/**
 * Created by sumeet on 29/5/16.
 */
public class UserPreferences {

    // constants
    public static final int REGISTRATION_MODE_EMAIL = 1;
    public static final int REGISTRATION_MODE_PHONE = 2;



    // Table Name for User
    public static final String TABLE_NAME = "USER_PREFERENCES";

    // Column names
    public static final String PREFERENCE_ID = "PREFERENCE_ID";
    public static final String USER_ID = "USER_ID";






    // Create Table CurrentServiceConfiguration Provider
    public static final String createTable =

            "CREATE TABLE IF NOT EXISTS "
                    + UserPreferences.TABLE_NAME + "("
                    + " " + UserPreferences.USER_ID + " SERIAL PRIMARY KEY,"

                    + ")";



//    + " UNIQUE (" + User.E_MAIL + "," + User.PHONE + ")"




    // Instance Variables
    private int userID;
    private String username;
    private String password;

    private String passwordResetCode;
    private Timestamp resetCodeExpires;

    private String email;
    private String phone;
    private String name;

    private Boolean gender;
    private String profileImagePath;
    private int role;

    private boolean isAccountPrivate;
    private String about;
    private String firebaseID;

    private Timestamp timestampCreated;
    private Timestamp timestampUpdated;

    private String token;
    private Timestamp timestampTokenExpires;

    private double taxAccountBalance;
    private double serviceAccountBalance;

    private double extendedCreditLimit;

    private int referredBy;
    private boolean isReferrerCredited;
    private boolean isVerified;


    private String rt_phone_country_code;

    private String rt_email_verification_code;
    private String rt_phone_verification_code;
    private int rt_registration_mode; // 1 for registration by email 2 for registration by phone
//    private Vehicle rt_vehicle;
    private StaffPermissions rt_staff_permissions;
    private ShopStaffPermissions rt_shop_staff_permissions;
    private DeliveryGuyData rt_delivery_guy_data;

    private UserPreferences userProfileGlobal;


    private String rt_oneSignalPlayerID;


    private ServiceConfigurationLocal serviceConfigurationLocal;









    // utility functions



    public String getPhoneWithCountryCode()
    {
        return (rt_phone_country_code + phone);
    }









    // Getters and Setters


    public String getRt_phone_country_code() {
        return rt_phone_country_code;
    }

    public void setRt_phone_country_code(String rt_phone_country_code) {
        this.rt_phone_country_code = rt_phone_country_code;
    }

    public UserPreferences getUserProfileGlobal() {
        return userProfileGlobal;
    }

    public void setUserProfileGlobal(UserPreferences userProfileGlobal) {
        this.userProfileGlobal = userProfileGlobal;
    }



    public ServiceConfigurationLocal getServiceConfigurationLocal() {
        return serviceConfigurationLocal;
    }

    public void setServiceConfigurationLocal(ServiceConfigurationLocal serviceConfigurationLocal) {
        this.serviceConfigurationLocal = serviceConfigurationLocal;
    }

    public DeliveryGuyData getRt_delivery_guy_data() {
        return rt_delivery_guy_data;
    }

    public void setRt_delivery_guy_data(DeliveryGuyData rt_delivery_guy_data) {
        this.rt_delivery_guy_data = rt_delivery_guy_data;
    }

    public ShopStaffPermissions getRt_shop_staff_permissions() {
        return rt_shop_staff_permissions;
    }

    public void setRt_shop_staff_permissions(ShopStaffPermissions rt_shop_staff_permissions) {
        this.rt_shop_staff_permissions = rt_shop_staff_permissions;
    }

    public String getRt_oneSignalPlayerID() {
        return rt_oneSignalPlayerID;
    }

    public void setRt_oneSignalPlayerID(String rt_oneSignalPlayerID) {
        this.rt_oneSignalPlayerID = rt_oneSignalPlayerID;
    }

    public StaffPermissions getRt_staff_permissions() {
        return rt_staff_permissions;
    }

    public void setRt_staff_permissions(StaffPermissions rt_staff_permissions) {
        this.rt_staff_permissions = rt_staff_permissions;
    }

    public Timestamp getResetCodeExpires() {
        return resetCodeExpires;
    }

    public void setResetCodeExpires(Timestamp resetCodeExpires) {
        this.resetCodeExpires = resetCodeExpires;
    }

    public String getPasswordResetCode() {
        return passwordResetCode;
    }

    public void setPasswordResetCode(String passwordResetCode) {
        this.passwordResetCode = passwordResetCode;
    }

    public double getExtendedCreditLimit() {
        return extendedCreditLimit;
    }

    public void setExtendedCreditLimit(double extendedCreditLimit) {
        this.extendedCreditLimit = extendedCreditLimit;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }


    public double getServiceAccountBalance() {
        return serviceAccountBalance;
    }

    public void setServiceAccountBalance(double serviceAccountBalance) {
        this.serviceAccountBalance = serviceAccountBalance;
    }

    public double getTaxAccountBalance() {
        return taxAccountBalance;
    }

    public void setTaxAccountBalance(double taxAccountBalance) {
        this.taxAccountBalance = taxAccountBalance;
    }

    public int getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(int referredBy) {
        this.referredBy = referredBy;
    }

    public boolean isReferrerCredited() {
        return isReferrerCredited;
    }

    public void setReferrerCredited(boolean referrerCredited) {
        isReferrerCredited = referrerCredited;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }


    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }


    public int getRt_registration_mode() {
        return rt_registration_mode;
    }

    public void setRt_registration_mode(int rt_registration_mode) {
        this.rt_registration_mode = rt_registration_mode;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getRt_email_verification_code() {
        return rt_email_verification_code;
    }

    public void setRt_email_verification_code(String rt_email_verification_code) {
        this.rt_email_verification_code = rt_email_verification_code;
    }

    public String getRt_phone_verification_code() {
        return rt_phone_verification_code;
    }

    public void setRt_phone_verification_code(String rt_phone_verification_code) {
        this.rt_phone_verification_code = rt_phone_verification_code;
    }



    public boolean isAccountPrivate() {
        return isAccountPrivate;
    }

    public void setAccountPrivate(boolean accountPrivate) {
        isAccountPrivate = accountPrivate;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Timestamp getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(Timestamp timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public Timestamp getTimestampUpdated() {
        return timestampUpdated;
    }

    public void setTimestampUpdated(Timestamp timestampUpdated) {
        this.timestampUpdated = timestampUpdated;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Timestamp getTimestampTokenExpires() {
        return timestampTokenExpires;
    }

    public void setTimestampTokenExpires(Timestamp timestampTokenExpires) {
        this.timestampTokenExpires = timestampTokenExpires;
    }



}
