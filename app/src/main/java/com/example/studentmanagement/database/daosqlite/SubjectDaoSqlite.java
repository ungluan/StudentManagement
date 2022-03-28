package com.example.studentmanagement.database.daosqlite;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.studentmanagement.database.dao.SubjectDao;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;

import java.util.ArrayList;

public class SubjectDaoSqlite {
    String TABLE_NAME = "MONHOC";
    DataBaseHelper dataBaseHelper;
    public SubjectDaoSqlite(Application application){
        this.dataBaseHelper = new DataBaseHelper(application);
    }

    public ArrayList<Subject> getAllSubject(){
        ArrayList<Subject> list = new ArrayList<>();
        Cursor cursor= dataBaseHelper.select("SELECT * FROM MONHOC");
        while (cursor.moveToNext()){
            list.add(new Subject(cursor.getString(0)
                    , cursor.getString(1), cursor.getInt(2)));
        }
        return list;
    }

    public boolean insert(Subject subject){
        ContentValues contentValues = new ContentValues();
        contentValues.put("MAMH", subject.getId());
        contentValues.put("TENMH", subject.getSubjectName());
        contentValues.put("HESO", subject.getCoefficient());

        return dataBaseHelper.insert(TABLE_NAME,contentValues);
    }

    public boolean update(Subject subject){
        ContentValues contentValues = new ContentValues();
        contentValues.put("MAMH", subject.getId());
        contentValues.put("TENMH", subject.getSubjectName());
        contentValues.put("HESO", subject.getCoefficient());

        return dataBaseHelper.update(TABLE_NAME,contentValues
        ,"MAMH=?", new String[]{subject.getId()});
    }

    public boolean delete(Subject subject){
        dataBaseHelper.select("PRAGMA foreign_keys=ON");
        return dataBaseHelper.delete(TABLE_NAME, "MAMH=?", new String[]{subject.getId()});
    }
}
