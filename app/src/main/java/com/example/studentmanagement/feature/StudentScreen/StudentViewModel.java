package com.example.studentmanagement.feature.StudentScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database.entity.relationship.SubjectWithMarks;
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
    private StudentRepository studentRepository;
    private GradeRepository gradeRepository;
    private SubjectRepository subjectRepository;
    private MarkRepository markRepository;

    public StudentViewModel(@NonNull Application application) {
        super(application);
//        AppDatabase db = AppDatabase.getDatabase(application);
        this.studentRepository = new StudentRepository(application);
        this.gradeRepository = new GradeRepository(application);
        this.subjectRepository = new SubjectRepository(application);
        this.markRepository = new MarkRepository(application);
    }

    public Maybe<List<Grade>> getListGrade() {
        return gradeRepository.getListGrade()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<Student>> getStudentsByGradeId(String gradeId) {
        return studentRepository.getStudentsByGradeId(gradeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertStudent(Student student) {
        return studentRepository.insertStudent(student)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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

    public Maybe<List<Subject>> getListSubject() {
        return subjectRepository.getListSubject()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Subject> loadChipGroupSubject(List<Subject> subjects) {
        return Observable.fromIterable(subjects)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable insertMark(Mark mark) {
        return markRepository.insertMark(mark).subscribeOn(Schedulers.computation());
    }

    public Completable updateMark(Mark mark) {
        return markRepository.updateMark(mark)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<List<SubjectWithMarks>> getSubjectAndMarkByStudentId(int studentId) {
        return subjectRepository.getSubjectAndMarkByStudentId(studentId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SubjectWithMarks> checkSubjectsSelected(List<SubjectWithMarks> list) {
        return Observable.fromIterable(list)
                .subscribeOn(Schedulers.computation());
    }

    public Observable<Subject> addListSubject(List<Subject> listNewSubject) {
        return Observable.fromIterable(listNewSubject)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<List<Subject>> getSubjectsByStudentId(int studentId) {
        return subjectRepository.getSubjectsByStudentId(studentId).
                subscribeOn(Schedulers.computation());
    }

    public Completable insertListMark(List<Mark> marks) {
        return markRepository.insertListMark(marks)
                .subscribeOn(Schedulers.computation());
    }

    public Completable deleteListMark(List<Mark> marks) {
        return markRepository.deleteListMark(marks);
    }
//    public Observable<List<Mark>> deleteMarks(List<Mark> marks){
//        return Observable.fromArray(marks).flatMap(
//                marks1 -> Ob
//        )
//    }

    /*public void deleteAndInsertMark(List<Mark> listRemove, List<Mark> listSelected) {
        markRepository.deleteAndInsertMark(listRemove, listSelected);
    }*/

//    public Completable insertStudentWithMarks(StudentWithMarks studentWithMarks) {
//        return studentRepository.insertStudentWithMarks(studentWithMarks)
//                .subscribeOn(Schedulers.computation())
//                .observeOn(Schedulers.newThread());
//    }
}
