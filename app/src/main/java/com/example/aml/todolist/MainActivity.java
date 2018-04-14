package com.example.aml.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.example.aml.todolist.data.TaskContract;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = MainActivity.class.getSimpleName() ;
    private static final int TASK_LOADER_ID = 0;
    private CustomCursorAdapter customCursorAdapter ;
    private RecyclerView recycler_view ;
   FloatingActionButton floatingActionButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        customCursorAdapter = new CustomCursorAdapter(this);
        recycler_view.setAdapter(customCursorAdapter);

      new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

          @Override
          public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {


              return false;
          }

          @Override
          public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

              int id =  (int)viewHolder.itemView.getTag();
              String stringId = Integer.toString(id);
              Uri uri = TaskContract.TaskEntry.CONTENT_URI;
              uri = uri.buildUpon().appendPath(stringId).build();
              getContentResolver().delete(uri , null , null);
              getSupportLoaderManager().restartLoader(TASK_LOADER_ID , null, MainActivity.this);


          }
      }).attachToRecyclerView(recycler_view);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , AddTaskActivity.class);
                startActivity(intent);
            }
        });
        getSupportLoaderManager().initLoader(TASK_LOADER_ID ,null ,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID , null , this);
    }

    @Override
    public Loader <Cursor> onCreateLoader(int id,  final Bundle args) {
        return new AsyncTaskLoader <Cursor >(this) {

            // Initialize Cursor that will hold all the task data
            Cursor mTaskData = null ;
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (mTaskData != null) {
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                     return  getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI ,
                             null , null , null , TaskContract.TaskEntry.COLUMN_PRIORITY);
                } catch (Exception e) {
                    Log.e(TAG  , "Failed to load data asyncrhrouncly") ;
                    e.printStackTrace();
                    return null;
                }

            }

            public void  deliverResult (Cursor data){

                mTaskData = data ;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader  <Cursor>loader, Cursor data) {

        //update the data  that adapter use to create view holder
        customCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader <Cursor> loader) {
        customCursorAdapter.swapCursor(null);

    }
}
