package com.denabelarde.questionnaire.helpers;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ddabelarde on 10/15/14.
 */
public class KeyBoardHandler {

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//        inputMethodManager.toggleSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),0,0);
    }
}
