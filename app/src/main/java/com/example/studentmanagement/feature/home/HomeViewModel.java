package com.example.studentmanagement.feature.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;



import com.example.studentmanagement.database.daosqlite.SubjectDaoSqlite;

import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.database_sqlite.Dao.GradeDao;
import com.example.studentmanagement.database_sqlite.Dao.StudentDao;
import com.example.studentmanagement.database_sqlite.Dao.SubjectDao;

import com.example.studentmanagement.database_sqlite.Dao.TeacherDao;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {
    private GradeDao gradeDao;
    private StudentDao studentDao;
    private SubjectDao subjectDao;
    private TeacherDao teacherDao;


    public HomeViewModel(@NonNull Application application) {
        super(application);

        gradeDao = new GradeDao(application);
        subjectDao = new SubjectDao(application);
        studentDao = new StudentDao(application);
        teacherDao = new TeacherDao(application);
    }

    public Teacher getTeacherById(int teacherId){
        return teacherDao.getTeacherById(teacherId);
    }

    public int getNumberOfGrades(){
        return gradeDao.getNumberOfGrade();
    }

    public int getNumberOfSubjects(){
        return subjectDao.getNumberOfSubject();
    }

    public int getNumberOfStudents(){
        return studentDao.getNumberOfStudent();
    }

    public int getNumberOfTeachers(){
        return teacherDao.getNumberOfTeachers();
    }

}