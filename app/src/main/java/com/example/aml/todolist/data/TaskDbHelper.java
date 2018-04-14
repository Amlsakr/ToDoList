package com.example.aml.todolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aml on 13/04/18.
 */

 public class TaskDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasksDB.db";
    private static final int VERSION = 1 ;
    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
final String CREATE_TABLE =
        "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " (" +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY, " +
                TaskContract.TaskEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                TaskContract.TaskEntry.COLUMN_PRIORITY + " INTEGER NOT NULL);";

     db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}
