package com.example.aml.todolist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by aml on 13/04/18.
 */

public class TaskContentProvider extends ContentProvider {

    private TaskDbHelper taskDbHelper ;
    public static final int TASKS = 100 ;
    public static final int TASKS_WITH_ID = 101 ;
 public  static final UriMatcher sURI_MATCHER = buildUriMatcher();

    public static UriMatcher buildUriMatcher (){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(TaskContract.TaskEntry.AUTHORITY , TaskContract.TaskEntry.PATH_TASKS , TASKS);
        uriMatcher.addURI(TaskContract.TaskEntry.AUTHORITY , TaskContract.TaskEntry.PATH_TASKS + "/#" , TASKS_WITH_ID);
        return uriMatcher ;
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        taskDbHelper = new TaskDbHelper(context);
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = taskDbHelper.getReadableDatabase();
        int match = sURI_MATCHER.match(uri);
        Cursor retCursor ;
        switch (match) {
            case TASKS :
                retCursor = db.query(TaskContract.TaskEntry.TABLE_NAME
                        , projection
                        , selection ,
                        selectionArgs  , null , null ,
                        sortOrder);
                break;
            default:
                throw new  UnsupportedOperationException("UNKNOWN URI " +uri.toString());

        }
       retCursor.setNotificationUri(getContext().getContentResolver() , uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = sURI_MATCHER.match(uri);
        switch ( match) {
            case TASKS :
                return "vnd.android.cursor.dir" + "/" + TaskContract.TaskEntry.AUTHORITY + "/" + TaskContract.TaskEntry.PATH_TASKS ;
            case TASKS_WITH_ID :
                return "vnd.android.cursor.item" + "/" +TaskContract.TaskEntry.AUTHORITY + "/" + TaskContract.TaskEntry.PATH_TASKS;
            default:
                throw new UnsupportedOperationException("UNKNOWN URI : " + uri);
        }
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = taskDbHelper.getWritableDatabase();

        int  match = sURI_MATCHER.match(uri);
        Uri returnUri ;
       switch (match){
           case TASKS :
               long id = db.insert(TaskContract.TaskEntry.TABLE_NAME , null , values);
               if (id > 0) {
                   returnUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI , id) ;

               } else  {
                   throw new android.database.SQLException("Failed to insert row into " + uri);
               }
               break;
           default:
               throw new UnsupportedOperationException("UnKnown uri " +uri) ;
       }
        getContext().getContentResolver().notifyChange(uri , null);
        return  returnUri ;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = taskDbHelper.getWritableDatabase();
        int match = sURI_MATCHER.match(uri);
        int taskDeletd ;
        switch (match) {
            case TASKS_WITH_ID :
                String id = uri.getPathSegments().get(1);
             taskDeletd =   db.delete(TaskContract.TaskEntry.TABLE_NAME , "_id=?" , new String[] {id} );
                break;
            default:
                throw  new UnsupportedOperationException("UN KNOWN URI" +uri.toString());
        }
        if (taskDeletd != 0) {
getContext().getContentResolver().notifyChange(uri ,null);
        }
        return taskDeletd;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int taskUpdated = 0;
        int match = sURI_MATCHER.match(uri);
        switch (match) {
            case  TASKS_WITH_ID :
                String id = uri.getPathSegments().get(1);
                taskUpdated = taskDbHelper.getWritableDatabase().update(TaskContract.TaskEntry.TABLE_NAME , values
                 ,  "_id=?", new String[]{id}) ;
                break;
            default: 
                new UnsupportedOperationException("UN KNOWN URI" +uri.toString());
        }
if ( taskUpdated != 0) {
    getContext().getContentResolver().notifyChange(uri , null);
}
    
        return taskUpdated;
    }
}
