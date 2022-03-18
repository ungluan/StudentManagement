package com.example.studentmanagement.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.studentmanagement.database.entity.Grade;

import java.util.List;

@Dao
public interface GradeDao {
    @Query("SELECT * FROM LOP")
    LiveData<List<Grade>> getAllGrade();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Grade grade);
}
