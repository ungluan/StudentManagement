package com.example.studentmanagement.feature.ProfileScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.database.entity.Account;
import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.database_sqlite.Dao.AccountDao;
import com.example.studentmanagement.database_sqlite.Dao.TeacherDao;

public class InformationViewModel extends AndroidViewModel {
    private TeacherDao teacherDao;
    private AccountDao accountDao;
    public InformationViewModel(@NonNull Application application) {
        super(application);
        teacherDao = new TeacherDao(application);
        accountDao = new AccountDao(application);
    }
    public Teacher getTeacherById(int teacherId){
        return teacherDao.getTeacherById(teacherId);
    }
    public Account getAccountById(int accountId){
        return  accountDao.getAccountById(accountId);
    }
}
