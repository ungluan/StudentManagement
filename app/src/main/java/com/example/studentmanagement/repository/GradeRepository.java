package com.example.studentmanagement.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.studentmanagement.database.AppDatabase;
import com.example.studentmanagement.database.dao.GradeDao;
import com.example.studentmanagement.database.entity.Grade;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

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

    public LiveData<List<Grade>> getAllGrade() {
        return gradeDao.getAllGrade();
    }

    public Completable insertGrade(Grade grade) {
        return gradeDao.insertGrade(grade);
    }

    public Completable updateGrade(Grade grade) {
        return gradeDao.updateGrade(grade);
    }

    public Completable deleteGrade(Grade grade) {
        return gradeDao.deleteGrade(grade);
    }

    public Maybe<Grade> getGradeById(String gradeId){
        return gradeDao.getGradeById(gradeId);
    }

    public Flowable<Integer> getNumberOfGrades(){
        return gradeDao.getNumberOfGrades();
    }
}
