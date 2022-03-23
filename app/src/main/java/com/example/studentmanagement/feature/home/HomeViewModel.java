package com.example.studentmanagement.feature.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.studentmanagement.database.AppDatabase;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.repository.GradeRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.SubjectRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlinx.coroutines.flow.Flow;

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
}