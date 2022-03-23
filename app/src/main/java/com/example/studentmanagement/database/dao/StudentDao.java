package com.example.studentmanagement.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.studentmanagement.database.entity.relationship.StudentWithMarks;


import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface StudentDao {
    @Transaction
    @Query("SELECT * FROM HOCSINH")
    List<StudentWithMarks> getStudentsWithMarks();

    @Query("SELECT COUNT(*) FROM HOCSINH")
    Flowable<Integer> getNumberOfStudents();
}
