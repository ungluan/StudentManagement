package com.example.studentmanagement.database_sqlite.Dao;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    private SQLiteDatabase db;
    private DataBaseHelper dataBaseHelper;

    public StudentDao(Application application) {
        // GetInstances
        this.dataBaseHelper = DataBaseHelper.getInstance(application);
        this.db = dataBaseHelper.getWritableDatabase();
    }

    public Boolean insertStudent(Student student) {
        return dataBaseHelper.insert(DataBaseHelper.TABLE_HOC_SINH, values(student,true));
    }

    public Boolean updateStudent(Student student) {
        return dataBaseHelper.update(DataBaseHelper.TABLE_HOC_SINH,
                DataBaseHelper.COLUMN_MA_HOC_SINH + "=" + student.getId(),
                values(student,false), null);
    }

    public Boolean checkStudent(String studentId) {
        Cursor cursor = dataBaseHelper.query(
                "SELECT * FROM "+ DataBaseHelper.TABLE_HOC_SINH
                        +" WHERE "+ DataBaseHelper.COLUMN_MA_HOC_SINH +"=? ", new String[]{studentId});
        cursor.moveToFirst();
        return cursor.getCount() != 0;
    }

    public boolean deleteStudent(int studentId) {
        return dataBaseHelper.delete(DataBaseHelper.TABLE_HOC_SINH,
                DataBaseHelper.COLUMN_MA_HOC_SINH + " = " + studentId , null);
    }

    public int getNumberOfStudent() {
        Cursor cursor = dataBaseHelper
                .query("SELECT COUNT(*) FROM " + DataBaseHelper.TABLE_HOC_SINH, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public List<Student> getStudentsByGradeId(String gradeId){
        List<Student> list = new ArrayList<>();
        String query = "SELECT * FROM "+DataBaseHelper.TABLE_HOC_SINH +
                " WHERE "+DataBaseHelper.COLUMN_LOP +" =?";
        Cursor cursor = dataBaseHelper.query(query,new String[]{gradeId});
        if(cursor.moveToFirst()){
            do{
                int studentId = cursor.getInt(0);
                String firstName = cursor.getString(1);
                String lastName = cursor.getString(2);
                String gender = cursor.getString(3);
                String birthdate = cursor.getString(4);
                Student student = new Student(studentId,firstName,lastName,gender,birthdate,gradeId);
                list.add(student);
            }while (cursor.moveToNext());
        }
        return list;
    }


    public ContentValues values(Student student, boolean noId) {
        ContentValues values = new ContentValues();
        if(!noId) values.put(DataBaseHelper.COLUMN_MA_HOC_SINH, student.getId());
        values.put(DataBaseHelper.COLUMN_HO, student.getFirstName());
        values.put(DataBaseHelper.COLUMN_TEN, student.getLastName());
        values.put(DataBaseHelper.COLUMN_PHAI, student.getGender());
        values.put(DataBaseHelper.COLUMN_NGAY_SINH, student.getBirthday());
        values.put(DataBaseHelper.COLUMN_LOP, student.getGradeId());
        return values;
    }
}
