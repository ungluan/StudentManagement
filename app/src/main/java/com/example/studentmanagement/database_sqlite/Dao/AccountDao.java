package com.example.studentmanagement.database_sqlite.Dao;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.studentmanagement.database.entity.Account;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AccountDao {
    private DataBaseHelper dataBaseHelper;

    public AccountDao(Application application) {
        // GetInstances
        this.dataBaseHelper = DataBaseHelper.getInstance(application);
    }

    public void updatePassword(int accountId, String newPassword) {
        String query = "UPDATE " + DataBaseHelper.TABLE_TAI_KHOAN + " SET " +
                DataBaseHelper.COLUMN_MAT_KHAU + " = '"+ newPassword +"'" + " WHERE "+
                DataBaseHelper.COLUMN_MA_TAI_KHOAN + " = " + accountId;

        dataBaseHelper.execSql(query);
    }

    public Boolean checkPassword(int teacherId, String password){
        String query = "SELECT 1 FROM " + DataBaseHelper.TABLE_GVCN +", "+DataBaseHelper.TABLE_TAI_KHOAN +
                " WHERE "+ DataBaseHelper.TABLE_GVCN +"."+ DataBaseHelper.COLUMN_MA_TAI_KHOAN +" = "+
                DataBaseHelper.TABLE_TAI_KHOAN +"."+ DataBaseHelper.COLUMN_MA_TAI_KHOAN +" AND "+
                DataBaseHelper.TABLE_TAI_KHOAN +"."+DataBaseHelper.COLUMN_MA_TAI_KHOAN + " = " + teacherId + " AND " +
                DataBaseHelper.COLUMN_MAT_KHAU +" = '"+ password +"'";
        Cursor cursor = dataBaseHelper.query(query,null);
        int value=-1;
        if(cursor.moveToFirst()){
            value = cursor.getInt(0);
        }
        boolean v = value!=-1;
        return v;
    }

    public int getAccountIdByTeacherId(int teacherId){
        String query = "SELECT "+DataBaseHelper.TABLE_TAI_KHOAN +"."+ DataBaseHelper.COLUMN_MA_TAI_KHOAN +
                " FROM "+DataBaseHelper.TABLE_TAI_KHOAN + ", "+ DataBaseHelper.TABLE_GVCN +
                " WHERE "+ DataBaseHelper.TABLE_GVCN +"."+ DataBaseHelper.COLUMN_MA_TAI_KHOAN +" = "+
                        DataBaseHelper.TABLE_TAI_KHOAN +"."+ DataBaseHelper.COLUMN_MA_TAI_KHOAN +" AND "+
                        DataBaseHelper.COLUMN_MA_CHU_NHIEM + " = " + teacherId;
        Cursor cursor = dataBaseHelper.query(query,null);
        int studentId = -1;
        if(cursor.moveToFirst()){
            studentId = cursor.getInt(0);
        }
        return studentId;
    }

    public Account getAccountById(int accountId){
        Account account = new Account();
        String query = "SELECT * FROM "+DataBaseHelper.TABLE_TAI_KHOAN+" WHERE "
                + DataBaseHelper.COLUMN_MA_TAI_KHOAN +" = " +accountId;
        Cursor cursor = dataBaseHelper.query(query,null);
        if(cursor.moveToFirst()){
            account.setId(cursor.getInt(0));
            account.setEmail(cursor.getString(1));
            account.setPassword(cursor.getString(2));
        }
        return account;
    }

    public ContentValues values(Account account, boolean noId) {
        ContentValues values = new ContentValues();
        if(!noId) values.put(DataBaseHelper.COLUMN_MA_TAI_KHOAN, account.getId());
        values.put(DataBaseHelper.COLUMN_EMAIL, account.getEmail());
        values.put(DataBaseHelper.COLUMN_MAT_KHAU, account.getPassword());
        return values;
    }

}
