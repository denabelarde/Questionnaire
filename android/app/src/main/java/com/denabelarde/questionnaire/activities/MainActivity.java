package com.denabelarde.questionnaire.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.denabelarde.questionnaire.R;
import com.denabelarde.questionnaire.Services.ParsePushDto;
import com.denabelarde.questionnaire.Services.ServiceManager;
import com.denabelarde.questionnaire.adapters.QuestionsItemAdapter;
import com.denabelarde.questionnaire.dbmodels.QuestionsDbModel;
import com.denabelarde.questionnaire.dbmodels.UserDbModel;
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
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        ParsePush.subscribeInBackground(ServiceManager.genericChannel,new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
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
        }else{
            refreshListView();
        }

        askQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AskQuestionDialogFrag questionDialogFragment = new AskQuestionDialogFrag();
                questionDialogFragment.setRemarksDialogListener(new QuestionDialogListener() {

                    @Override
                    public void onSubmitClick(String title, String description) {
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
                                    progressDialog.dismiss();
                                    questionDialogFragment.dismiss();
                                } else {
                                    Toast.makeText(MainActivity.this, "Sending failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


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


//        if (prefs.getBoolean("firstrun", true)) {
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Questions");
//            progressDialog = ProgressDialog.show(MainActivity.this,
//                    "Notification",
//                    "Fetching questions, please wait ...");
//            progressDialog.setCancelable(false);
//            query.findInBackground(new FindCallback<ParseObject>() {
//                public void done(List<ParseObject> objects, ParseException e) {
//                    progressDialog.dismiss();
//                    if (e == null) {
//                        ArrayList<String[]> batchArray = new ArrayList<String[]>();
//                        for (ParseObject parseObject : objects) {
//                            String[] qArray = {parseObject.getObjectId(), parseObject.getString("ownerId"), parseObject.getString("ownerUserName"), parseObject.getString("title"), parseObject.getString("description"), parseObject.getString("createdAt"), parseObject.getString("updatedAt"), String.valueOf(parseObject.getInt("answersCount"))};
//                            QuestionDto questionDto = new QuestionDto(parseObject.getObjectId(), parseObject.getString("ownerId"), parseObject.getString("ownerUserName"), parseObject.getString("title"), parseObject.getString("description"), parseObject.getString("createdAt"), parseObject.getString("updatedAt"), parseObject.getInt("answersCount"));
//                            questionsArray.add(questionDto);
//                            batchArray.add(qArray);
//                        }
//                        QuestionsDbModel.batchInsertQuestions(MainActivity.this, batchArray);
//                        questionsItemAdapter.notifyDataSetChanged();
//                    } else {
//                        Toast.makeText(MainActivity.this, "Cannot load questions", Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//
//        }

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
                  if(progressDialog!=null){
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
        questionsArray.clear();
        questionsArray.addAll(QuestionsDbModel.getAllQuestions(MainActivity.this));
        questionsItemAdapter.notifyDataSetChanged();
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
        ParsePushReceiver.onDataUpdatedListener = null;
        ServiceManager.onDataUpdatedListener = null;
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
