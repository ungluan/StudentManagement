package com.example.studentmanagement.feature.ProfileScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.database.entity.Account;
import com.example.studentmanagement.database_sqlite.Dao.AccountDao;

public class ChangePasswordViewModel extends AndroidViewModel {
    private AccountDao accountDao;
    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
        accountDao = new AccountDao(application);
    }

    public void updatePassword(int accountId, String newPassword){
        accountDao.updatePassword( accountId,  newPassword);
    }
    public boolean checkPassword(int teacherId, String password){
        return accountDao.checkPassword(teacherId,password);
    }
    public int getAccountIdByTeacherId(int teacherId){
        return accountDao.getAccountIdByTeacherId(teacherId);
    }
}