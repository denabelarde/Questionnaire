package com.denabelarde.questionnaire.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.denabelarde.questionnaire.R;
import com.denabelarde.questionnaire.Services.ServiceManager;
import com.denabelarde.questionnaire.dbmodels.UserDbModel;
import com.denabelarde.questionnaire.helpers.AlertDialogHelper;
import com.denabelarde.questionnaire.models.UserDto;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends Activity {

    @InjectView(R.id.username_txt)
    EditText userNameField;
    @InjectView(R.id.password_txt)
    EditText passwordField;
    @InjectView(R.id.login_button)
    Button loginButton;
    ProgressDialog progressDialog;
    @InjectView(R.id.toplayout)
    RelativeLayout topLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ServiceManager.isNetworkAvailable(LoginActivity.this)){
                    String username = userNameField.getText().toString();
                    String password = passwordField.getText().toString();
                    progressDialog = ProgressDialog.show(LoginActivity.this,
                            "Notification",
                            "Logging in, please wait ...");
                    progressDialog.setCancelable(true);
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            progressDialog.dismiss();
                            if (user != null) {
                                //start sinch service
                                //start next activity
                                progressDialog.dismiss();
                                UserDbModel.insertUser(LoginActivity.this, new UserDto(user.getObjectId(), user.getUsername(), new Date().toString()));
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "There was an error logging in.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    AlertDialogHelper.alertMessage("Error", "No Internet Connection!", LoginActivity.this);
                }
            }
        });

        Keyboard();
    }


    private void Keyboard() {
        final View activityRootView = findViewById(R.id.loginLayout);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        activityRootView.getWindowVisibleDisplayFrame(r);
                        int heightDiff = activityRootView.getRootView()
                                .getHeight() - (r.bottom - r.top);
                        if (heightDiff > 100) {
                            System.out.println("Keyboard is not hidden");

                            topLayout.setVisibility(View.GONE);

                        } else {
                            System.out.println("Keyboard is hidden");
                            Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                            topLayout.setVisibility(View.VISIBLE);
                            topLayout.startAnimation(slideDown);
                        }
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
