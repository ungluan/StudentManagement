package com.example.studentmanagement.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.studentmanagement.database.entity.StudentWithMarks;


import java.util.List;

@Dao
public interface StudentDao {
    @Transaction
    @Query("SELECT * FROM HOCSINH")
    List<StudentWithMarks> getStudentsWithMarks();
}
