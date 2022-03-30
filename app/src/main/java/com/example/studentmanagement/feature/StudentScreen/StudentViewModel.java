package com.example.studentmanagement.feature.StudentScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database_sqlite.Dao.GradeDao;
import com.example.studentmanagement.database_sqlite.Dao.MarkDao;
import com.example.studentmanagement.database_sqlite.Dao.StudentDao;
import com.example.studentmanagement.database_sqlite.Dao.SubjectDao;

import java.util.List;
import java.util.Map;

public class StudentViewModel extends AndroidViewModel {
    private GradeDao gradeDao;
    private StudentDao studentDao;
    private SubjectDao subjectDao;
    private MarkDao markDao;


    public StudentViewModel(@NonNull Application application) {
        super(application);
        gradeDao = new GradeDao(application);
        subjectDao = new SubjectDao(application);
        studentDao = new StudentDao(application);
        markDao = new MarkDao(application);
    }

    public List<Grade> getGrades() {
        return gradeDao.getGrades();
    }

    public List<Student> getStudentsByGradeId(String gradeId) {
        return studentDao.getStudentsByGradeId(gradeId);
    }

    public boolean insertStudent(Student student) {
        return studentDao.insertStudent(student);
    }

    public boolean updateStudent(Student student) {
        return studentDao.updateStudent(student);
    }

    public boolean deleteStudent(int studentId) {
        return studentDao.deleteStudent(studentId);
    }

    public Map<String,Double> getSubjectsSelectedByStudentId(int studentId){
        return subjectDao.getListSubjectAndMarkByStudentId(studentId);
    }
    public List<Subject> getSubjectSubjectId(int studentId){
        return subjectDao.getSubjectSubjectId(studentId);
    }

    public List<Subject> getSubjects() {
        return subjectDao.getSubjects();
    }

    public boolean deleteAndInsertMark(List<Mark> marks, List<String> delList){
        return markDao.deleteAndInsertMark(marks,delList);
    }



}
