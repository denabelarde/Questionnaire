package com.denabelarde.questionnaire.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.denabelarde.questionnaire.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rygalang on 1/30/15.
 */
public class AskQuestionDialogFrag extends DialogFragment {

    @InjectView(R.id.title_field)
    EditText titleField;
    @InjectView(R.id.description_field)
    EditText descriptionField;


    private View remarksView;
    private Context activityContext;
    private LayoutInflater inflater;
    private QuestionDialogListener remarksDialogListener;
    private String titleText, descriptionText;

    @OnClick({R.id.btn_clear, R.id.btn_submit, R.id.close_dialog})
    public void btnClick(View btnView) {
        switch (btnView.getId()) {
            case R.id.btn_submit:
                btnSubmitClicked();
                break;
            case R.id.btn_clear:
                btnClearClick();
                break;
            case R.id.close_dialog:
                btnDismissClick();
                break;
        }
    }

    private void btnDismissClick() {
        titleText = titleField.getText().toString();
        descriptionText = descriptionField.getText().toString();
        if (!titleText.isEmpty() && !descriptionText.isEmpty()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Warning");
            alert.setIcon(R.drawable.warning);
            alert.setMessage("Save this comment/remark?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    remarksDialogListener.onSubmitClick(titleText, descriptionText);
                    dismiss();
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    remarksDialogListener.onClearText();
                    dismiss();
                }
            });
            alert.show();
        } else {
            this.dismiss();
        }
    }

    private void btnSubmitClicked() {
        titleText = titleField.getText().toString();
        descriptionText = descriptionField.getText().toString();
        if (!titleText.isEmpty() && !descriptionText.isEmpty()) {
            remarksDialogListener.onSubmitClick(titleText, descriptionText);
        } else {
            AlertDialogHelper.alertMessage("Error", "empty remarks", activityContext);
        }
    }

    private void btnClearClick() {
        remarksDialogListener.onClearText();
        titleField.setText("");
        descriptionField.setText("");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activityContext = activity;
        this.inflater = LayoutInflater.from(activityContext);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        remarksView = inflater.inflate(R.layout.question_dialog_layout, null);
        Dialog mDialog = new Dialog(activityContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(remarksView);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        ButterKnife.inject(this, remarksView);
        try {
            KeyBoardHandler.showSoftKeyboard(getActivity());
        } catch (Exception e) {

        }
        return mDialog;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        remarksDialogListener.onDismissed();
        super.onDismiss(dialog);
    }

    public void setRemarksDialogListener(QuestionDialogListener remarksDialogListener) {
        this.remarksDialogListener = remarksDialogListener;
    }


}
