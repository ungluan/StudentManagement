package com.example.studentmanagement.repository;

import android.app.Application;

import com.example.studentmanagement.database.AppDatabase;
import com.example.studentmanagement.database.dao.StudentDao;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.relationship.StudentWithMarks;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

public class StudentRepository {
    private final StudentDao studentDao;

    public StudentRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.studentDao = db.studentDao();
    }

    /*public List<StudentWithMarks> getStudentsWithMarks() {
        return studentDao.getStudentsWithMarks();
    }*/

    public Flowable<Integer> getNumberOfStudents() {
        return studentDao.getNumberOfStudents();
    }

    public Flowable<List<Student>> getStudentsByGradeId(String gradeId) {
        return studentDao.getStudentsByGradeId(gradeId);
    }

    public Completable insertStudent(Student student) {
        return studentDao.insertStudent(student);
    }

    public Completable updateStudent(Student student) {
        return studentDao.updateStudent(student);
    }

    public Completable deleteStudent(Student student) {
        return studentDao.deleteStudent(student);
    }

}