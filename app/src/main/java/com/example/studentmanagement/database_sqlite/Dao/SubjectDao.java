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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        int result = cursor.getInt(0);
        cursor.close();
        return result;
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
        cursor.close();
        return list;
    }

    public Map<String,Double> getListSubjectAndMarkByStudentId(int studentId){
        Map<String,Double> maps = new HashMap<String,Double>();
        String query = "SELECT MONHOC.MAMONHOC, DIEM.DIEM FROM MONHOC,DIEM \n" +
                " WHERE DIEM.MAHOCSINH = "+ studentId + " AND DIEM.MAMONHOC = MONHOC.MAMONHOC ";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                String subjectId = cursor.getString(0);
//                String subjectName = cursor.getString(1);
//                int coefficient = cursor.getInt(2);
                Double mark = cursor.getDouble(1);
//                Subject subject = new Subject(subjectId,subjectName,coefficient);
                maps.put(subjectId,mark);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return maps;
    }

    public List<Subject> getSubjectSubjectId(int studentId){
        List<Subject> list = new ArrayList<>();
        String query = "SELECT MONHOC.MAMONHOC, MONHOC.TENMONHOC , MONHOC.HESO FROM MONHOC,DIEM \n" +
                " WHERE DIEM.MAHOCSINH = "+ studentId + " AND DIEM.MAMONHOC = MONHOC.MAMONHOC ";

        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                String subjectId = cursor.getString(0);
                String subjectName = cursor.getString(1);
                int coefficient = cursor.getInt(2);
                Subject subject = new Subject(subjectId,subjectName,coefficient);
                list.add(subject);
            }while(cursor.moveToNext());
        }
        cursor.close();
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
