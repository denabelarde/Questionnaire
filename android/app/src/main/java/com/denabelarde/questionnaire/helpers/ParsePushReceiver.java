package com.denabelarde.questionnaire.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.denabelarde.questionnaire.Services.ParsePushDto;
import com.denabelarde.questionnaire.Services.ServiceManager;
import com.google.gson.Gson;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by ddabelarde on 5/6/15.
 */
public class ParsePushReceiver extends ParsePushBroadcastReceiver {

    NotificationManager mNotificationManager;
    int notification_id = 0;
    public static onDataUpdatedListener onDataUpdatedListener = null;

    public interface onDataUpdatedListener {
        // public abstract void onImageUpload(long reportID, long imageID,
        // String filename);

        public abstract void refreshData();

    }

    public static void setOnDataUpdatedListener(onDataUpdatedListener listener) {
        System.out.println("Initialized Onstatusupdatelistener");
        onDataUpdatedListener = listener;

    }

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        /** remove notification from notification bar */
        Notification n = super.getNotification(context, intent);
        notification_id = intent.getExtras().getInt("NOTIFICATION_TYPE");
        mNotificationManager.notify(notification_id, n);
        mNotificationManager.cancel(notification_id);
        return null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mNotificationManager = (NotificationManager) context.getApplicationContext().
                getSystemService(Context.NOTIFICATION_SERVICE);
        System.out.println(intent.getStringExtra(ParsePushBroadcastReceiver.KEY_PUSH_DATA) + " <<<<PUSH NOTIFICATION");
        Gson gson = new Gson();
        ParsePushDto parsePushDto = gson.fromJson(intent.getStringExtra(ParsePushBroadcastReceiver.KEY_PUSH_DATA), ParsePushDto.class);
        try {
            if (parsePushDto.getAlert().split("~")[0].equalsIgnoreCase("question")) {
                ServiceManager.fetchAllQuestionsFromParse(context);
            } else {
                ServiceManager.fetchAnswersForQuestion(context, parsePushDto.getAlert().split("~")[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onReceive(context, intent);
    }
}