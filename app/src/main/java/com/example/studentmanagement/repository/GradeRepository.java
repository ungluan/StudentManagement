package com.example.studentmanagement.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.studentmanagement.database.AppDatabase;
import com.example.studentmanagement.database.dao.GradeDao;
import com.example.studentmanagement.database.entity.Grade;

import java.util.List;

public class GradeRepository {
    private GradeDao gradeDao;

    public GradeRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.gradeDao = db.classDao();
        Log.i("loca", application.getDatabasePath("app_word").toString());
    }

    // Thực hiện Async tại viewModel
    public LiveData<List<Grade>> getAllGrade(){
        return gradeDao.getAllGrade();
    }

    void insert(Grade grade){
        AppDatabase.databaseWriteExecutor.execute(()->{
                    gradeDao.insert(grade);
                }
            );
    }
}
