package com.example.studentmanagement.feature.SubjectScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.studentmanagement.database.daosqlite.SubjectDaoSqlite;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.repository.SubjectRepository;

import java.util.List;

public class SubjectViewModel extends AndroidViewModel {
    private SubjectDaoSqlite subjectDaoSqlite;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        this.subjectDaoSqlite = new SubjectDaoSqlite(application);
    }

    public List<Subject> getAllSubject(){return subjectDaoSqlite.getAllSubject();}

    public boolean insert(Subject subject){
        return subjectDaoSqlite.insert(subject);
    }

    public boolean update(Subject subject){
        return subjectDaoSqlite.update(subject);
    }

    public boolean delete(String subjectId){
        return subjectDaoSqlite.delete(subjectId);
    }
}
