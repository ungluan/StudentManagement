package com.example.studentmanagement.feature.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.repository.GradeRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.SubjectRepository;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {
    private GradeRepository gradeRepository;
    private SubjectRepository subjectRepository;
    private StudentRepository studentRepository;
    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.gradeRepository = new GradeRepository(application);
        this.subjectRepository = new SubjectRepository(application);
        this.studentRepository = new StudentRepository(application);
    }

    public Flowable<Integer> getNumberOfGrades(){
        return gradeRepository.getNumberOfGrades()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Integer> getNumberOfSubjects(){
        return subjectRepository.getNumberOfSubjects()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<Integer> getNumberOfStudents(){
        return studentRepository.getNumberOfStudents()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /*public Flowable<List<GradeWithStudents>> getGradesWithStudents(){
        return gradeRepository.getGradesWithStudents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }*/
}