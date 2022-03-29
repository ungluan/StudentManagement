package com.example.studentmanagement.database_sqlite.Dao;


import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkDao {
    private SQLiteDatabase db;
    private DataBaseHelper dataBaseHelper;

    public MarkDao(Application application) {
        // GetInstances
        this.dataBaseHelper = DataBaseHelper.getInstance(application);
        this.db = dataBaseHelper.getWritableDatabase();
    }

    public Boolean deleteAndInsertMark(List<Mark> marks, List<String> delList){
        db.beginTransaction();
        try{
            String queryDelete = "DELETE FROM MARK WHERE MAMONHOC IN ( "+
                    listToString(delList) +" ) ";
            String queryInsert = "INSERT INTO DIEM VALUES "+listMarkToString(marks);
            dataBaseHelper.query(queryDelete,null);
            dataBaseHelper.query(queryInsert,null);
            db.endTransaction();
            return true;
        }catch (Exception e){
            db.endTransaction();
            return false;
        }
    }
    public String stringMark(Mark mark){
        return "("+mark.getStudentId()+", '"+mark.getSubjectId()+"', "+mark.getScore()+"),";
    }
    public String listMarkToString(List<Mark> marks){
        String s ="" ;
        for (int i=0 ; i<marks.size(); i++){
            s += stringMark(marks.get(i));
        }
        return s.substring(0,s.length()-1);
    }
    public String listToString(List<String> delList){
        if(delList.size()==0) return "";
        String s = delList.toString();
        s = s.substring(1,s.length()-1);
        return s;
    }

    public ContentValues values(Mark mark) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_MA_HOC_SINH, mark.getStudentId());
        values.put(DataBaseHelper.COLUMN_MA_MON_HOC, mark.getSubjectId());
        values.put(DataBaseHelper.COLUMN_DIEM, mark.getScore());
        return values;
    }

}

