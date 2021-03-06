package com.example.studentmanagement.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.relationship.StudentWithMarks;


import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface StudentDao {
    @Transaction
    @Query("SELECT * FROM HOCSINH WHERE MAHOCSINH = :studentId ")
    Maybe<StudentWithMarks> getStudentWithMarks(int studentId);

    @Query("SELECT COUNT(*) FROM HOCSINH")
    Flowable<Integer> getNumberOfStudents();

    @Query("SELECT * FROM HOCSINH WHERE LOP = :gradeId")
    Flowable<List<Student>> getStudentsByGradeId(String gradeId);

    @Insert
    Completable insertStudent(Student student);
    @Update
    Completable updateStudent(Student student);
    @Delete
    Completable deleteStudent(Student student);

}
