package com.denabelarde.questionnaire.Services;

import android.content.Context;

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

    public static String genericChannel = "com.denabelarde.questionnaire";
    public static onDataUpdatedListener onDataUpdatedListener = null;

    public interface onDataUpdatedListener {
        // public abstract void onImageUpload(long reportID, long imageID,
        // String filename);

        public abstract void returnResult(int resultCode);

    }

    public static void setOnDataUpdatedListener(onDataUpdatedListener listener) {
        System.out.println("Initialized Onstatusupdatelistener");
        onDataUpdatedListener = listener;
    }


    public static void fetchAllQuestionsFromParse(final Context context) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Questions");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {

                    ArrayList<String[]> batchArray = new ArrayList<String[]>();
                    for (ParseObject parseObject : objects) {
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
}
