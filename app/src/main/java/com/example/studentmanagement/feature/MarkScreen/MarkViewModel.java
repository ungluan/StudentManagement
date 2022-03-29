package com.example.studentmanagement.feature.MarkScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.database.daosqlite.MarkDaoSqlite;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database_sqlite.Dao.GradeDao;
import com.example.studentmanagement.database_sqlite.Dao.MarkDao;
import com.example.studentmanagement.database_sqlite.Dao.StudentDao;
import com.example.studentmanagement.database_sqlite.Dao.SubjectDao;
import com.example.studentmanagement.repository.GradeRepository;
import com.example.studentmanagement.repository.MarkRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MarkViewModel extends AndroidViewModel {
    private MarkDao markDao;
    private GradeDao gradeDao;
    private SubjectDao subjectDao;
    private StudentDao studentDao;
    public MarkViewModel(@NonNull Application application) {
        super(application);
        this.markDao = new MarkDao(application);
        this.gradeDao = new GradeDao(application);
        this.subjectDao = new SubjectDao(application);
        this.studentDao = new StudentDao(application);
    }

    public Student getStudent(int studentId){
        return studentDao.getStudent(studentId);
    }
    public Student getStudentByMark(int studentId, String subjectId){
        return markDao.getStudentByMark(studentId, subjectId);
    }

    public boolean updateMark(Mark mark){
        return markDao.update(mark);
    }

    public List<Subject> getSubjects(){
        return subjectDao.getSubjects();
    }

    public List<Grade> getGrades(){
        return gradeDao.getGrades();
    }

    public List<Mark> getMarks(String gradeId, String subjectId){
        return markDao.getMarkByGradeAndSubject(gradeId, subjectId);
    }

//    private MarkDaoSqlite markDaoSqlite;
////    private MarkRepository markRepository;
////    private SubjectRepository subjectRepository;
////    private GradeRepository gradeRepository;
//
//    public MarkViewModel(@NonNull Application application) {
//        super(application);
////        studentRepository = new StudentRepository(application);
////        markRepository = new MarkRepository(application);
////        subjectRepository = new SubjectRepository(application);
////        gradeRepository = new GradeRepository(application);
//        markDaoSqlite = new MarkDaoSqlite(application);
//
//    }
//
//    public ArrayList<Mark> getAllMarkByGradeAndSubject(String gradeId, String subjectId){
//        return markDaoSqlite.getMarkByGradeAndSubject(gradeId, subjectId);
//    }
//
//    public Student getStudentByMark(int studentId){
//        return markDaoSqlite.getStudentByMark(studentId);
//    }






//   public Flowable<Student> getStudentById(int id){
//        return studentRepository.getStudentById(id)
//                .subscribeOn(Schedulers.io())
//               .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    public Flowable<Float> getStudentMark(String studentId, String subjectId){
//        return markRepository.getStudentMark(studentId,
//                subjectId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.newThread());
//    }
//
//    public Flowable<List<Student>> getStudentGradeIdAndSubjectId(String gradeId,
//                                                                 String subjectId){
//        return studentRepository.getStudentByGradeIdAndSubjectId(gradeId,
//                subjectId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.newThread());
//    }
//
//    public Flowable<List<Mark>> getMarkByStudentAndSubject(String gradeId, String subjectId){
//        return markRepository.getMarkByStudentAndSubject(gradeId, subjectId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.newThread());
//    }
//    public Maybe<List<Grade>> getListGrade() {
//        return gradeRepository.getListGrade()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    public Maybe<List<Subject>> getListSubject() {
//        return subjectRepository.getListSubject()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//
//    public Completable updateMark(Mark mark) {
//        return markRepository.updateMark(mark)
//                .subscribeOn(Schedulers.computation())
//                .observeOn(Schedulers.newThread());
//    }




}
