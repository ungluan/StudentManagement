package com.example.studentmanagement.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.studentmanagement.database.AppDatabase;
import com.example.studentmanagement.database.dao.GradeDao;
import com.example.studentmanagement.database.entity.Grade;

import java.util.List;

// Repository Có thể Singleton?
// Nó có ảnh hưởng gì không?

// Implement SingleTon
// 1 static gradeRepository
// Constructor gradeRepository!=null ? return : new GradeRepository()
public class GradeRepository {
    private GradeDao gradeDao;

    public GradeRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.gradeDao = db.classDao();
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
