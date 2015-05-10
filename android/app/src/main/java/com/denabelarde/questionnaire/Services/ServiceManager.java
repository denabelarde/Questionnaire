package com.denabelarde.questionnaire.Services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.denabelarde.questionnaire.dbmodels.AnswersDbModel;
import com.denabelarde.questionnaire.dbmodels.QuestionsDbModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddabelarde on 5/7/15.
 */
public class ServiceManager {

    public static String genericChannel = "comdenabelardequestionnaire";
    public static String answerChannelPrefix = "answer_";
    public static onDataUpdatedListener onDataUpdatedListener = null;

    public static void setOnDataUpdatedListener(onDataUpdatedListener listener) {
        System.out.println("Initialized Onstatusupdatelistener");
        onDataUpdatedListener = listener;
    }

    public static void fetchAllQuestionsFromParse(final Context context) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Questions");
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    ArrayList<String[]> batchArray = new ArrayList<String[]>();
                    for (ParseObject parseObject : objects) {
                        if (!QuestionsDbModel.checkIfExisting(context, parseObject.getObjectId())) {
                            System.out.println(parseObject.getObjectId() + " <<< getObjectId");
                            System.out.println(parseObject.getString("title") + " <<< Question title");
                            System.out.println(parseObject.getDate("updatedAt") + " <<< updatedAt");
                            System.out.println(parseObject.getDate("createdAt") + " <<< createdAt");
                            System.out.println(parseObject.getString("description") + " <<< description");
                            System.out.println(parseObject.getString("ownerId") + " <<< ownerId");
                            System.out.println(parseObject.getString("ownerUserName") + " <<< ownerUserName");
                            System.out.println(parseObject.getString("answersCount") + " <<< answersCount");
                            String[] qArray = {parseObject.getObjectId(), parseObject.getString("ownerId"), parseObject.getString("ownerUserName"), parseObject.getString("title"), parseObject.getString("description"), parseObject.getDate("createdAt") + "", parseObject.getDate("updatedAt") + "", String.valueOf(parseObject.getInt("answersCount")) + ""};
                            batchArray.add(qArray);
                        }
                    }
                    QuestionsDbModel.batchInsertQuestions(context, batchArray);
                    if (onDataUpdatedListener != null) {
                        onDataUpdatedListener.returnResult(200);
                    }
                } else {
                    if (onDataUpdatedListener != null) {
                        onDataUpdatedListener.returnResult(-1);
                    }
                }
            }
        });
    }

    public static void fetchAnswersForQuestion(final Context context, String questionId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Answers");
        query.whereEqualTo("questionId", questionId);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    System.out.println("success");
                    ArrayList<String[]> batchArray = new ArrayList<String[]>();
                    for (ParseObject parseObject : objects) {
                        if (!AnswersDbModel.checkIfExisting(context, parseObject.getObjectId())) {
                            String[] answerArray = {parseObject.getObjectId(), parseObject.getString("answerString"), parseObject.getString("userId"), parseObject.getString("questionId"), parseObject.getString("userName")};
                            System.out.println(parseObject.getObjectId()+ " <<<object id");
                            System.out.println(parseObject.getString("answerString")+" <<< answerString");
                            batchArray.add(answerArray);
                        }
                    }
                    AnswersDbModel.batchInsertAnswers(context, batchArray);
                    if (onDataUpdatedListener != null) {
                        onDataUpdatedListener.returnResult(200);
                    }
                } else {
                    if (onDataUpdatedListener != null) {
                        onDataUpdatedListener.returnResult(-1);
                    }
                }
            }
        });
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public interface onDataUpdatedListener {
        // public abstract void onImageUpload(long reportID, long imageID,
        // String filename);

        public abstract void returnResult(int resultCode);

    }
}
