package com.denabelarde.questionnaire.helpers;

/**
 * Created by rygalang on 1/30/15.
 */
public interface QuestionDialogListener {
    public void onSubmitClick(String title, String description);

    public void onDismissed();

    public void onClearText();
}
