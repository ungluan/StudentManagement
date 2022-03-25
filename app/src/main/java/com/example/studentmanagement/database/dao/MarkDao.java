package com.example.studentmanagement.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface MarkDao {



    @Update
    Completable updateMark(Mark mark);

    @Query("SELECT DIEM FROM DIEM WHERE MAHOCSINH=:studentId AND MAMONHOC=:subjectId")
    Flowable<Float> getStudentmark(String studentId, String subjectId);

    @Query("SELECT D.* FROM (SELECT * FROM DIEM WHERE MAMONHOC=:subjectId) as D," +
            " (SELECT MAHOCSINH FROM HOCSINH WHERE LOP=:gradeId) AS HS" +
            " WHERE D.MAHOCSINH = HS.MAHOCSINH")
    Flowable<List<Mark>> getMarkByStudentAndSubject(String gradeId, String subjectId);
}
