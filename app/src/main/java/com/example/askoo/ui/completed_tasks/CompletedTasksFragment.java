package com.example.askoo.ui.completed_tasks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askoo.CompletedTask;
import com.example.askoo.MyDividerItemDecoration;
import com.example.askoo.R;

import java.util.ArrayList;
import java.util.List;

public class CompletedTasksFragment extends Fragment {

    private final List<CompletedTask> completedTasks = new ArrayList<>();
    private CompletedTasksRecyclerViewAdapter completedTasksRecyclerViewAdapter;
    private CompletedTasksDatabaseHelper completedTasksDatabaseHelper;


    public CompletedTasksFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_completed_tasks, container, false);
        Context context = root.getContext();
        RecyclerView recyclerView = root.findViewById(R.id.completed_tasks_recyclerView_id);
        completedTasksRecyclerViewAdapter = new CompletedTasksRecyclerViewAdapter(context, completedTasks);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(context, DividerItemDecoration.VERTICAL, 16));

        recyclerView.setAdapter(completedTasksRecyclerViewAdapter);

        completedTasksDatabaseHelper = new CompletedTasksDatabaseHelper(context);
        completedTasks.addAll(completedTasksDatabaseHelper.getAllCompletedTasks());
        return root;
    }




}