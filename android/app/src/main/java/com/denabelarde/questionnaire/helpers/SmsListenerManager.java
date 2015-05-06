package com.denabelarde.questionnaire.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by ddabelarde on 1/9/15.
 */
public class SmsListenerManager extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {


        }
    }


}
