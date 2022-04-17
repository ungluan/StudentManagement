package com.example.studentmanagement.feature.SubjectScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.studentmanagement.database.daosqlite.SubjectDaoSqlite;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database_sqlite.Dao.SubjectDao;

import java.util.List;

public class SubjectViewModel extends AndroidViewModel {
    private SubjectDao subjectDao;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        this.subjectDao = new SubjectDao(application);
    }

    public List<Subject> getAllSubject(){return subjectDao.getSubjects();}

    public boolean insertSubject(Subject subject){
        return subjectDao.insertSubject(subject);
    }

    public Subject getSubjectById(String id){
        return subjectDao.getSubject(id);
    }

    public boolean updateSubject(Subject subject){
        return subjectDao.updateSubject(subject);
    }

    public boolean deleteSubject(String subjectId){
        return subjectDao.deleteSubject(subjectId);
    }

    public List<Subject> searchSubjectBySameIdOrName(String search){
        return subjectDao.searchSubjectBySameIdOrName(search);

    }
}
