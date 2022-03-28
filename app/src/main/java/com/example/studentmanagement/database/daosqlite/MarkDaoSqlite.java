package com.example.studentmanagement.database.daosqlite;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;

import java.util.ArrayList;

public class MarkDaoSqlite {
    String TABLE_NAME = "DIEM";
    DataBaseHelper dataBaseHelper;
    public MarkDaoSqlite(Application application){
        this.dataBaseHelper = new DataBaseHelper(application);
    }


    public Student getStudentByMark(String studentId){
        Cursor cursor= dataBaseHelper.select("SELECT * FROM HOCSINH WHERE MAHOCSINH='"+studentId+"'");
        if(cursor.moveToNext()){
//            return Student(cursor.getString(0), cursor.getString(1), cursor.getString(2),
//                    cursor.getInt(3)>0, date, lop));
        }
        return null;
    }
    public ArrayList<Mark> getMarkByGradeAndSubject(String gradeId, String subjectId){
        ArrayList<Mark> list = new ArrayList<>();
        Cursor cursor= dataBaseHelper.select("SELECT D.* FROM (SELECT * FROM DIEM WHERE MAMONHOC='"+subjectId+"') as D," +
                " (SELECT MAHOCSINH FROM HOCSINH WHERE LOP='"+gradeId+"') AS HS" +
                " WHERE D.MAHOCSINH = HS.MAHOCSINH");
        while (cursor.moveToNext()){
            int studenId = cursor.getInt(0);
            Double mark = cursor.getDouble(2);
            list.add(new Mark(studenId
                    , subjectId, mark));
        }
        return list;
    }

    public boolean insert(Mark mark){
        ContentValues contentValues = new ContentValues();
        contentValues.put("MAHOCSINH", mark.getStudentId());
        contentValues.put("MAMH", mark.getSubjectId());
        contentValues.put("DIEM", mark.getScore());

        return dataBaseHelper.insert(TABLE_NAME,contentValues);
    }

    public boolean update(Mark mark){
        ContentValues contentValues = new ContentValues();
        contentValues.put("MAHOCSINH", mark.getStudentId());
        contentValues.put("MAMH", mark.getSubjectId());
        contentValues.put("DIEM", mark.getScore());


        return dataBaseHelper.update(TABLE_NAME,contentValues
                ,"MAHOCSINH=? AND MAMH=?"
                , new String[]{
                        Integer.toString(mark.getStudentId())
                        , mark.getSubjectId()});
    }

    public boolean delete(Mark mark){
        dataBaseHelper.select("PRAGMA foreign_keys=ON");
        return dataBaseHelper.delete(TABLE_NAME
                , "MAHOCSINH=? AND MAMH=?"
                , new String[]{
                        Integer.toString(mark.getStudentId())
                        , mark.getSubjectId()});
    }
}
