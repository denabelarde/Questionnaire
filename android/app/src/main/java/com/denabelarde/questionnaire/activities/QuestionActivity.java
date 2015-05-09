package com.denabelarde.questionnaire.activities;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.denabelarde.questionnaire.R;
import com.denabelarde.questionnaire.Services.ServiceManager;
import com.denabelarde.questionnaire.adapters.AnswersItemAdapter;
import com.denabelarde.questionnaire.dbmodels.AnswersDbModel;
import com.denabelarde.questionnaire.dbmodels.QuestionsDbModel;
import com.denabelarde.questionnaire.dbmodels.UserDbModel;
import com.denabelarde.questionnaire.helpers.AlertDialogHelper;
import com.denabelarde.questionnaire.helpers.KeyBoardHandler;
import com.denabelarde.questionnaire.models.AnswerDto;
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

public class QuestionActivity extends ActionBarActivity {

    @InjectView(R.id.question_title)
    TextView questionTitle;
    @InjectView(R.id.question_desc)
    TextView questionDesc;
    @InjectView(R.id.question_answers)
    ListView answersLv;
    @InjectView(R.id.answer_field)
    EditText answerField;
    @InjectView(R.id.submit_button)
    TextView submitButton;

    QuestionDto questionDto;
    AnswersItemAdapter answersItemAdapter;
    ArrayList<AnswerDto> answersArray;
    ProgressDialog progressDialog;
    UserDto userDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.inject(this);
        questionDto = (QuestionDto) getIntent().getExtras().getSerializable("question");

        if (questionDto != null) {
            answersArray = new ArrayList<>();
            answersItemAdapter = new AnswersItemAdapter(this, answersArray);
            answersLv.setAdapter(answersItemAdapter);
            userDto = UserDbModel.getCurrentUser(this);
            progressDialog = ProgressDialog.show(QuestionActivity.this,
                    "Notification",
                    "Loading answers, please wait ...");
            progressDialog.setCancelable(false);
            ServiceManager.fetchAnswersForQuestion(this, questionDto.getObjectId());
            questionTitle.setText(questionDto.getTitle());
            questionDesc.setText(questionDto.getDescription());
        }


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ServiceManager.isNetworkAvailable(QuestionActivity.this)) {
                    if (!answerField.getText().toString().isEmpty()) {
                        progressDialog = ProgressDialog.show(QuestionActivity.this,
                                "Notification",
                                "Submitting answer, please wait ...");
                        progressDialog.setCancelable(false);
                        final ParseObject answerObject = new ParseObject("Answers");
                        answerObject.put("userId", userDto.getObjectID());
                        answerObject.put("userName", userDto.getUserName());
                        answerObject.put("questionId", questionDto.getObjectId());
                        answerObject.put("answerString", answerField.getText().toString());
                        answerObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    ParsePush push = new ParsePush();
                                    push.setChannel(ServiceManager.answerChannelPrefix + questionDto.getObjectId());
                                    push.setMessage("answer~" + questionDto.getObjectId() + "~new");
                                    push.sendInBackground(new SendCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                System.out.println("question push sent");
                                            }
                                        }
                                    });
                                    subscribeToChannel();
                                    try {
                                        KeyBoardHandler.hideSoftKeyboard(QuestionActivity.this);
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                    answerField.setText("");
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(QuestionActivity.this, "Sending failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        AlertDialogHelper.alertMessage("Error", "Answer must not be empty!", QuestionActivity.this);
                    }

                } else {
                    AlertDialogHelper.alertMessage("Error", "No Internet Connection!", QuestionActivity.this);
                }
            }
        });
    }

    private void refreshListView() {
        try {
            answersArray.clear();
            answersArray.addAll(AnswersDbModel.getAllAnswersByQuestion(this, questionDto.getObjectId()));
            answersItemAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
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
                        subscribeToChannel();
                    }
                }
            });
        }
    }

    public void subscribeToChannel() {
        ParsePush.subscribeInBackground(ServiceManager.answerChannelPrefix + questionDto.getObjectId(), new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    System.out.println("Successfully Subscribed to " + questionDto.getObjectId());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
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

    @Override
    protected void onResume() {
        initDataUpdateListener();
        subscribeToChannel();
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        ParsePush.unsubscribeInBackground(ServiceManager.answerChannelPrefix + questionDto.getObjectId(), new SaveCallback() {
            @Override
            public void done(ParseException e) {
                System.out.println("Successfully unsubscribed from " + questionDto.getObjectId());
            }
        });
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        ServiceManager.onDataUpdatedListener = null;
        super.onPause();
    }
}
