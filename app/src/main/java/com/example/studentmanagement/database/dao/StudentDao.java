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
//    @Transaction
//    @Query("SELECT * FROM HOCSINH")
//    List<StudentWithMarks> getStudentsWithMarks();

    @Query("SELECT COUNT(*) FROM HOCSINH")
    Flowable<Integer> getNumberOfStudents();

    @Query("SELECT * FROM HOCSINH WHERE LOP = :gradeId")
    Flowable<List<Student>> getStudentsByGradeId(String gradeId);

    @Query("SELECT * FROM HOCSINH WHERE MAHOCSINH=:id")
    Flowable<Student> getStudentById(int id);


    @Query("SELECT HS.* FROM (SELECT * FROM HOCSINH WHERE LOP = :gradeId) AS HS," +
            "(SELECT MAHOCSINH FROM DIEM WHERE MAMONHOC = :subjectId) AS D " +
            "WHERE HS.MAHOCSINH = D.MAHOCSINH")
    Flowable<List<Student>> getStudentByGradeIdAndSubjectId(String gradeId, String subjectId);

    @Insert
    Completable insertStudent(Student student);
    @Update
    Completable updateStudent(Student student);
    @Delete
    Completable deleteStudent(Student student);
}
