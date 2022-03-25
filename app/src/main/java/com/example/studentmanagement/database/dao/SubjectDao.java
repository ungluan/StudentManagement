package com.example.studentmanagement.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database.entity.relationship.SubjectWithMarks;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

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

    @Insert
    Completable insertSubject(Subject subject);

    @Update
    Completable updateSubject(Subject subject);

    @Delete
    Completable deleteSubject(Subject subject);

    @Query("SELECT * FROM MONHOC WHERE MAMONHOC=:id ")
    Maybe<Subject> getSubjectById(String id);

    @Query("SELECT * FROM MONHOC")
    Maybe<List<Subject>> getListSubject();

}
