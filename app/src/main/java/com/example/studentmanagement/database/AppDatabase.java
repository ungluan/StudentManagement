package com.example.studentmanagement.database;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.studentmanagement.database.dao.GradeDao;
import com.example.studentmanagement.database.dao.SubjectDao;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database.entity.Grade;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Grade.class, Subject.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GradeDao classDao();

    public abstract SubjectDao subjectDao();

    private static volatile AppDatabase INSTANCES;
    private static final int NUM_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUM_OF_THREADS);


    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCES == null) {
            synchronized (AppDatabase.class) {
                INSTANCES = Room.databaseBuilder(
                        context,
                        AppDatabase.class,
                        "word_database"
                ).build();
            }
        }
        return INSTANCES;
    }

    private static RoomDatabase.Callback roomDatabaseCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            GradeDao dao = INSTANCES.classDao();

            dao.insert(new Grade(1, "12A1", "Nguyễn Bích Thủy"));
            dao.insert(new Grade(1, "12A2", "Lê Văn Hiền"));
            dao.insert(new Grade(1, "12A3", "Trần Huy Hoàng"));
        }
    };
}
