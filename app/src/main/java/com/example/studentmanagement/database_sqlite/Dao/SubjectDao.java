package com.example.studentmanagement.database_sqlite.Dao;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SubjectDao {
    private SQLiteDatabase db;
    private DataBaseHelper dataBaseHelper;

    public SubjectDao(Application application) {
        // GetInstances
        this.dataBaseHelper = DataBaseHelper.getInstance(application);
        this.db = dataBaseHelper.getWritableDatabase();
    }

    public Boolean insertSubject(Subject subject) {
        return dataBaseHelper.insert(DataBaseHelper.TABLE_MON_HOC, values(subject));
    }

    public Boolean updateSubject(Subject subject) {
        return dataBaseHelper.update(DataBaseHelper.TABLE_MON_HOC,
                DataBaseHelper.COLUMN_MA_MON_HOC + "=" + subject.getId(),
                values(subject), null);
    }

    // Khoong '' co loi hong
    public Boolean checkSubject(String subjectId) {
        Cursor cursor = dataBaseHelper.query(
                "SELECT * FROM "+ DataBaseHelper.TABLE_MON_HOC
                        +" WHERE "+ DataBaseHelper.COLUMN_MA_MON_HOC +"=? ", new String[]{subjectId});
        cursor.moveToFirst();
        return cursor.getCount() != 0;
    }

    // Khoong '' co loi hong
    public Boolean deleteSubject(String subjectId) {
        return dataBaseHelper.delete(DataBaseHelper.TABLE_MON_HOC,
                DataBaseHelper.COLUMN_MA_MON_HOC + " = " + subjectId , null);
    }

    public int getNumberOfSubject() {
        Cursor cursor = dataBaseHelper
                .query("SELECT COUNT(*) FROM " + DataBaseHelper.TABLE_MON_HOC, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public List<Subject> getSubjects(){
        List<Subject> list = new ArrayList<>();
        Cursor cursor = dataBaseHelper.query("SELECT * FROM "+DataBaseHelper.TABLE_MON_HOC,null);
        if(cursor.moveToFirst()){
            do{
                String subjectId = cursor.getString(0);
                String subjectName = cursor.getString(1);
                int coefficient = cursor.getInt(2);

                Subject subject = new Subject(subjectId,subjectName,coefficient);
                list.add(subject);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public ContentValues values(Subject subject) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_MA_MON_HOC, subject.getId());
        values.put(DataBaseHelper.COLUMN_TEN_MON_HOC, subject.getSubjectName());
        values.put(DataBaseHelper.COLUMN_HE_SO, subject.getCoefficient());
        return values;
    }
}
