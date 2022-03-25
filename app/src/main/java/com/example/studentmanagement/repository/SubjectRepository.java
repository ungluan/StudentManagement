package com.example.studentmanagement.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.studentmanagement.database.AppDatabase;
import com.example.studentmanagement.database.dao.SubjectDao;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database.entity.relationship.SubjectWithMarks;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

public class SubjectRepository {
    private final SubjectDao subjectDao;

    public SubjectRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.subjectDao = db.subjectDao();
    }

    public LiveData<List<Subject>> getAllSubject(){
        return subjectDao.getAllSubject();
    }


    public List<SubjectWithMarks> getSubjectsWithMarks() {
        return subjectDao.getSubjectsWithMarks();
    }

    public Flowable<Integer> getNumberOfSubjects(){
        return subjectDao.getNumberOfSubjects();
    }

    public Completable insertSubject(Subject subject) {return subjectDao.insertSubject(subject);}
    public Completable updateSubject(Subject subject) {return subjectDao.updateSubject(subject);}
    public Completable deleteSubject(Subject subject) {return subjectDao.deleteSubject(subject);}
    public Maybe<Subject> getSubjectById(String id) {return subjectDao.getSubjectById(id);}
    public Maybe<List<Subject>> getListSubject(){return subjectDao.getListSubject();}

}
