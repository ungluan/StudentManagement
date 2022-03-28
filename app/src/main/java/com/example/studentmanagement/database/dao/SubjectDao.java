package com.example.studentmanagement.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database.entity.relationship.SubjectWithMarks;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface SubjectDao {
    // get all subject@
    @Query("SELECT * FROM MONHOC")
    LiveData<List<Subject>> getAllSubject();

    @Transaction
    @Query("SELECT * FROM MONHOC")
    List<SubjectWithMarks> getSubjectsWithMarks();

    @Query("SELECT COUNT(*) FROM MONHOC")
    Flowable<Integer> getNumberOfSubjects();

    @Query("SELECT * FROM MONHOC")
    Maybe<List<Subject>> getListSubject();

    @Query("SELECT * FROM MONHOC " +
            "WHERE MAMONHOC IN (SELECT MAMONHOC FROM DIEM WHERE DIEM.MAHOCSINH = :studentId) ")
    Maybe<List<Subject>> getSubjectsByStudentId(int studentId);

    @Transaction
    @Query("SELECT * FROM MONHOC " +
            "WHERE MAMONHOC IN (SELECT MAMONHOC FROM DIEM WHERE DIEM.MAHOCSINH = :studentId) ")
    Maybe<List<SubjectWithMarks>> getSubjectAndMarkByStudentId(int studentId);




//    @Transaction
//    @Query("SELECT * FROM MONHOC")
//    List<SubjectWithMarks> getSubjectWithMarkByStudentId;
}
