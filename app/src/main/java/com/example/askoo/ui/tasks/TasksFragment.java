package com.example.askoo.ui.tasks;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askoo.CompletedTask;
import com.example.askoo.MyDividerItemDecoration;
import com.example.askoo.R;
import com.example.askoo.Task;
import com.example.askoo.ui.completed_tasks.CompletedTasksDatabaseHelper;
import com.example.askoo.ui.completed_tasks.CompletedTasksRecyclerViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment implements TasksRecyclerViewAdapter.TasksAdapterListener {

    private final List<Task> taskList = new ArrayList<>();
    private final List<CompletedTask> completedTasks = new ArrayList<>();

    private TasksRecyclerViewAdapter recyclerViewAdapter;
    private EditText newTask;

    private TasksDatabaseHelper databaseHelper;

    public TasksFragment() {
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        Context context = root.getContext();
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView_id);
        recyclerViewAdapter = new TasksRecyclerViewAdapter(context, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(context, DividerItemDecoration.VERTICAL, 16));
        recyclerView.setAdapter(recyclerViewAdapter);

        databaseHelper = new TasksDatabaseHelper(context);
        taskList.addAll(databaseHelper.getAllTasks());
        recyclerViewAdapter.submitList(taskList);

        newTask = root.findViewById(R.id.et_enterTodo);
        newTask.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                createTaskFromEditText();
                return true;
            }
            return false;
        });
        return root;
    }


    private void createTask(String task) {
        long id = databaseHelper.insertTask(task);

        Task task1 = databaseHelper.getTask(id);

        if (task1 != null) {
            taskList.add(0, task1);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    public void createTaskFromEditText() {
        if (TextUtils.isEmpty(newTask.getText().toString())) {
            Toast.makeText(getActivity(), "Please enter your task", Toast.LENGTH_SHORT).show();
        } else {
            createTask(newTask.getText().toString());
            newTask.setText(null);
            closeKeyboard();
        }
    }

    public void closeKeyboard() {
        View view = getActivity().getCurrentFocus();

        if (view != null) {
            InputMethodManager methodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onClick(int taskId, int position) {
//        Toast.makeText(getContext(), "Currently Underdevelopment!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(int taskId, int position) {
        View view = ((FragmentActivity) getContext()).getLayoutInflater().inflate(R.layout.bottomsheet_dialog,
                null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(view);

        TextView delete = view.findViewById(R.id.tv_delete);
        TextView moveToComplete = view.findViewById(R.id.tv_move);

        delete.setOnClickListener(view12 -> {
            deleteTask(position);
            Snackbar.make(getView(), "Task deleted", Snackbar.LENGTH_LONG).show();
            bottomSheetDialog.dismiss();
        });

        moveToComplete.setOnClickListener(view1 -> {
            Cursor cursor = databaseHelper.getData(taskId);
            cursor.moveToFirst();
            String complete = cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK));
            cursor.close();
            createCompletedTask(complete);
            deleteTask(position);
            Snackbar.make(getView(), "Move to completed", Snackbar.LENGTH_LONG).show();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }

    private void deleteTask(int position) {
        databaseHelper.deleteTask(taskList.get(position));
        taskList.remove(position);
        recyclerViewAdapter.notifyItemRemoved(position);
    }

    private void createCompletedTask(String completedTask) {
        CompletedTasksDatabaseHelper completedTasksDatabaseHelper = new CompletedTasksDatabaseHelper(getContext());
        CompletedTasksRecyclerViewAdapter completedTasksRecyclerViewAdapter = new CompletedTasksRecyclerViewAdapter(getContext(), completedTasks);
        long id = completedTasksDatabaseHelper.insertCompletedTasks(completedTask);


        CompletedTask completedTask1 = completedTasksDatabaseHelper.getCompletedTasks(id);

        if (completedTask1 != null) {
            completedTasks.add(0, completedTask1);
            completedTasksRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

}