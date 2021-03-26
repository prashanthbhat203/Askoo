package com.example.askoo.ui.completed_tasks;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askoo.CompletedTask;
import com.example.askoo.R;

import java.util.List;

public class CompletedTasksRecyclerViewAdapter extends RecyclerView.Adapter<CompletedTasksRecyclerViewAdapter.MyViewHolder> {

    private final List<CompletedTask> completedTasks;

    public CompletedTasksRecyclerViewAdapter(Context context, List<CompletedTask> completedTasks) {
        this.completedTasks = completedTasks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_tasks_content, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CompletedTask completedTask = completedTasks.get(position);
        if (completedTask != null) {
            holder.task.setText(completedTask.getCompleted_tasks());
            holder.dot.setText(Html.fromHtml("&#8226"));
        }
    }


    @Override
    public int getItemCount() {
        return completedTasks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView task;
        private final TextView dot;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.completed_tasks_todo);
            task.setPaintFlags(task.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            dot = itemView.findViewById(R.id.completed_tasks_dot);

        }
    }


}

