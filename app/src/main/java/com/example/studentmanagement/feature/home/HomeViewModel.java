package com.example.studentmanagement.feature.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.studentmanagement.database_sqlite.Dao.GradeDao;
import com.example.studentmanagement.database_sqlite.Dao.StudentDao;
import com.example.studentmanagement.database_sqlite.Dao.SubjectDao;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;
import com.example.studentmanagement.repository.GradeRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.SubjectRepository;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {

    private GradeDao gradeDao;
    private StudentDao studentDao;
    private SubjectDao subjectDao;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        gradeDao = new GradeDao(application);
        subjectDao = new SubjectDao(application);
        studentDao = new StudentDao(application);
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

}