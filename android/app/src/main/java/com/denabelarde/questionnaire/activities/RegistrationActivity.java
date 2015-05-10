package com.denabelarde.questionnaire.activities;


import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.denabelarde.questionnaire.R;
import com.denabelarde.questionnaire.Services.ServiceManager;
import com.denabelarde.questionnaire.dbmodels.QuestionsDbModel;
import com.denabelarde.questionnaire.helpers.AlertDialogHelper;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegistrationActivity extends ActionBarActivity {

    @InjectView(R.id.register_username)
    EditText userNameTxt;
    @InjectView(R.id.register_password)
    EditText passwordTxt;
    @InjectView(R.id.register_confirmpass)
    EditText registerConfirmPass;
    @InjectView(R.id.register_submit)
    Button registerSubmit;
    ProgressDialog progressDialog;
    boolean result = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.registration_layout);
        ButterKnife.inject(this);
        registerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ServiceManager.isNetworkAvailable(RegistrationActivity.this)) {
                    if (!userNameTxt.getText().toString().isEmpty() && !passwordTxt.getText().toString().isEmpty() && !registerConfirmPass.getText().toString().isEmpty()) {
                        progressDialog = ProgressDialog.show(RegistrationActivity.this,
                                "Notification",
                                "Saving User, please wait ...");
                        progressDialog.setCancelable(false);
                        if (passwordTxt.getText().toString().equals(registerConfirmPass.getText().toString())) {
                            proceedToRegistratin();
                        } else {
                            progressDialog.dismiss();
                            AlertDialogHelper.alertMessage("Error", "Password and confirm password did not match!", RegistrationActivity.this);
                        }
                    } else {
                        AlertDialogHelper.alertMessage("Error", "Fill all required fields!", RegistrationActivity.this);
                    }
                } else {
                    AlertDialogHelper.alertMessage("Error", "No Internet Connection!", RegistrationActivity.this);
                }
            }
        });

    }

    public boolean proceedToRegistratin() {
        final ParseUser parseUser = new ParseUser();
        parseUser.setUsername(userNameTxt.getText().toString());
        parseUser.setPassword(passwordTxt.getText().toString());
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(RegistrationActivity.this, "Successfully created new user.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    AlertDialogHelper.alertMessage("Error", e.getMessage(), RegistrationActivity.this);
                }
                progressDialog.dismiss();
            }
        });

        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
