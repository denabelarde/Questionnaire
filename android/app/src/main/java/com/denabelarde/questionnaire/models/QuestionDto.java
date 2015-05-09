package com.denabelarde.questionnaire.models;

import java.io.Serializable;

/**
 * Created by ddabelarde on 5/6/15.
 */
public class QuestionDto implements Serializable {

    private long _id;
    private String objectId;
    private String ownerId;
    private String ownerUsername;
    private String title;
    private String description;
    private String createdAt;
    private String updatedAt;
    private int answersCount;

    public QuestionDto() {
    }

    public QuestionDto(String objectId, String ownerId, String ownerUsername, String title, String description, String createdAt, String updatedAt, int answersCount) {
        this.objectId = objectId;
        this.ownerId = ownerId;
        this.ownerUsername = ownerUsername;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.answersCount = answersCount;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getAnswersCount() {
        return answersCount;
    }

    public void setAnswersCount(int answersCount) {
        this.answersCount = answersCount;
    }
}
