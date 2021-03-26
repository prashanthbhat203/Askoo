package com.example.askoo.ui.completed_tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.askoo.CompletedTask;

import java.util.ArrayList;
import java.util.List;

public class CompletedTasksDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "completed_tasks_db";

    public CompletedTasksDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CompletedTask.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CompletedTask.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insertCompletedTasks(String completedTask) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CompletedTask.COLUMN_COMPLETED_TASK, completedTask);

        long id = sqLiteDatabase.insert(CompletedTask.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public CompletedTask getCompletedTasks(long id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(CompletedTask.TABLE_NAME, new String[]{CompletedTask.COLUMN_ID, CompletedTask.COLUMN_COMPLETED_TASK},
                CompletedTask.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        assert cursor != null;
        CompletedTask completedTask = new CompletedTask(
                cursor.getInt(cursor.getColumnIndex(CompletedTask.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(CompletedTask.COLUMN_COMPLETED_TASK)));
        cursor.close();
        return completedTask;
    }

    public List<CompletedTask> getAllCompletedTasks() {
        List<CompletedTask> completedTasks = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + CompletedTask.TABLE_NAME;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                CompletedTask task = new CompletedTask();
                task.setId(cursor.getInt(cursor.getColumnIndex(CompletedTask.COLUMN_ID)));
                task.setCompleted_tasks(cursor.getString(cursor.getColumnIndex(CompletedTask.COLUMN_COMPLETED_TASK)));

                completedTasks.add(task);
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return completedTasks;
    }
}
