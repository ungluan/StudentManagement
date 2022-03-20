package com.example.studentmanagement.feature.GradeScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.repository.GradeRepository;

import java.util.List;

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
}

