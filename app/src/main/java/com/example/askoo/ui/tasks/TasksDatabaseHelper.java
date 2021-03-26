package com.example.askoo.ui.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.askoo.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tasks_db";

    public TasksDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Task.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Task.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insertTask(String task) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Task.COLUMN_TASK, task);

        long id = sqLiteDatabase.insert(Task.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public Task getTask(long id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(Task.TABLE_NAME,
                new String[]{Task.COLUMN_ID, Task.COLUMN_TASK, Task.COLUMN_TIMESTAMP},
                Task.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        assert cursor != null;
        Task task = new Task(
                cursor.getInt(cursor.getColumnIndex(Task.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK)),
                cursor.getString(cursor.getColumnIndex(Task.COLUMN_TIMESTAMP))
        );
        cursor.close();
        return task;

    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Task.TABLE_NAME + " ORDER BY " +
                Task.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(Task.COLUMN_ID)));
                task.setTask(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TASK)));
                task.setTimeStamp(cursor.getString(cursor.getColumnIndex(Task.COLUMN_TIMESTAMP)));

                taskList.add(task);
            } while (cursor.moveToNext());
        }

        sqLiteDatabase.close();
        return taskList;
    }

    public Cursor getData(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT * FROM " + Task.TABLE_NAME + " WHERE " + Task.COLUMN_ID + " = "+ id + "", null);
        return cursor;
    }

//    public int getTodoCount() {
//        String countQuery = "SELECT * FROM " + Task.TABLE_NAME;
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
//
//        int count = cursor.getCount();
//        cursor.close();
//        return count;
//    }

    public int updateTask(Task task) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Task.COLUMN_TASK, task.getTask());

        return sqLiteDatabase.update(Task.TABLE_NAME, contentValues, Task.COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    public void deleteTask(Task task) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Task.TABLE_NAME, Task.COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        sqLiteDatabase.close();
    }


}
