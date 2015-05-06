package com.denabelarde.questionnaire.models;

/**
 * Created by ddabelarde on 5/6/15.
 */
public class UserDto {

    private long _id;
    private String objectID;
    private String userName;
    private String dateCreated;

    public UserDto() {
    }

    public UserDto(String objectID, String userName, String dateCreated) {
        this.objectID = objectID;
        this.userName = userName;
        this.dateCreated = dateCreated;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
