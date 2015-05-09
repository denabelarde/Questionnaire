package com.denabelarde.questionnaire.dbmodels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.denabelarde.questionnaire.helpers.DatabaseHelper;
import com.denabelarde.questionnaire.models.AnswerDto;
import com.denabelarde.questionnaire.models.QuestionDto;

import java.util.ArrayList;


public class AnswersDbModel {

    private static String TABLE_TAG = "answers";
    private static String _ID_TAG = "_id";
    private static String OBJECT_ID_TAG = "object_id";
    private static String ANSWER_TAG = "answer_string";
    private static String USERID_TAG = "user_id";
    private static String QUESTION_ID_TAG = "question_id";
    private static String USERNAME_TAG = "username";

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

    public static boolean checkIfExisting(Context context, String objectId) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        String[] columns = {"count(" + OBJECT_ID_TAG + ")"};
        int reportcount = 0;
        boolean result = false;
        Cursor report = dbhelper.query(TABLE_TAG, columns, OBJECT_ID_TAG + "=?", new String[]{objectId}, null,
                null, null);

        if (report.moveToFirst()) {
            reportcount = report.getInt(0);
        }

        if (reportcount > 0) {
            result = true;
        }
        report.close();
        dbhelper.close();

        return result;

    }

    public static void batchInsertAnswers(Context context,
                                          ArrayList<String[]> values) {
        String sql = "INSERT INTO " + TABLE_TAG;
        sql += "(" + OBJECT_ID_TAG + "," + ANSWER_TAG + "," + USERID_TAG + "," + QUESTION_ID_TAG + "," + USERNAME_TAG + ")";
        sql += "VALUES (?,?,?,?,?)";

        DatabaseHelper dbhelper = new DatabaseHelper(context);
        dbhelper.batchInsert(sql, values);
    }


    public static long insertAnswer(Context context, AnswerDto answerDto) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);

        ContentValues values = new ContentValues();
        values.put(OBJECT_ID_TAG, answerDto.getObjectId());
        values.put(ANSWER_TAG, answerDto.getAnswer());
        values.put(USERID_TAG, answerDto.getUserId());
        values.put(QUESTION_ID_TAG, answerDto.getQuestionId());

        return dbhelper.insert(TABLE_TAG, values);

    }

    public static ArrayList<AnswerDto> getAllAnswersByQuestion(Context context, String questionId) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        Cursor answer = dbhelper.query(TABLE_TAG, null, QUESTION_ID_TAG + "=?", new String[]{questionId}, null, null,
                null);

        ArrayList<AnswerDto> answerArray = new ArrayList<>();
        if (answer.moveToFirst()) {
            do {
                AnswerDto answerDto = new AnswerDto();
                answerDto.set_id(answer.getLong(0));
                answerDto.setObjectId(answer.getString(1));
                answerDto.setAnswer(answer.getString(2));
                answerDto.setUserId(answer.getString(3));
                answerDto.setQuestionId(answer.getString(4));
                answerDto.setUserName(answer.getString(5));

                answerArray.add(answerDto);
            } while (answer.moveToNext());

        }

        answer.close();
        dbhelper.close();
        return answerArray;
    }


    public static void deleteAllAnswers(Context context) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        System.out.println(dbhelper.delete(TABLE_TAG, null, null) + " <--- Answers Deleted!");

    }


}
