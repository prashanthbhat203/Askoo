package com.example.askoo;

public class CompletedTask {
    int id;
    String completed_tasks;

    public static final String TABLE_NAME = "completed_tasks_again";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_COMPLETED_TASK = "task";


    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_COMPLETED_TASK + " TEXT"
            + ")";

    public CompletedTask() {
    }

    public CompletedTask(int id, String completed_tasks) {
        this.id = id;
        this.completed_tasks = completed_tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompleted_tasks() {
        return completed_tasks;
    }

    public void setCompleted_tasks(String completed_tasks) {
        this.completed_tasks = completed_tasks;
    }
}
