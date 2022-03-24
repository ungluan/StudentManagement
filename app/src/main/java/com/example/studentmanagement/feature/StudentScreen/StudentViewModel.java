package com.example.studentmanagement.feature.StudentScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.repository.GradeRepository;
import com.example.studentmanagement.repository.StudentRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StudentViewModel extends AndroidViewModel {
    private StudentRepository studentRepository;
    private GradeRepository gradeRepository;

    public StudentViewModel(@NonNull Application application) {
        super(application);
//        AppDatabase db = AppDatabase.getDatabase(application);
        this.studentRepository = new StudentRepository(application);
        this.gradeRepository = new GradeRepository(application);
    }

    public Flowable<List<Grade>> getListGrade() {
        return gradeRepository.getListGrade()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<Student>> getStudentsByGradeId(String gradeId) {
        return studentRepository.getStudentsByGradeId(gradeId)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread());
    }

    public Completable insertStudent(Student student) {
        return studentRepository.insertStudent(student)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread());
    }

    public Completable updateStudent(Student student) {
        return studentRepository.updateStudent(student)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread());
    }

    public Completable deleteStudent(Student student) {
        return studentRepository.deleteStudent(student)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread());
    }
}
