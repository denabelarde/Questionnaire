package com.denabelarde.questionnaire.Services;

/**
 * Created by Denver on 5/8/2015.
 */
public class ParsePushDto {

    String alert;
    String push_hash;

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getPush_hash() {
        return push_hash;
    }

    public void setPush_hash(String push_hash) {
        this.push_hash = push_hash;
    }
}
