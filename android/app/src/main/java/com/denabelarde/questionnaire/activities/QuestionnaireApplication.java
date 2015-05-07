package com.denabelarde.questionnaire.activities;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by ddabelarde on 5/6/15.
 */
public class QuestionnaireApplication extends Application {

    @Override
    public void onCreate() {
        Parse.initialize(this, "YlA9W8KPP1ieJ7mzF4yAfeUpVcIHgHrCT92e9HJ8", "hT9SPJ0xiFhJjIjpjvx4R39MmDcsrPTFhkzdO5lw");
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }
}
