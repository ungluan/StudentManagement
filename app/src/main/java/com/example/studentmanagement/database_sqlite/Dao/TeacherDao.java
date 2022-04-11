package com.example.studentmanagement.database_sqlite.Dao;

import android.app.Application;
import android.database.Cursor;
import android.view.contentcapture.DataRemovalRequest;

import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class TeacherDao {
    private DataBaseHelper dataBaseHelper;

    public TeacherDao(Application application) {
        // GetInstances
        this.dataBaseHelper = DataBaseHelper.getInstance(application);
    }

    public List<Teacher> getTeachers(){
        String query = "SELECT * from " + DataBaseHelper.TABLE_GVCN;
        Cursor cursor = dataBaseHelper.query(query,null);
        List<Teacher> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Teacher teacher = new Teacher(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                list.add(teacher);
            }while (cursor.moveToNext());
        }
        return list;
    }
    public Teacher getTeacherById(int teacherId){
        Cursor cursor = dataBaseHelper.query(
                "SELECT * FROM "+ DataBaseHelper.TABLE_GVCN +" WHERE "+
                        DataBaseHelper.COLUMN_MA_CHU_NHIEM +" = "+teacherId,null);
        Teacher teacher = new Teacher();
        if(cursor.moveToFirst()){
            teacher.setId(cursor.getInt(0));
            teacher.setTeacherName(cursor.getString(1));
            teacher.setIdAccount(cursor.getInt(2));
            teacher.setImageUrl(cursor.getString(3));
            teacher.setPhone(cursor.getString(4));
        }
        return teacher;
    }
    public List<Teacher> getTeacherHaveNotGrade(){
        String query = "SELECT * from " + DataBaseHelper.TABLE_GVCN+
                " WHERE " + DataBaseHelper.COLUMN_MA_CHU_NHIEM +" NOT IN "
                + "( SELECT "+DataBaseHelper.COLUMN_MA_CHU_NHIEM+" FROM "
                +DataBaseHelper.TABLE_LOP +")";
        Cursor cursor = dataBaseHelper.query(query,null);
        List<Teacher> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Teacher teacher = new Teacher(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                list.add(teacher);
            }while (cursor.moveToNext());
        }
        return list;
    }
}
