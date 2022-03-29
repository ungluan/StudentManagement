package com.example.studentmanagement.database_sqlite;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.studentmanagement.database.entity.Grade;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_LOP = "LOP";
    public static final String COLUMN_CHU_NHIEM = "CHUNHIEM";
    public static final String COLUMN_LOP = "LOP";
    public static final String COLUMN_HO = "HO";

    public static final String TABLE_MON_HOC = "MONHOC";
    public static final String COLUMN_MA_MON_HOC = "MAMONHOC";
    public static final String COLUMN_TEN = "TEN";
    public static final String COLUMN_TEN_MON_HOC = "TENMONHOC";
    public static final String COLUMN_HE_SO = "HESO";

    public static final String TABLE_HOC_SINH = "HOCSINH";
    public static final String COLUMN_MA_HOC_SINH = "MAHOCSINH";
    public static final String COLUMN__PHAI = "PHAI";
    public static final String COLUMN_NGAY_SINH = "NGAYSINH";

    public static final String TABLE_DIEM = "DIEM";
    public static final String COLUMN_DIEM = "DIEM";

    public static DataBaseHelper INSTANCE = null;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static final String createGradeTableStatement = "CREATE TABLE " + TABLE_LOP + "(\n" +
            "\t    " + COLUMN_LOP + " TEXT NOT NULL PRIMARY KEY, \n" +
            "\t    " + COLUMN_CHU_NHIEM + " TEXT\n" +
            "    )";

    private static final String createSubjectTableStatement = "CREATE TABLE " + TABLE_MON_HOC + "(\n" +
            "\t    " + COLUMN_MA_MON_HOC + " TEXT NOT NULL PRIMARY KEY,\n" +
            "\t    " + COLUMN_TEN_MON_HOC + " TEXT,\n" +
            "\t    " + COLUMN_HE_SO + " INTEGER\n" +
            "    )";

    private static final String createStudentTableStatement = "CREATE TABLE " + TABLE_HOC_SINH + "(\n" +
            "        " + COLUMN_MA_HOC_SINH + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "        " + COLUMN_HO + " TEXT,\n" +
            "        " + COLUMN_TEN + " TEXT,\n" +
            "        " + COLUMN__PHAI + " TEXT,\n" +
            "        " + COLUMN_NGAY_SINH + " TEXT,\n" +
            "        " + COLUMN_LOP + " TEXT ," +
            "        FOREIGN KEY (" + COLUMN_LOP + ") REFERENCES " + TABLE_LOP + "(" + COLUMN_LOP + ")\n" +
            "        ON DELETE NO ACTION\n" +
            "        ON UPDATE CASCADE" +
            "    )";

    private static final String createMarkTableStatement = "CREATE TABLE " + TABLE_DIEM + "(\n" +
            "        " + COLUMN_MA_HOC_SINH + " INTEGER NOT NULL,\n" +
            "        " + COLUMN_MA_MON_HOC + " TEXT NOT NULL ,\n" +
            "        " + COLUMN_DIEM + " REAL,\n" +
            "        PRIMARY KEY (" + COLUMN_MA_HOC_SINH + ", " + COLUMN_MA_MON_HOC + "),\n" +
            "        FOREIGN KEY (" + COLUMN_MA_HOC_SINH + ") REFERENCES " + TABLE_HOC_SINH + "(" + COLUMN_MA_HOC_SINH + ") \n" +
            "        ON DELETE NO ACTION\n" +
            "        ON UPDATE CASCADE, " +
            "        FOREIGN KEY (" + COLUMN_MA_MON_HOC + ") REFERENCES " + TABLE_MON_HOC + "(" + COLUMN_MA_MON_HOC + ")\n" +
            "        ON DELETE NO ACTION\n" +
            "        ON UPDATE CASCADE" +
            "    )";

    private DataBaseHelper(Application application) {
        super(application, "app_database_sqlite.db", null, 1);
    }

    public static synchronized DataBaseHelper getInstance(Application application) {
        if (INSTANCE == null) INSTANCE = new DataBaseHelper(application);
        return INSTANCE;
    }


    /*
    CREATE TABLE LOP(
	    LOP CHAR(4) NOT NULL PRIMARY KEY,
	    CHUNHIEM NVARCHAR(50)
    )
    CREATE TABLE MONHOC(
	    MAMONHOC CHAR(10) NOT NULL PRIMARY KEY,
	    TENMONHOC NVARCHAR(20),
	    HESO INT
    )
    CREATE TABLE HOCSINH(
        MAHOCSINH INT NOT NULL PRIMARY KEY,
        HO NVARCHAR(50),
        TEN NVARCHAR(10),
        PHAI NVARCHAR(3),
        NGAYSINH VARCHAR(20),
        LOP CHAR(4) FOREIGN KEY REFERENCES LOP(LOP)
    )
    CREATE TABLE DIEM(
        MAHOCSINH INT NOT NULL,
        MAMONHOC CHAR(10) NOT NULL,
        DIEM FLOAT,
        PRIMARY KEY (MAHOCSINH, MAMONHOC),
        FOREIGN KEY (contact_id)
      REFERENCES contacts (contact_id)
         ON DELETE CASCADE
         ON UPDATE NO ACTION,
   FOREIGN KEY (group_id)
      REFERENCES groups (group_id)
         ON DELETE CASCADE
         ON UPDATE NO ACTION
    )
    */
    // This is called if the first time database is accessed.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createGradeTableStatement);
        db.execSQL(createSubjectTableStatement);
        db.execSQL(createStudentTableStatement);
        db.execSQL(createMarkTableStatement);
        db.rawQuery("PRAGMA foreign_keys = ON", null);
    }

    // This is called if the database version number change.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOC_SINH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MON_HOC);
        onCreate(db);
    }

    public int getNumberOfGrade() {
        String query = "SELECT COUNT(*) FROM " + TABLE_LOP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        System.out.println(cursor.getCount());
        System.out.println(cursor.getInt(0));
        Log.d("HomeFragment", "ThreadName on Executorx: " + Thread.currentThread().getName());
        return cursor.getInt(0);
    }

    public boolean insert(String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insertOrThrow(tableName, null, values) > 0;
    }

    public boolean update(String tableName,String whereClause,
                          ContentValues values, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(tableName, values, whereClause, whereArgs) > 0;
    }

    public boolean delete(String tableName,String whereClause ,
                          String gradeId, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(tableName, whereClause, whereArgs) > 0;
    }
    public Cursor query(String query, String[] selectionArgs){
        return this.getReadableDatabase().rawQuery(query,selectionArgs,null);
    }
}