//package com.example.studentmanagement.database;
//
//
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//import androidx.sqlite.db.SupportSQLiteDatabase;
//
//import com.example.studentmanagement.database.dao.GradeDao;
//import com.example.studentmanagement.database.dao.MarkDao;
//import com.example.studentmanagement.database.dao.StudentDao;
//import com.example.studentmanagement.database.dao.SubjectDao;
//
//import com.example.studentmanagement.database.entity.Mark;
//import com.example.studentmanagement.database.entity.Student;
//import com.example.studentmanagement.database.entity.Subject;
//import com.example.studentmanagement.database.entity.Grade;
//
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//
//@Database(entities = {Grade.class, Subject.class, Student.class, Mark.class}, version = 6, exportSchema = false)
//public abstract class AppDatabase extends RoomDatabase {
//    public abstract GradeDao classDao();
//
//    public abstract SubjectDao subjectDao();
//
//    public abstract StudentDao studentDao();
//
//    public abstract MarkDao markDao();
//
//    private static volatile AppDatabase INSTANCES;
//    private static final int NUM_OF_THREADS = 4;
//    public static final ExecutorService databaseWriteExecutor =
//            Executors.newFixedThreadPool(NUM_OF_THREADS);
//
//
//    public static AppDatabase getDatabase(final Context context) {
//        if (INSTANCES == null) {
//            synchronized (AppDatabase.class) {
//                INSTANCES = Room.databaseBuilder(
//                        context,
//                        AppDatabase.class,
//                        "app_database"
//                ).createFromAsset("database/app_database.db")
//                        .fallbackToDestructiveMigration().build();
//            }
//        }
//        return INSTANCES;
//    }
//
//    private static RoomDatabase.Callback roomDatabaseCallBack = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            GradeDao dao = INSTANCES.classDao();
//
//            dao.insertGrade(new Grade("12A1", "Nguyễn Bích Thủy"));
//            dao.insertGrade(new Grade("12A2", "Lê Văn Hiền"));
//            dao.insertGrade(new Grade("12A3", "Trần Huy Hoàng"));
//        }
//    };
//}
