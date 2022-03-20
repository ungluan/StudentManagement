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

public class HomeViewModel extends AndroidViewModel {
    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.gradeRepository = new GradeRepository(application);
//        this.studentRepository = new StudentRepository(application);
//        this.subjectRepository = new SubjectRepository(application);
//        grades = gradeRepository.getAllGrade();
//        subjects = subjectRepository.getAllSubject();
//        students = studentRepository.getNumberOfStudents();
    }
    private GradeRepository gradeRepository;
    private StudentRepository studentRepository;
    private SubjectRepository subjectRepository;
    private LiveData<List<Grade>> grades;
    private LiveData<List<Subject> > subjects;
    private int students;
}