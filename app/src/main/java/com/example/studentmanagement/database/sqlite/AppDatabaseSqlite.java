package com.example.studentmanagement.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.studentmanagement.database.AppDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AppDatabaseSqlite extends SQLiteOpenHelper {

    private static volatile AppDatabase INSTANCES;
    private static final int NUM_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUM_OF_THREADS);
    public AppDatabaseSqlite(@Nullable Context context) {
        super(context, "app_database_sql",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
