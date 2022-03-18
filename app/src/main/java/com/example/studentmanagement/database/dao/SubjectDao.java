package com.example.studentmanagement.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.studentmanagement.database.entity.Subject;

import java.util.List;

@Dao
public interface SubjectDao {
    // get all subject@
    @Query("SELECT * FROM MONHOC")
    LiveData<List<Subject>> getAllSubject();
}
