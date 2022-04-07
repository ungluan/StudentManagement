package com.example.studentmanagement.feature.loginScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.database_sqlite.Dao.LoginDao;

public class LoginViewModel extends AndroidViewModel {
    final LoginDao loginDao;
    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginDao = new LoginDao(application);
    }

    public boolean login(String email, String password){
        return loginDao.login(email,password);
    }
}
