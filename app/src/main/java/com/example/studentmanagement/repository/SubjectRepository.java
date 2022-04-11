//package com.example.studentmanagement.repository;
//
//import android.app.Application;
//
//import androidx.lifecycle.LiveData;
//
//import com.example.studentmanagement.database.AppDatabase;
//import com.example.studentmanagement.database.dao.SubjectDao;
//import com.example.studentmanagement.database.entity.Subject;
//import com.example.studentmanagement.database.entity.relationship.SubjectWithMarks;
//
//import java.util.List;
//
//import io.reactivex.rxjava3.core.Flowable;
//import io.reactivex.rxjava3.core.Maybe;
//
//public class SubjectRepository {
//    private final SubjectDao subjectDao;
//
//    public SubjectRepository(Application application) {
//        AppDatabase db = AppDatabase.getDatabase(application);
//        this.subjectDao = db.subjectDao();
//    }
//
//    public LiveData<List<Subject>> getAllSubject(){
//        return subjectDao.getAllSubject();
//    }
//
//    public Maybe<List<Subject>> getSubjectsByStudentId(int studentId) {
//        return subjectDao.getSubjectsByStudentId(studentId);
//    }
//
//    public Flowable<Integer> getNumberOfSubjects(){
//        return subjectDao.getNumberOfSubjects();
//    }
//
//
//    public Maybe<List<Subject>> getListSubject(){return subjectDao.getListSubject();}
//
//    public Maybe<List<SubjectWithMarks>> getSubjectAndMarkByStudentId(int studentId){
//        return subjectDao.getSubjectAndMarkByStudentId(studentId);
//    }
//}
