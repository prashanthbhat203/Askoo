package com.example.askoo.ui.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askoo.R;
import com.example.askoo.Task;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TasksRecyclerViewAdapter extends ListAdapter<Task, TasksRecyclerViewAdapter.MyViewHolder> {

    private static final String TAG = TasksRecyclerViewAdapter.class.getSimpleName();

    private final Context context;
    private final TasksRecyclerViewAdapter.TasksAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView task;
        private final TextView dot;
        private final TextView timestamp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.tasks_todo);
            dot = itemView.findViewById(R.id.tasks_dot);
            timestamp = itemView.findViewById(R.id.timestamp);

            itemView.setOnClickListener(view -> listener.onClick(getTask(getLayoutPosition()).getId(), getLayoutPosition()));

            itemView.setOnLongClickListener(view -> {
                listener.onLongClick(getTask(getLayoutPosition()).getId(), getLayoutPosition());
                return true;
            });
        }
    }

    public TasksRecyclerViewAdapter(Context context, TasksRecyclerViewAdapter.TasksAdapterListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }

    @NotNull
    @Override
    public TasksRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_content, parent, false);
        return new TasksRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull TasksRecyclerViewAdapter.MyViewHolder holder, int position) {
        Task task = getTask(position);
        if(task != null) {
            holder.task.setText(task.getTask());
            holder.dot.setText(Html.fromHtml("&#8226;"));
            holder.timestamp.setText(formatDate(task.getTimeStamp()));
        }
    }

    public Task getTask(int position) {
        return getItem(position);
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Task>() {
                @Override
                public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
                    return oldItem.getId() == newItem.getId() && oldItem.getTask().equals(newItem.getTask());
                }
            };

    private String formatDate(String timeStamp) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = simpleDateFormat.parse(timeStamp);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM-d");
            assert date != null;
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public interface TasksAdapterListener {
        void onClick(int taskId, int position);

        void onLongClick(int taskId, int position);
    }
}