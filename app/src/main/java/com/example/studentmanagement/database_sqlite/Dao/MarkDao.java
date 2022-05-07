package com.example.studentmanagement.database_sqlite.Dao;


import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.MarkDTO;
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
        try{
            if(!delList.isEmpty()) {
                String queryDelete = "DELETE FROM DIEM WHERE MAMONHOC IN ( "+
                        listToString(delList) +" ) ";
                Log.d("Mark","'"+queryDelete+"'");
                dataBaseHelper.execSql(queryDelete);

            }
            if(marks.size()!=0){
                String queryInsert = "INSERT INTO DIEM (MAHOCSINH, MAMONHOC, DIEM) VALUES "+listMarkToString(marks);
                Log.d("Mark","'"+queryInsert+"'");

                dataBaseHelper.execSql(queryInsert);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public String stringMark(Mark mark){

        return "("+mark.getStudentId()+", '"+mark.getSubjectId()+"', "+mark.getScore()+ "),";
    }
    public String listMarkToString(List<Mark> marks){
        String s ="" ;
        for (int i=0 ; i<marks.size(); i++){
            s += stringMark(marks.get(i));
        }
        return s.substring(0,s.length()-1);
    }
    public String stringSub(String s){
        return "'"+s+"'";
    }
    public String listToString(List<String> delList){
        if(delList.size()==0) return "";
        String s ="";
        for(int i=0; i<delList.size() ; i++){
            s += stringSub(delList.get(i))+",";
        }
        s= s.substring(0,s.length()-1);
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
                ", "+DataBaseHelper.TABLE_MON_HOC + "." + DataBaseHelper.COLUMN_HINH_ANH +
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

    public ArrayList<MarkDTO> getMarkDTOByGradeAndSubject(String gradeId, String subjectId) {
        ArrayList<MarkDTO> list = new ArrayList<>();
        Cursor cursor = dataBaseHelper.query("SELECT HS." + DataBaseHelper.COLUMN_MA_HOC_SINH +
                ", " + DataBaseHelper.COLUMN_HO + ", " + DataBaseHelper.COLUMN_TEN +
                ", " + DataBaseHelper.COLUMN_PHAI + ", "+ DataBaseHelper.COLUMN_NGAY_SINH+
                ", HS."+ DataBaseHelper.COLUMN_HINH_ANH + ", " + DataBaseHelper.COLUMN_LOP +
                ", " + DataBaseHelper.COLUMN_MA_MON_HOC + ", " + DataBaseHelper.COLUMN_DIEM +
                 " FROM (SELECT *  FROM " + DataBaseHelper.TABLE_DIEM + " WHERE " + DataBaseHelper.COLUMN_MA_MON_HOC +
                "='" + subjectId + "') as D," +
                " (SELECT * FROM " + DataBaseHelper.TABLE_HOC_SINH + " WHERE " + DataBaseHelper.COLUMN_LOP
                + "='" + gradeId + "') AS HS" +
                " WHERE D." + DataBaseHelper.COLUMN_MA_HOC_SINH + " = HS." + DataBaseHelper.COLUMN_MA_HOC_SINH





                , null);
        while (cursor.moveToNext()) {
            list.add(new MarkDTO(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getDouble(8)));
        }
        return list;
    }


    public List<MarkDTO> searchMarkByStudentAndScore(String query, String gradeId, String subjectId) {
        ArrayList<MarkDTO> list = new ArrayList<>();
        int studentId;
        double score;
        String searchLike = "%" + query + "%";
        Cursor cursor = dataBaseHelper.query("SELECT HS." + DataBaseHelper.COLUMN_MA_HOC_SINH +
                ", " + DataBaseHelper.COLUMN_HO + ", " + DataBaseHelper.COLUMN_TEN +
                ", " + DataBaseHelper.COLUMN_PHAI + ", "+ DataBaseHelper.COLUMN_NGAY_SINH+
                ", HS."+ DataBaseHelper.COLUMN_HINH_ANH + ", " + DataBaseHelper.COLUMN_LOP +
                ", " + DataBaseHelper.COLUMN_MA_MON_HOC + ", " + DataBaseHelper.COLUMN_DIEM +
                " FROM (SELECT *  FROM " + DataBaseHelper.TABLE_DIEM + " WHERE " + DataBaseHelper.COLUMN_MA_MON_HOC +
                "='" + subjectId + "') as D," +
                " (SELECT * FROM " + DataBaseHelper.TABLE_HOC_SINH + " WHERE " + DataBaseHelper.COLUMN_LOP
                + "='" + gradeId + "') AS HS" +
                " WHERE D." + DataBaseHelper.COLUMN_MA_HOC_SINH + " = HS." + DataBaseHelper.COLUMN_MA_HOC_SINH+
                " AND (CAST(HS.MAHOCSINH AS TEXT) LIKE ? OR HO LIKE ? OR TEN LIKE ? OR NGAYSINH LIKE ? OR CAST(DIEM AS TEXT) LIKE ?)"
//                        " AND (CAST(HS.MAHOCSINH AS TEXT) LIKE ? OR HO LIKE ? OR TEN LIKE ? 0R" +
//                        " PHAI LIKE ? OR NGAYSINH LIKE ? OR CAST(DIEM AS TEXT) LIKE ?)"




                , new String[]{searchLike,searchLike,searchLike,searchLike,searchLike});
        while (cursor.moveToNext()) {
            list.add(new MarkDTO(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getDouble(8)));
        }
        return list;
    }
}