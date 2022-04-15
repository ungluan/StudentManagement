package com.example.studentmanagement.feature.loginScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.database_sqlite.Dao.LoginDao;
import com.example.studentmanagement.database_sqlite.Dao.TeacherDao;

public class LoginViewModel extends AndroidViewModel {
    final LoginDao loginDao;
    final TeacherDao teacherDao;
    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginDao = new LoginDao(application);
        teacherDao = new TeacherDao(application);
    }

    public boolean login(String email, String password){
        return loginDao.login(email,password);
    }
    public int getTeacherIdByEmail(String email){
        return teacherDao.getIdTeacherByEmail(email);
    }
}
