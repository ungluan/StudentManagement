package com.example.studentmanagement.database_sqlite.Dao;

import android.app.Application;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class GradeDao {
    private DataBaseHelper dataBaseHelper;

    public GradeDao(Application application) {
        // GetInstances
        this.dataBaseHelper = DataBaseHelper.getInstance(application);
    }

    public Boolean insertGrade(Grade grade) {
        return dataBaseHelper.insert(DataBaseHelper.TABLE_LOP, values(grade,false));
    }

    public Boolean updateGrade(Grade grade) {
        return dataBaseHelper.update(DataBaseHelper.TABLE_LOP,
                DataBaseHelper.COLUMN_LOP + "= '" + grade.getGradeId()+"'",
                values(grade,true), null);
    }

    public Boolean checkGradeId(String gradeId) {
        Cursor cursor = dataBaseHelper.query(
                "SELECT * FROM LOP WHERE LOP=? ", new String[]{gradeId});
        cursor.moveToFirst();
        return cursor.getCount() != 0;
    }

    public Boolean deleteGrade(String gradeId) {
        return dataBaseHelper.delete(DataBaseHelper.TABLE_LOP,
                DataBaseHelper.COLUMN_LOP + " = '" + gradeId+"'", null);
    }

    public int getNumberOfGrade() {
        Cursor cursor = dataBaseHelper
                .query("SELECT COUNT(*) FROM " + DataBaseHelper.TABLE_LOP, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public List<Grade> getGrades(){
        List<Grade> grades = new ArrayList<>();
        String query = "SELECT * FROM " + DataBaseHelper.TABLE_LOP;
        Cursor cursor = dataBaseHelper.query(query,null);
        if(cursor.moveToFirst()){
            do{
                String gradeId = cursor.getString(0);
                int teacherId = cursor.getInt(1);
                String image = cursor.getString(2);
                Grade grade = new Grade(gradeId,teacherId,image);
                grades.add(grade);
            }while (cursor.moveToNext());
        }
        return grades;
    }

    public ContentValues values(Grade grade, boolean noId) {
        ContentValues values = new ContentValues();
        if(!noId) values.put(DataBaseHelper.COLUMN_LOP, grade.getGradeId());
        values.put(DataBaseHelper.COLUMN_MA_CHU_NHIEM, grade.getTeacherId());
        values.put(DataBaseHelper.COLUMN_HINH_ANH, grade.getImage());
        return values;
    }
}
