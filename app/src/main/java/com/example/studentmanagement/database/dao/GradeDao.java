package com.example.studentmanagement.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.GradeWithStudents;

import java.util.List;

@Dao
public interface GradeDao {
    @Query("SELECT * FROM LOP")
    LiveData<List<Grade>> getAllGrade();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Grade grade);

    @Transaction
    @Query("SELECT * FROM LOP")
    List<GradeWithStudents> getGradesWithStudents();
}
