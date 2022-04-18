package com.example.studentmanagement.database_sqlite.Dao;


import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkDao {

    private SQLiteDatabase db;
    private DataBaseHelper dataBaseHelper;
    private String tableMark, colStudentId, colSubjectId, colMark, colGrade, tableStudent;

    public MarkDao(Application application) {
        this.dataBaseHelper = DataBaseHelper.getInstance(application);
        db = dataBaseHelper.getWritableDatabase();
        tableMark = dataBaseHelper.TABLE_DIEM;
        colStudentId = dataBaseHelper.COLUMN_MA_HOC_SINH;
        colSubjectId = dataBaseHelper.COLUMN_MA_MON_HOC;
        colMark = dataBaseHelper.COLUMN_DIEM;
        colGrade = dataBaseHelper.COLUMN_LOP;
        tableStudent = dataBaseHelper.TABLE_HOC_SINH;
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
        return dataBaseHelper.insert(tableMark, values(mark));
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

    public ArrayList<Mark> getListMarkOfStudent(int studentId) {
        ArrayList<Mark> list = new ArrayList<>();
//        String sql = "SELECT MAHOCSINH, DIEM.MAMH, TENMH, HESO, DIEM FROM DIEM, Subject WHERE DIEM.MAMH=Subject.MAMH" +
//                " AND MAHOCSINH='" + studentId + "'";
        String sql = "SELECT " + DataBaseHelper.COLUMN_MA_HOC_SINH +
                ", " + DataBaseHelper.TABLE_MON_HOC + "." + DataBaseHelper.COLUMN_MA_MON_HOC +
                ", " + DataBaseHelper.COLUMN_TEN_MON_HOC +
                ", "+DataBaseHelper.COLUMN_HE_SO+
                ", " + DataBaseHelper.COLUMN_DIEM+
                ", " + DataBaseHelper.COLUMN_KHOI+
                ", " + DataBaseHelper.COLUMN_HINH_ANH +
                " FROM "+DataBaseHelper.TABLE_DIEM +", " + DataBaseHelper.TABLE_MON_HOC +
                " WHERE "+ DataBaseHelper.TABLE_DIEM+"."+DataBaseHelper.COLUMN_MA_MON_HOC+
                "="+DataBaseHelper.TABLE_MON_HOC+"." + DataBaseHelper.COLUMN_MA_MON_HOC+
                " AND MAHOCSINH='" + studentId + "'";
        Cursor cursor = dataBaseHelper.query(sql, null);
        while (cursor.moveToNext()) {
            Subject subject = new Subject(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(6),
                    cursor.getInt(5)
            );

            list.add(new Mark(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(4),
                    subject));
        }
        return list;
    }

    private ContentValues values(Mark mark) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(colStudentId, mark.getStudentId());
        contentValues.put(colSubjectId, mark.getSubjectId());
        contentValues.put(colMark, mark.getScore());
        return contentValues;
    }

    private List<Mark> getListMarkFromCursor(Cursor cursor) {
        List<Mark> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int studenId = cursor.getInt(0);
            String subjectId = cursor.getString(1);
            Double mark = cursor.getDouble(2);
            list.add(new Mark(studenId
                    , subjectId, mark));
        }
        return list;
    }

    public List<Mark> getAllMarks(){
        String sql = "SELECT * FROM " + DataBaseHelper.TABLE_DIEM ;
        Cursor cursor = dataBaseHelper.query(sql, null);
        return getListMarkFromCursor(cursor);

    }

    public Map<String, Integer> countRankStudent(){
        Map<String , Integer> countRank = new HashMap<>();
        Arrays.stream(ERankStudent.values())
                .forEach(rankStudent -> {
                    countRank.put(rankStudent.getRank(), 0);
                });

        getAllMarks().forEach(mark -> {
            String rank = mark.rankStudent();
            countRank.put(rank, countRank.get(rank) + 1);
        });
        return countRank;
    }


}