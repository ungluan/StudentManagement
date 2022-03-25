package com.example.studentmanagement.repository;

import android.app.Application;

import androidx.room.Database;

import com.example.studentmanagement.database.AppDatabase;
import com.example.studentmanagement.database.dao.MarkDao;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class MarkRepository{
    private MarkDao markDao;

    public MarkRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        this.markDao = db.markDao();
    }

    public Flowable<Float> getStudentMark(String studentId, String subjectId){
        return markDao.getStudentmark(studentId, subjectId);
    }
    public Flowable<List<Mark>> getMarkByStudentAndSubject(String gradeId, String subjectId){
        return  markDao.getMarkByStudentAndSubject(gradeId, subjectId);
    }

    public Completable updateMark(Mark mark) {
        return markDao.updateMark(mark);
    }
}
