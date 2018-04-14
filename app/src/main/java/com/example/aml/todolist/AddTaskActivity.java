package com.example.aml.todolist;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.aml.todolist.data.TaskContract;

public class AddTaskActivity extends AppCompatActivity {

    private int mPriority = 0 ;
    EditText edit_text_task_description ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        edit_text_task_description = (EditText) findViewById(R.id.edit_text_task_description);
        setContentView(R.layout.activity_add_task);
        ( (RadioButton) findViewById(R.id.rdButton1)) .setChecked(true);
        mPriority = 1 ;
    }



    public void onAddTask(View view) {
        String input = ((EditText) findViewById(R.id.edit_text_task_description)).getText().toString();

        if (input.length() == 0) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION , input);
        contentValues.put(TaskContract.TaskEntry.COLUMN_PRIORITY , mPriority);
        Uri uri = getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI , contentValues);
        if ( uri != null) {
            Toast.makeText(getApplication() , uri.toString() , Toast.LENGTH_LONG ) .show();
            Log.e("inert" ,uri.toString());
        }
     finish();

    }

    public void onPrioritySelected(View view) {
        if (  ( (RadioButton) findViewById(R.id.rdButton1)).isChecked()) {
            mPriority = 1;
        } else if
                ( ( (RadioButton) findViewById(R.id.rdButton2)) .isChecked()) {
            mPriority = 2;
        } else if
                (  ( (RadioButton) findViewById(R.id.rdButton3)).isChecked()) {
            mPriority=3;

        }

    }

}
