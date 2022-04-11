package com.example.studentmanagement.database_sqlite;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.core.database.sqlite.SQLiteDatabaseKt;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_LOP = "LOP";
    public static final String COLUMN_LOP = "LOP";
    public static final String COLUMN_HO = "HO";
    public static final String COLUMN_HINH_ANH = "HINHANH";


    public static final String TABLE_MON_HOC = "MONHOC";
    public static final String COLUMN_MA_MON_HOC = "MAMONHOC";
    public static final String COLUMN_TEN = "TEN";
    public static final String COLUMN_TEN_MON_HOC = "TENMONHOC";
    public static final String COLUMN_HE_SO = "HESO";

    public static final String TABLE_HOC_SINH = "HOCSINH";
    public static final String COLUMN_MA_HOC_SINH = "MAHOCSINH";
    public static final String COLUMN_PHAI = "PHAI";
    public static final String COLUMN_NGAY_SINH = "NGAYSINH";

    public static final String TABLE_DIEM = "DIEM";
    public static final String COLUMN_DIEM = "DIEM";

    public static final String TABLE_GVCN = "GVCN";
    public static final String COLUMN_MA_CHU_NHIEM = "MACHUNHIEM";
    public static final String COLUMN_TEN_CHU_NHIEM = "TENCHUNHIEM";
    public static final String COLUMN_MA_TAI_KHOAN = "MATAIKHOAN";

    public static final String TABLE_TAI_KHOAN = "TAIKHOAN";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_MAT_KHAU = "MATKHAU";


    public static DataBaseHelper INSTANCE = null;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /***
        - Login bằng giáo viên: Chỉ được nhập điểm môn học và lớp học mình dạy ->
        Giáo viên có 1 GV có nhiều lớp và 1 lớp có nhiều giáo viên
        - Nhập điểm: 1 GV có nhiều
     ***/

    private static final String createGradeTableStatement = "CREATE TABLE " + TABLE_LOP + "(\n" +
            "\t    " + COLUMN_LOP + " TEXT NOT NULL PRIMARY KEY, \n" +
            "\t    " + COLUMN_MA_CHU_NHIEM + " INTEGER, \n" +
            "     " + COLUMN_HINH_ANH + " TEXT, \n" +
            "        FOREIGN KEY (" + COLUMN_MA_CHU_NHIEM + ") REFERENCES " + TABLE_GVCN + "(" + COLUMN_MA_CHU_NHIEM + ")\n" +
            "        ON DELETE NO ACTION\n" +
            "        ON UPDATE CASCADE" +
            "    )";

    public static final String COLUMN_SO_DIEN_THOAI = "SODIENTHOAI";
    private static final String createTeacherTableStatement = "CREATE TABLE " + TABLE_GVCN + "(\n" +
            "\t    " + COLUMN_MA_CHU_NHIEM + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\t    " + COLUMN_TEN_CHU_NHIEM + " TEXT, \n" +
            "\t    " + COLUMN_MA_TAI_KHOAN + " TEXT, \n" +
            "     " + COLUMN_HINH_ANH + " TEXT, \n" +
            "     " + COLUMN_SO_DIEN_THOAI + " TEXT, \n" +
            "        FOREIGN KEY (" + COLUMN_MA_TAI_KHOAN + ") REFERENCES " + TABLE_TAI_KHOAN + "(" + COLUMN_MA_TAI_KHOAN + ")\n" +
            "        ON DELETE NO ACTION\n" +
            "        ON UPDATE CASCADE" +
            "    )";

    private static final String createAccountTableStatement = "CREATE TABLE " + TABLE_TAI_KHOAN + "(\n" +
            "\t    " + COLUMN_MA_TAI_KHOAN + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\t    " + COLUMN_EMAIL + " TEXT, \n" +
            "\t    " + COLUMN_MAT_KHAU + " TEXT " +
            "    )";

    private static final String createSubjectTableStatement = "CREATE TABLE " + TABLE_MON_HOC + "(\n" +
            "\t    " + COLUMN_MA_MON_HOC + " TEXT NOT NULL PRIMARY KEY,\n" +
            "\t    " + COLUMN_TEN_MON_HOC + " TEXT,\n" +
            "\t    " + COLUMN_HE_SO + " INTEGER,\n" +
            "     " + COLUMN_HINH_ANH + " TEXT \n" +

            "    )";

    private static final String createStudentTableStatement = "CREATE TABLE " + TABLE_HOC_SINH + "(\n" +
            "        " + COLUMN_MA_HOC_SINH + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "        " + COLUMN_HO + " TEXT,\n" +
            "        " + COLUMN_TEN + " TEXT,\n" +
            "        " + COLUMN_PHAI + " TEXT,\n" +
            "        " + COLUMN_NGAY_SINH + " TEXT,\n" +
            "        " + COLUMN_LOP + " TEXT ," +
            "     " + COLUMN_HINH_ANH + " TEXT, \n" +
            "        FOREIGN KEY (" + COLUMN_LOP + ") REFERENCES " + TABLE_LOP + "(" + COLUMN_LOP + ")\n" +
            "        ON DELETE NO ACTION\n" +
            "        ON UPDATE CASCADE" +
            "    )";

    private static final String createMarkTableStatement = "CREATE TABLE " + TABLE_DIEM + "(\n" +
            "        " + COLUMN_MA_HOC_SINH + " INTEGER NOT NULL,\n" +
            "        " + COLUMN_MA_MON_HOC + " TEXT NOT NULL ,\n" +
            "        " + COLUMN_DIEM + " REAL,\n" +
            "     " + COLUMN_HINH_ANH + " TEXT, \n" +
            "        PRIMARY KEY (" + COLUMN_MA_HOC_SINH + ", " + COLUMN_MA_MON_HOC + "),\n" +
            "        FOREIGN KEY (" + COLUMN_MA_HOC_SINH + ") REFERENCES " + TABLE_HOC_SINH + "(" + COLUMN_MA_HOC_SINH + ") \n" +
            "        ON DELETE NO ACTION\n" +
            "        ON UPDATE CASCADE, " +
            "        FOREIGN KEY (" + COLUMN_MA_MON_HOC + ") REFERENCES " + TABLE_MON_HOC + "(" + COLUMN_MA_MON_HOC + ")\n" +
            "        ON DELETE NO ACTION\n" +
            "        ON UPDATE CASCADE" +
            "    )";


    private DataBaseHelper(Application application) {
        super(application, "app_database_sqlite.db", null, 12);

    }

    public static synchronized DataBaseHelper getInstance(Application application) {
        if (INSTANCE == null) INSTANCE = new DataBaseHelper(application);
        return INSTANCE;
    }


    /*
    CREATE TABLE LOP(
	    LOP CHAR(4) NOT NULL PRIMARY KEY,
	    MACHUNHIEM NVARCHAR(50)
    )
    CREATE TABLE MONHOC(
	    MAMONHOC CHAR(10) NOT NULL PRIMARY KEY,
	    TENMONHOC NVARCHAR(20),
	    HESO INT
    )
    CREATE TABLE GVCN(
        MAGVCN INT NOT NULL PRIMARY KEY,
        TENGVCN CHAR(),
        MATAIKHOAN
    )
    CREATE TABLE TAIKHOAN(
        MATAIKHOAN,
        EMAIL,
        MATKHAU,
    )
    CREATE TABLE User(

        MAMONHOC CHAR(10),
        MAGIAOVIEN INT,
        MALOP CHAR(10)
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
        db.execSQL(createAccountTableStatement);
        db.execSQL(createTeacherTableStatement);
        db.execSQL(createGradeTableStatement);
        db.execSQL(createSubjectTableStatement);
        db.execSQL(createStudentTableStatement);
        db.execSQL(createMarkTableStatement);
        db.execSQL("PRAGMA foreign_keys = ON");
    }

    // This is called if the database version number change.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOC_SINH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MON_HOC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GVCN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAI_KHOAN);
        onCreate(db);
    }


    //insert, delete, update, select


    public boolean insert(String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        return db.insertOrThrow(tableName, null, values) > 0;
    }

    public boolean update(String tableName, String whereClause,
                          ContentValues values, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        return db.update(tableName, values, whereClause, whereArgs) > 0;
    }

    public boolean delete(String tableName, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON");
        try{
            db.delete(tableName, whereClause, whereArgs);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Cursor query(String query, String[] selectionArgs) {
        return this.getReadableDatabase().rawQuery(query, selectionArgs, null);
    }
}

