package com.example.studentmanagement.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database.entity.relationship.SubjectWithMarks;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

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
}
