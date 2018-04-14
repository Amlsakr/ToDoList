package com.example.aml.todolist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aml.todolist.data.TaskContract;

/**
 * Created by aml on 13/04/18.
 */

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.TaskViewHolder> {

    Context context ;
    private Cursor cursor ;

    public CustomCursorAdapter(Context context) {
        this.context = context;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item , parent , false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        //Indices for the _id , description , and priority columns
        int idIndex = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
        int descriptionIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION);
        int priorityIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_PRIORITY);

        // go to the right location in the cursor
        cursor.moveToPosition(position);

        //Derermine the value of the wanted data
        final  int id = cursor.getInt(idIndex);
        String description = cursor.getString(descriptionIndex);
        int priority = cursor.getInt(priorityIndex);

        // set values
        holder.itemView.setTag(id);
        holder.task_description.setText(description);
        holder.tv_priority.setText(priority + "");

        GradientDrawable  gradientDrawable = (GradientDrawable) holder.tv_priority.getBackground();
        // Get the approriate background color based on priority
        int priorityColor = getColorPriority(priority);
        gradientDrawable.setColor(priorityColor);



        
    }

    private int getColorPriority (int priority){
        int priorityColor = 0 ;

        switch (priority) {
            case  1 :
                priorityColor = ContextCompat.getColor(context , R.color.materialRed);
                break;
            case 2 :
                priorityColor = ContextCompat.getColor(context , R.color.materialOrange) ;
                break;
            case 3  :
                priorityColor = ContextCompat.getColor(context , R.color.materialYellow);
                break;
            default: break;

        }
return priorityColor ;
    }

    public  Cursor swapCursor (Cursor c){
        if (cursor == c) {
            return null ;
        }
        Cursor temp = cursor ;
        cursor = c ;
        if ( c != null) {
            this.notifyDataSetChanged();
        }

        return temp ;
    }

    @Override
    public int getItemCount() {

        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    public class  TaskViewHolder extends RecyclerView.ViewHolder {
        TextView task_description ;
        TextView tv_priority ;

        public TaskViewHolder(View itemView) {
            super(itemView);
            task_description = (TextView) itemView.findViewById(R.id.task_description);
            tv_priority = (TextView) itemView.findViewById(R.id.tv_priority);
        }
    }
}
