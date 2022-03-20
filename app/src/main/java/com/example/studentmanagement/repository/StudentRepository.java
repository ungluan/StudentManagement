package com.example.studentmanagement.repository;

import android.app.Application;

import com.example.studentmanagement.database.AppDatabase;
import com.example.studentmanagement.database.dao.StudentDao;
import com.example.studentmanagement.database.entity.StudentWithMarks;

import java.util.List;

public class StudentRepository {
    private final StudentDao studentDao;
    public StudentRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.studentDao = db.studentDao();
    }

    public List<StudentWithMarks> getStudentsWithMarks(){
        return studentDao.getStudentsWithMarks();
    }

//    public int getNumberOfStudents(){
//        return studentDao.getNumberOfStudent();
//    }
}
