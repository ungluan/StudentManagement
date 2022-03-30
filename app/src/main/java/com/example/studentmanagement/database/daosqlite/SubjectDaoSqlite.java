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
//    String tableMH, colMMH, colTenMH, colHeSo;
//    DataBaseHelper dataBaseHelper;
//    public SubjectDaoSqlite(Application application){
//        this.dataBaseHelper = new DataBaseHelper(application);
//        tableMH = dataBaseHelper.TABLE_MON_HOC;
//        colMMH = dataBaseHelper.COLUMN_MA_MON_HOC;
//        colTenMH = dataBaseHelper.COLUMN_TEN_MON_HOC;
//        colHeSo = dataBaseHelper.COLUMN_HE_SO;
//    }
//
//    public ArrayList<Subject> getAllSubject(){
//        ArrayList<Subject> list = new ArrayList<>();
//        Cursor cursor= dataBaseHelper.select("SELECT * FROM "+tableMH);
//        while (cursor.moveToNext()){
//            list.add(new Subject(cursor.getString(0)
//                    , cursor.getString(1), cursor.getInt(2)));
//        }
//        return list;
//    }
//    public int getNumberOfSubject(){
//        Cursor cursor = dataBaseHelper.select("SELECT COUNT(*) FROM " + tableMH);
//        if(cursor.moveToNext()) return cursor.getInt(0);
//        else return 0;
//    }
//    public boolean insert(Subject subject){
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(colMMH, subject.getId());
//        contentValues.put(colTenMH, subject.getSubjectName());
//        contentValues.put(colHeSo, subject.getCoefficient());
//
//        return dataBaseHelper.insert(tableMH,contentValues);
//    }
//
//    public boolean update(Subject subject){
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(colMMH, subject.getId());
//        contentValues.put(colTenMH, subject.getSubjectName());
//        contentValues.put(colHeSo, subject.getCoefficient());
//
//        return dataBaseHelper.update(tableMH,contentValues
//        ,colMMH+"=?", new String[]{subject.getId()});
//    }
//
//    public boolean delete(String subjectId){
//        dataBaseHelper.select("PRAGMA foreign_keys=ON");
//        return dataBaseHelper.delete(tableMH, colMMH+"=?", new String[]{subjectId});
//    }
}
