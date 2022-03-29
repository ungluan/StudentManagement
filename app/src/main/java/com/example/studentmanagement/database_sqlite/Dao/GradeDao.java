package com.example.studentmanagement.database_sqlite.Dao;

import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;

public class GradeDao {
    private SQLiteDatabase db;
    private DataBaseHelper dataBaseHelper;

    public GradeDao(Application application) {
        // GetInstances
        this.dataBaseHelper = DataBaseHelper.getInstance(application);
        this.db = dataBaseHelper.getWritableDatabase();
    }

    public Boolean insertGrade(Grade grade){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_LOP,grade.getGradeId());
        values.put(DataBaseHelper.COLUMN_CHU_NHIEM,grade.getTeacherName());
        long result = -1;
//        try{
            result = db.insertOrThrow(DataBaseHelper.TABLE_LOP,null,values);
//        }catch (SQLiteException e){
//            Log.d("HomeFragment","Exception in insertGrade "+e.getMessage());
//            Log.d("HomeFragment","GradeDao ThreadName"+ Thread.currentThread().getName() );
//        }
        return result != 1;
    }

    public Boolean deleteGrade(Grade grade){
        ContentValues values = new ContentValues();

    }


}
