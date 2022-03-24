package com.example.studentmanagement.feature.SubjectScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.repository.SubjectRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SubjectViewModel extends AndroidViewModel {
    private SubjectRepository subjectRepository;
    private LiveData<List<Subject>> subjects;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        this.subjectRepository = new SubjectRepository(application);
        subjects = this.subjectRepository.getAllSubject();
    }

    public LiveData<List<Subject>> getAllSubject(){return this.subjects;}

    public Completable insertSubject(Subject subject){
        return subjectRepository.insertSubject(subject)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateSubject(Subject subject){
        return subjectRepository.updateSubject(subject)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteSubject(Subject subject){
        return subjectRepository.deleteSubject(subject)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<Subject> getSubjectById(String id){
        return subjectRepository.getSubjectById(id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
