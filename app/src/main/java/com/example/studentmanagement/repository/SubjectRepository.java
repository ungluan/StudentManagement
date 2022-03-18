package com.example.studentmanagement.repository;

import androidx.lifecycle.LiveData;

import com.example.studentmanagement.database.dao.SubjectDao;
import com.example.studentmanagement.database.entity.Subject;

import java.util.List;

public class SubjectRepository {
    private final SubjectDao subjectDao;

    public SubjectRepository(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    public LiveData<List<Subject>> getAllSubject(){
        return subjectDao.getAllSubject();
    }
}
