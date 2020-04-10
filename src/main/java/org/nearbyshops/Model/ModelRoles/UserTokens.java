package org.nearbyshops.Model.ModelRoles;

import java.sql.Timestamp;

/**
 * Created by sumeet on 29/5/16.
 */
public class UserTokens {



    // Table Name for User
    public static final String TABLE_NAME = "USER_TOKENS";

    // Column names
    public static final String TOKEN_ID = "TOKEN_ID";
    public static final String LOCAL_USER_ID = "LOCAL_USER_ID";
    public static final String TOKEN_STRING = "TOKEN_STRING";
    public static final String DATE_TIME_CREATED = "DATE_TIME_CREATED";



    // Create Table CurrentServiceConfiguration Provider
    public static final String createTable =

            "CREATE TABLE IF NOT EXISTS "
                    + UserTokens.TABLE_NAME + "("
                    + " " + UserTokens.TOKEN_ID + " SERIAL PRIMARY KEY,"
                    + " " + UserTokens.LOCAL_USER_ID + " int not null,"
                    + " " + UserTokens.TOKEN_STRING + " text not null,"
                    + " " + UserTokens.DATE_TIME_CREATED + " timestamp with time zone NOT NULL default now(),"
                    + " FOREIGN KEY(" + UserTokens.LOCAL_USER_ID +") REFERENCES " + User.TABLE_NAME + "(" + User.USER_ID + ") ON DELETE CASCADE"
                    + ")";



    // Instance Variables
    private int tokenID;
    private int userIDLocal;
    private String tokenString;
    private Timestamp dateTimeCreated;






    // Getters and Setters


    public int getTokenID() {
        return tokenID;
    }

    public void setTokenID(int tokenID) {
        this.tokenID = tokenID;
    }

    public int getUserIDLocal() {
        return userIDLocal;
    }

    public void setUserIDLocal(int userIDLocal) {
        this.userIDLocal = userIDLocal;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public Timestamp getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(Timestamp dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }
}
