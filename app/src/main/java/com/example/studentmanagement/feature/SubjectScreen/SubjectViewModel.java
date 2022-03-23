package com.example.studentmanagement.feature.SubjectScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.repository.SubjectRepository;

import java.util.List;

public class SubjectViewModel extends AndroidViewModel {
    private SubjectRepository subjectRepository;
    private LiveData<List<Subject>> subjects;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        this.subjectRepository = new SubjectRepository(application);
        subjects = this.subjectRepository.getAllSubject();
    }

    public LiveData<List<Subject>> getAllSubject(){return this.subjects;}
}
