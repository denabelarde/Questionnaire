package com.denabelarde.questionnaire.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.denabelarde.questionnaire.R;
import com.denabelarde.questionnaire.dbmodels.UserDbModel;

import java.util.Timer;
import java.util.TimerTask;


public class SplashScreen extends Activity {


    private Handler mHandler;

    private long del = 1000;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        /*
         * Here we check whether the app is a release version or the debug
		 * version Release version allows tracking of Crashlytics, a crash/bug
		 * reporting tool
		 */
        boolean isDebuggable = (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        if (isDebuggable) {
            System.out.println("This is a debug apk");
        } else {
            System.out.println("This is a release apk");
        }

        Timer timer = new Timer();
        timer.schedule(task, del);
    }


    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (UserDbModel.count(SplashScreen.this) > 0) {
                /*ActivityLogDbModel.deleteAllActivityLogs(SplashScreen.this);*/
                Intent in = new Intent().setClass(SplashScreen.this,
                        MainActivity.class).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            } else {
                Intent in = new Intent().setClass(SplashScreen.this,
                        LoginActivity.class).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }

            finish();
        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // mChecker.onDestroy();
    }

}
