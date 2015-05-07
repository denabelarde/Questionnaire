package com.denabelarde.questionnaire.dbmodels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.denabelarde.questionnaire.helpers.DatabaseHelper;
import com.denabelarde.questionnaire.models.UserDto;


public class UserDbModel {

    private static String TABLE_TAG = "users";
    private static String _ID_TAG = "_id";
    private static String OBJECT_ID_TAG = "object_id";
    private static String USERNAME_TAG = "username";
    private static String DATECREATED_TAG = "datecreated";

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


    public static long insertUser(Context context, UserDto userDto) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);

        ContentValues values = new ContentValues();
        values.put(USERNAME_TAG, userDto.getUserName());
        values.put(OBJECT_ID_TAG, userDto.getObjectID());
        values.put(DATECREATED_TAG, userDto.getDateCreated());
        return dbhelper.insert(TABLE_TAG, values);

    }

    public static UserDto getCurrentUser(Context context) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        int status = 1;
        Cursor user = dbhelper.query(TABLE_TAG, null, null,null, null, null,
                null);

        UserDto userDto = null;
        if (user.moveToFirst()) {
            do {
                userDto = new UserDto();
                userDto.set_id(user.getLong(0));
                userDto.setObjectID(user.getString(1));
                userDto.setUserName(user.getString(2));
                userDto.setDateCreated(user.getString(3));
            } while (user.moveToNext());

        }

        user.close();
        dbhelper.close();
        return userDto;
    }


    public static void deleteAllUsers(Context context) {
        DatabaseHelper dbhelper = new DatabaseHelper(context);
        System.out.println(dbhelper.delete(TABLE_TAG, null, null) + " <--- Users Deleted!");

    }


}
