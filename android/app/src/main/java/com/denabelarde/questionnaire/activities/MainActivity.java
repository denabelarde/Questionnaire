package com.denabelarde.questionnaire.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.denabelarde.questionnaire.R;
import com.denabelarde.questionnaire.Services.ParsePushDto;
import com.denabelarde.questionnaire.Services.ServiceManager;
import com.denabelarde.questionnaire.adapters.QuestionsItemAdapter;
import com.denabelarde.questionnaire.dbmodels.QuestionsDbModel;
import com.denabelarde.questionnaire.dbmodels.UserDbModel;
import com.denabelarde.questionnaire.helpers.AlertDialogHelper;
import com.denabelarde.questionnaire.helpers.AskQuestionDialogFrag;
import com.denabelarde.questionnaire.helpers.ParsePushReceiver;
import com.denabelarde.questionnaire.helpers.QuestionDialogListener;
import com.denabelarde.questionnaire.models.QuestionDto;
import com.denabelarde.questionnaire.models.UserDto;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.questions_list)
    ListView questionsLv;
    @InjectView(R.id.ask_question_btn)
    TextView askQuestionBtn;
    QuestionsItemAdapter questionsItemAdapter;
    ArrayList<QuestionDto> questionsArray;
    ProgressDialog progressDialog;
    SharedPreferences prefs;
    UserDto userDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        ParsePush.subscribeInBackground(ServiceManager.genericChannel, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    System.out.println("successfully subscribed in generic channel");
                }
            }
        });
        questionsArray = new ArrayList<QuestionDto>();
        questionsItemAdapter = new QuestionsItemAdapter(this, questionsArray);
        questionsLv.setAdapter(questionsItemAdapter);

        prefs = getSharedPreferences(
                "dynamicobjx.mercury", Context.MODE_PRIVATE);
        userDto = UserDbModel.getCurrentUser(this);
        initDataUpdateListener();

        if (prefs.getBoolean("firstrun", true)) {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Notification",
                    "Fetching questions, please wait ...");
            ServiceManager.fetchAllQuestionsFromParse(this);
        } else {
            refreshListView();
        }

        askQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AskQuestionDialogFrag questionDialogFragment = new AskQuestionDialogFrag();
                questionDialogFragment.setRemarksDialogListener(new QuestionDialogListener() {

                    @Override
                    public void onSubmitClick(String title, String description) {
                        if (ServiceManager.isNetworkAvailable(MainActivity.this)) {
                            if (!title.isEmpty() && !description.isEmpty()) {
                                progressDialog = ProgressDialog.show(MainActivity.this,
                                        "Notification",
                                        "Sending Question, please wait ...");
                                progressDialog.setCancelable(false);
                                final ParseObject questionObject = new ParseObject("Questions");
                                questionObject.put("title", title);
                                questionObject.put("description", description);
                                questionObject.put("ownerId", userDto.getObjectID());
                                questionObject.put("ownerUserName", userDto.getUserName());
                                questionObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            ParsePush push = new ParsePush();
                                            push.setChannel(ServiceManager.genericChannel);
                                            push.setMessage("question~new");
                                            push.sendInBackground(new SendCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        System.out.println("question push sent");
                                                    }
                                                }
                                            });

                                            ParsePush.subscribeInBackground(questionObject.getObjectId(), new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        System.out.println("Successfully subscribed in " + questionObject.getObjectId() + " channel!");
                                                    }
                                                }
                                            });

                                            questionDialogFragment.dismiss();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Sending failed.", Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();
                                    }
                                });
                            } else {
                                AlertDialogHelper.alertMessage("Error", "Fill all required fields!", MainActivity.this);
                            }
                        } else {
                            AlertDialogHelper.alertMessage("Error", "No Internet Connection!", MainActivity.this);
                        }
                    }

                    @Override
                    public void onDismissed() {
                    }

                    @Override
                    public void onClearText() {

                    }
                });
                questionDialogFragment.show(getSupportFragmentManager(), null);
            }
        });


        questionsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                intent.putExtra("question", questionsArray.get(i));
                startActivity(intent);
            }
        });

    }


    private void initPushListener() {
        if (ParsePushReceiver.onDataUpdatedListener == null) {
            ParsePushReceiver.setOnDataUpdatedListener(new ParsePushReceiver.onDataUpdatedListener() {
                @Override
                public void refreshData() {
                    refreshListView();
                }
            });
        }
    }

    private void initDataUpdateListener() {
        if (ServiceManager.onDataUpdatedListener == null) {
            ServiceManager.setOnDataUpdatedListener(new ServiceManager.onDataUpdatedListener() {
                @Override
                public void returnResult(int resultCode) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    if (resultCode == 200) {
                        refreshListView();
                        prefs.edit().putBoolean("firstrun", false).apply();
                    }
                }
            });
        }
    }

    private void refreshListView() {
        try {
            questionsArray.clear();
            questionsArray.addAll(QuestionsDbModel.getAllQuestions(MainActivity.this));
            questionsItemAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPushListener();
        initDataUpdateListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        ParsePushReceiver.onDataUpdatedListener = null;
        ServiceManager.onDataUpdatedListener = null;
        super.onPause();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specif
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout_settings) {
            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
            adb.setTitle("Warning");
            adb.setIcon(R.drawable.warning);
            adb.setMessage(getResources().getString(R.string.logout_confirmation));
            adb.setNegativeButton("NO",
                    new AlertDialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                        }
                    });
            adb.setPositiveButton("YES",
                    new AlertDialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // isCheckedTODO Auto-generated method stub
                            UserDbModel.deleteAllUsers(MainActivity.this);
                            finish();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
            adb.setCancelable(false);
            adb.show();
        } else {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
