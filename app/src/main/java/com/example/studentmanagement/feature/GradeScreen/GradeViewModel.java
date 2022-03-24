package com.example.studentmanagement.feature.GradeScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.repository.GradeRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GradeViewModel extends AndroidViewModel {
    private GradeRepository gradeRepository;
    private LiveData<List<Grade>> grades;

    public GradeViewModel(@NonNull Application application) {
        super(application);
        this.gradeRepository = new GradeRepository(application);
        grades = gradeRepository.getAllGrade();
    }

    public LiveData<List<Grade>> getAllGrade() {
        return grades;
    }

    public Completable updateGrade(Grade grade) {
        return gradeRepository.updateGrade(grade)
                .subscribeOn(Schedulers.computation())
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteGrade(Grade grade) {
        return gradeRepository.deleteGrade(grade)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertGrade(Grade grade){
        return gradeRepository.insertGrade(grade)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<Grade> getGradeById(String gradeId){
        return gradeRepository.getGradeById(gradeId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

