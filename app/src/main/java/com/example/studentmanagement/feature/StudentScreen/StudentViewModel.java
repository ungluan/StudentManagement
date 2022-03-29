package com.example.studentmanagement.feature.StudentScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database.entity.relationship.SubjectWithMarks;
import com.example.studentmanagement.database_sqlite.Dao.GradeDao;
import com.example.studentmanagement.database_sqlite.Dao.StudentDao;
import com.example.studentmanagement.database_sqlite.Dao.SubjectDao;
import com.example.studentmanagement.repository.GradeRepository;
import com.example.studentmanagement.repository.MarkRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.SubjectRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class StudentViewModel extends AndroidViewModel {
    private GradeDao gradeDao;
    private StudentDao studentDao;
    private SubjectDao subjectDao;


    public StudentViewModel(@NonNull Application application) {
        super(application);
        gradeDao = new GradeDao(application);
        subjectDao = new SubjectDao(application);
        studentDao = new StudentDao(application);
    }

    public List<Grade> getGrades() {
        return gradeDao.getGrades();
    }

    public List<Student> getStudentsByGradeId(String gradeId) {
        return studentDao.getStudentsByGradeId(gradeId);
    }

    public boolean insertStudent(Student student) {
        return studentDao.insertStudent(student);
    }

    public boolean updateStudent(Student student) {
        return studentDao.updateStudent(student);
    }

    public boolean deleteStudent(int studentId) {
        return studentDao.deleteStudent(studentId);
    }

    public List<Subject> getSubjects() {
        return subjectDao.getSubjects();
    }

    public Observable<Subject> loadChipGroupSubject(List<Subject> subjects) {
        return Observable.fromIterable(subjects)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

//    public Boolean insertMark(Mark mark) {
//        return markRepository.insertMark(mark).subscribeOn(Schedulers.computation());
//    }
//
//    public Completable updateMark(Mark mark) {
//        return markRepository.updateMark(mark)
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    public Maybe<List<SubjectWithMarks>> getSubjectAndMarkByStudentId(int studentId) {
//        return subjectRepository.getSubjectAndMarkByStudentId(studentId)
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    public Observable<SubjectWithMarks> checkSubjectsSelected(List<SubjectWithMarks> list) {
        return Observable.fromIterable(list)
                .subscribeOn(Schedulers.computation());
    }

    public Observable<Subject> addListSubject(List<Subject> listNewSubject) {
        return Observable.fromIterable(listNewSubject)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

//    public Maybe<List<Subject>> getSubjectsByStudentId(int studentId) {
//        return subjectRepository.getSubjectsByStudentId(studentId).
//                subscribeOn(Schedulers.computation());
//    }
//
//    public Completable insertListMark(List<Mark> marks) {
//        return markRepository.insertListMark(marks)
//                .subscribeOn(Schedulers.computation());
//    }
//
//    public Completable deleteListMark(List<Mark> marks) {
//        return markRepository.deleteListMark(marks);
//    }

}
