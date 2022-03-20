package com.example.studentmanagement.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.studentmanagement.database.AppDatabase;
import com.example.studentmanagement.database.dao.SubjectDao;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database.entity.SubjectWithMarks;

import java.util.List;

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
}
