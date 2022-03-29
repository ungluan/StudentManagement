package com.example.studentmanagement.database_sqlite.Dao;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;

import java.util.ArrayList;

public class MarkDao {
    private static DataBaseHelper dataBaseHelper;
    private String tableMark, colStudentId, colSubjectId, colMark, colGrade, tableStudent;

    public MarkDao(Application application) {
        this.dataBaseHelper = DataBaseHelper.getInstance(application);
        tableMark = dataBaseHelper.TABLE_DIEM;
        colStudentId = dataBaseHelper.COLUMN_MA_HOC_SINH;
        colSubjectId = dataBaseHelper.COLUMN_MA_MON_HOC;
        colMark = dataBaseHelper.COLUMN_DIEM;
        colGrade = dataBaseHelper.COLUMN_LOP;
        tableStudent = dataBaseHelper.TABLE_HOC_SINH;
    }


    public Student getStudentByMark(int studentId, String subjectId) {
        Cursor cursor = dataBaseHelper.query("SELECT *"
                + " FROM " + tableStudent
                + " WHERE " + colStudentId + "=" + studentId
                ,null);
        if (cursor.moveToNext()) {
            return new Student(cursor.getInt(0)//id
                    , cursor.getString(1)//ho
                    , cursor.getString(2)//ten
                    , cursor.getString(3)//phai
                    , cursor.getString(4)//birthday
                    , cursor.getString(5)); // gradeId
        }
        Log.d("MarkDaoSqlite:", "getStudentByMark method");
        return null;
    }

    public ArrayList<Mark> getMarkByGradeAndSubject(String gradeId, String subjectId) {
        ArrayList<Mark> list = new ArrayList<>();
        Cursor cursor = dataBaseHelper.query("SELECT D.*"
                + " FROM (SELECT *  FROM " + tableMark + " WHERE " + colSubjectId + "='" + subjectId + "') as D,"
                + " (SELECT " + colStudentId + " FROM " + tableStudent + " WHERE " + colGrade + "='" + gradeId + "') AS HS" +
                " WHERE D." + colStudentId + " = HS." + colStudentId, null);
        while (cursor.moveToNext()) {
            int studenId = cursor.getInt(0);
            Double mark = cursor.getDouble(2);
            list.add(new Mark(studenId
                    , subjectId, mark));
        }
        return list;
    }

    public boolean insert(Mark mark) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(colStudentId, mark.getStudentId());
        contentValues.put(colSubjectId, mark.getSubjectId());
        contentValues.put(colMark, mark.getScore());

        return dataBaseHelper.insert(tableMark, contentValues);
    }

    public boolean update(Mark mark) {


        return dataBaseHelper.update(dataBaseHelper.TABLE_DIEM
                , colStudentId + "=? AND " + colSubjectId + "=?"
                , values(mark)
                , new String[]{
                        Integer.toString(mark.getStudentId())
                        , mark.getSubjectId()});
    }

    public boolean delete(Mark mark) {
        dataBaseHelper.query("PRAGMA foreign_keys=ON", null);
        return dataBaseHelper.delete(
                tableMark
                , colStudentId + "=? AND " + colSubjectId + "=?"
                , new String[]{
                        Integer.toString(mark.getStudentId())
                        , mark.getSubjectId()});
    }

    private ContentValues values(Mark mark) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(colStudentId, mark.getStudentId());
        contentValues.put(colSubjectId, mark.getSubjectId());
        contentValues.put(colMark, mark.getScore());
        return contentValues;
    }

}
