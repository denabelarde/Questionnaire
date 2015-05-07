package com.denabelarde.questionnaire.dbmodels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.denabelarde.questionnaire.helpers.DatabaseHelper;
import com.denabelarde.questionnaire.models.QuestionDto;

import java.util.ArrayList;


public class QuestionsDbModel {

    private static String TABLE_TAG = "questions";
    private static String _ID_TAG = "_id";
    private static String OBJECT_ID_TAG = "object_id";
    private static String OWNER_ID_TAG = "owner_id";
    private static String OWNER_USERNAME_TAG = "owner_username";
    private static String TITLE_TAG = "title";
    private static String DESCRIPTION_TAG = "description";
    private static String CREATED_TAG = "created_at";
    private static String UPDATED_TAG = "updated_at";
    private static String ANSWERS_COUNT_TAG = "answers_count";

    public static int count(Context context) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        String[] columns = {"count(" + _ID_TAG + ")"};
        int reportcount = 0;

        Cursor report = dbhelper.query(TABLE_TAG, columns, null, null, null,
                null, null);

        if (report.moveToFirst()) {
            reportcount = report.getInt(0);
        }
        report.close();
        dbhelper.close();

        return reportcount;

    }


    public static void batchInsertQuestions(Context context,
                                            ArrayList<String[]> values) {
        String sql = "INSERT INTO " + TABLE_TAG;
        sql += "(" + OBJECT_ID_TAG + "," + OWNER_ID_TAG + "," + OWNER_USERNAME_TAG + "," + TITLE_TAG + "," + DESCRIPTION_TAG + "," + CREATED_TAG + "," + UPDATED_TAG + "," + ANSWERS_COUNT_TAG + ")";
        sql += "VALUES (?,?,?,?,?,?,?,?)";


        DatabaseHelper dbhelper = new DatabaseHelper(context);
        dbhelper.batchInsert(sql, values);
    }


    public static long insertQuestion(Context context, QuestionDto questionDto) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);

        ContentValues values = new ContentValues();
        values.put(OBJECT_ID_TAG, questionDto.getObjectId());
        values.put(OWNER_ID_TAG, questionDto.getOwnerId());
        values.put(OWNER_USERNAME_TAG, questionDto.getOwnerUsername());
        values.put(TITLE_TAG, questionDto.getTitle());
        values.put(DESCRIPTION_TAG, questionDto.getDescription());
        values.put(CREATED_TAG, questionDto.getCreatedAt());
        values.put(UPDATED_TAG, questionDto.getUpdatedAt());
        values.put(ANSWERS_COUNT_TAG, questionDto.getAnswersCount());

        return dbhelper.insert(TABLE_TAG, values);

    }

    public static ArrayList<QuestionDto> getAllQuestions(Context context) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        Cursor question = dbhelper.query(TABLE_TAG, null, null, null, null, null,
                null);

        ArrayList<QuestionDto> questionsArray = new ArrayList<>();
        if (question.moveToFirst()) {
            do {
                QuestionDto questionDto = new QuestionDto();
                questionDto.set_id(question.getLong(0));
                questionDto.setObjectId(question.getString(1));
                questionDto.setOwnerId(question.getString(2));
                questionDto.setOwnerUsername(question.getString(3));
                questionDto.setTitle(question.getString(4));
                questionDto.setDescription(question.getString(5));
                questionDto.setCreatedAt(question.getString(6));
                questionDto.setUpdatedAt(question.getString(7));
                questionDto.setAnswersCount(question.getInt(8));

                questionsArray.add(questionDto);
            } while (question.moveToNext());

        }

        question.close();
        dbhelper.close();
        return questionsArray;
    }


    public static void deleteAllQuestions(Context context) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        System.out.println(dbhelper.delete(TABLE_TAG, null, null) + " <--- Users Deleted!");

    }


}
