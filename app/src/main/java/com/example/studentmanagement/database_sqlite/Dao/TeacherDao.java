package com.example.studentmanagement.database_sqlite.Dao;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.studentmanagement.database.entity.Account;
import com.example.studentmanagement.database.entity.Student;
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
    public Boolean insertTeacher(Teacher teacher) {
        return dataBaseHelper.insert(DataBaseHelper.TABLE_GVCN, values(teacher,true));
    }

    public Boolean updateTeacher(Teacher teacher) {
        return dataBaseHelper.update(DataBaseHelper.TABLE_GVCN,
                DataBaseHelper.COLUMN_MA_CHU_NHIEM + "=" + teacher.getId(),
                values(teacher,false), null);
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
    public int getIdTeacherByEmail(String email){
        String query = "SELECT "+ DataBaseHelper.COLUMN_MA_CHU_NHIEM +" FROM "
                + DataBaseHelper.TABLE_GVCN +", "+DataBaseHelper.TABLE_TAI_KHOAN +" WHERE "+
                DataBaseHelper.TABLE_GVCN +"."+ DataBaseHelper.COLUMN_MA_TAI_KHOAN +" = "+
                DataBaseHelper.TABLE_TAI_KHOAN +"."+ DataBaseHelper.COLUMN_MA_TAI_KHOAN +" AND "+
                DataBaseHelper.COLUMN_EMAIL +" = '"+email+"'";
        Cursor cursor = dataBaseHelper.query(query,null);
        int teacherId=-1;
        if(cursor.moveToFirst()){
            teacherId = cursor.getInt(0);
        }
        return teacherId;
    }
    public Teacher getTeacherByEmail(String email){
        String query = "SELECT "+ DataBaseHelper.COLUMN_MA_CHU_NHIEM+","+ DataBaseHelper.COLUMN_TEN_CHU_NHIEM
                +","+ DataBaseHelper.TABLE_GVCN+"."+DataBaseHelper.COLUMN_MA_TAI_KHOAN +","+ DataBaseHelper.COLUMN_HINH_ANH+
                ","+ DataBaseHelper.COLUMN_SO_DIEN_THOAI+" FROM "
                + DataBaseHelper.TABLE_GVCN +", "+DataBaseHelper.TABLE_TAI_KHOAN +" WHERE "+
                DataBaseHelper.TABLE_GVCN +"."+ DataBaseHelper.COLUMN_MA_TAI_KHOAN +" = "+
                DataBaseHelper.TABLE_TAI_KHOAN +"."+ DataBaseHelper.COLUMN_MA_TAI_KHOAN +" AND "+
                DataBaseHelper.COLUMN_EMAIL +" = '"+email+"'";
        Cursor cursor = dataBaseHelper.query(query,null);
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
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<Teacher> searchTeacherBySameNameOrPhoneOrAccount(String search) {

        String query = "SELECT * FROM " + DataBaseHelper.TABLE_GVCN +
                " WHERE " + DataBaseHelper.COLUMN_TEN_CHU_NHIEM +
                " LIKE ? OR " + DataBaseHelper.COLUMN_SO_DIEN_THOAI +
                " LIKE ? OR " + DataBaseHelper.COLUMN_MA_TAI_KHOAN +
                " LIKE ?";
        Cursor cursor = dataBaseHelper.query(query, new String[]{"%" + search + "%", "%" + search + "%", "%" + search + "%"});

        return getTeacherFromCursor(cursor);

    }

    public Boolean insertTeacher2(Teacher teacher) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_TEN_CHU_NHIEM, teacher.getTeacherName());
        values.put(DataBaseHelper.COLUMN_SO_DIEN_THOAI, teacher.getPhone());
        values.put(DataBaseHelper.COLUMN_HINH_ANH, teacher.getImageUrl());
        return dataBaseHelper.insert(DataBaseHelper.TABLE_GVCN, values);
    }

    public Boolean updateTeacher2(Teacher teacher) {
        return dataBaseHelper.update(DataBaseHelper.TABLE_GVCN,
                DataBaseHelper.COLUMN_MA_CHU_NHIEM + "=" + teacher.getId(),
                values(teacher), null);
    }

    public Boolean deleteTeacher(int teacherId) {
        return dataBaseHelper.delete(DataBaseHelper.TABLE_GVCN,
                DataBaseHelper.COLUMN_MA_CHU_NHIEM + " = " + teacherId, null);
    }

    public ContentValues values(Teacher teacher) {
        ContentValues values = new ContentValues();
//        values.put(DataBaseHelper.COLUMN_MA_CHU_NHIEM, teacher.getId());
        values.put(DataBaseHelper.COLUMN_TEN_CHU_NHIEM, teacher.getTeacherName());
        values.put(DataBaseHelper.COLUMN_SO_DIEN_THOAI, teacher.getPhone());
        values.put(DataBaseHelper.COLUMN_HINH_ANH, teacher.getImageUrl());
        if(teacher.checkAccount()) values.put(DataBaseHelper.COLUMN_MA_TAI_KHOAN, teacher.getIdAccount());
        return values;
    }

    private List<Teacher> getTeacherFromCursor(Cursor cursor) {
        List<Teacher> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(
                    new Teacher(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getString(3),
                            cursor.getString(4)));

        }
        return list;
    }

    public Teacher findById(int id) {
        String query = "SELECT * FROM " + DataBaseHelper.TABLE_GVCN +
                " WHERE " + DataBaseHelper.COLUMN_MA_CHU_NHIEM +
                " = ?";
        Cursor cursor = dataBaseHelper.query(query, new String[]{Integer.toString(id)});
        if (cursor.moveToNext())
            return new Teacher(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4));
        else return null;

    }

    public ContentValues values(Teacher teacher, boolean noId) {
        ContentValues values = new ContentValues();
        if(!noId) values.put(DataBaseHelper.COLUMN_MA_CHU_NHIEM, teacher.getId());
        values.put(DataBaseHelper.COLUMN_TEN_CHU_NHIEM, teacher.getTeacherName());
        values.put(DataBaseHelper.COLUMN_HINH_ANH, teacher.getImageUrl());
        values.put(DataBaseHelper.COLUMN_MA_TAI_KHOAN, teacher.getIdAccount());
        values.put(DataBaseHelper.COLUMN_SO_DIEN_THOAI,teacher.getPhone());
        return values;
    }

    public int getNumberOfTeachers() {
        Cursor cursor = dataBaseHelper
                .query("SELECT COUNT(*) FROM " + DataBaseHelper.TABLE_GVCN, null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        cursor.close();
        return result;
    }
}
