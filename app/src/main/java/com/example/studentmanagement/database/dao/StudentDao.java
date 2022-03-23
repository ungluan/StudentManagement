package com.example.studentmanagement.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.relationship.StudentWithMarks;


import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface StudentDao {
    @Transaction
    @Query("SELECT * FROM HOCSINH")
    List<StudentWithMarks> getStudentsWithMarks();

    @Query("SELECT COUNT(*) FROM HOCSINH")
    Flowable<Integer> getNumberOfStudents();

    @Query("SELECT * FROM HOCSINH WHERE LOP = :gradeId")
    Flowable<List<Student>> getStudentsByGradeId(String gradeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertStudent(Student student);
}
