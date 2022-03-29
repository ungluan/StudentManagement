package com.example.studentmanagement.database_sqlite.Dao;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
        return dataBaseHelper.insert(DataBaseHelper.TABLE_HOC_SINH, values(student));
    }

    public Boolean updateStudent(Student student) {
        return dataBaseHelper.update(DataBaseHelper.TABLE_HOC_SINH,
                DataBaseHelper.COLUMN_MA_HOC_SINH + "=" + student.getId(),
                values(student), null);
    }

    public Boolean checkStudent(String studentId) {
        Cursor cursor = dataBaseHelper.query(
                "SELECT * FROM "+ DataBaseHelper.TABLE_HOC_SINH
                        +" WHERE "+ DataBaseHelper.COLUMN_MA_HOC_SINH +"=? ", new String[]{studentId});
        cursor.moveToFirst();
        return cursor.getCount() != 0;
    }

    public Boolean deleteStudent(String studentId) {
        return dataBaseHelper.delete(DataBaseHelper.TABLE_HOC_SINH,
                DataBaseHelper.COLUMN_LOP + " = " + studentId , null);
    }

    public int getNumberOfStudent() {
        Cursor cursor = dataBaseHelper
                .query("SELECT COUNT(*) FROM " + DataBaseHelper.TABLE_HOC_SINH, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public List<Student> getStudents(){
        List<Student> list = new ArrayList<>();
        Cursor cursor = dataBaseHelper.query("SELECT * FROM "+DataBaseHelper.TABLE_HOC_SINH,null);
        if(cursor.moveToFirst()){
            do{
                int studentId = cursor.getInt(0);
                String firstName = cursor.getString(1);
                String lastName = cursor.getString(2);
                String gender = cursor.getString(3);
                String birthdate = cursor.getString(4);
                String gradeId = cursor.getString(5);
                Student student = new Student(studentId,firstName,lastName,gender,birthdate,gradeId);
                list.add(student);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public ContentValues values(Student student) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_MA_HOC_SINH, student.getId());
        values.put(DataBaseHelper.COLUMN_HO, student.getFirstName());
        values.put(DataBaseHelper.COLUMN_TEN, student.getLastName());
        values.put(DataBaseHelper.COLUMN_PHAI, student.getGender());
        values.put(DataBaseHelper.COLUMN_NGAY_SINH, student.getBirthday());
        values.put(DataBaseHelper.COLUMN_LOP, student.getGradeId());
        return values;
    }

    public Student getStudent(int studentId) {
        Cursor cursor = dataBaseHelper.query("SELECT *"
                        + " FROM " + DataBaseHelper.TABLE_HOC_SINH
                        + " WHERE " + DataBaseHelper.COLUMN_MA_HOC_SINH + "=" + studentId
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
}
