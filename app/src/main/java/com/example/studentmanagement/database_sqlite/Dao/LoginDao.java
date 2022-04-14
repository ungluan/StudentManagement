package com.example.studentmanagement.database_sqlite.Dao;

import android.app.Application;
import android.database.Cursor;

import androidx.room.Database;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;

public class LoginDao {
    private DataBaseHelper dataBaseHelper;

    public LoginDao(Application application) {
        // GetInstances
        this.dataBaseHelper = DataBaseHelper.getInstance(application);
    }

    public Boolean login(String email, String password) {
        String query = "SELECT 1 FROM "+ DataBaseHelper.TABLE_TAI_KHOAN +" WHERE "
                + DataBaseHelper.COLUMN_EMAIL +" = '" +email+"' AND "
                + DataBaseHelper.COLUMN_MAT_KHAU + " = '"+password+"'";
        Cursor cursor = dataBaseHelper.query(query,null);
        if(cursor.moveToFirst()) return true;
        return false;
    }
}
