package com.example.studentmanagement.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.relationship.GradeWithStudents;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;

/***
 * @Query(“SELECT * FROM Users WHERE id = :userId”)
 * Maybe<User> getUserById(String userId);
 *
 *  Single
 * 1. When there is no user in the database and the query returns no rows,
 *      Single will trigger onError(EmptyResultSetException.class)
 * 2. When there is a user in the database, Single will trigger onSuccess.
 * 3. If the user is updated after Single was completed, nothing happens.
 *
 *  Maybe
 * 1. When there is no user in the database and the query returns no rows, Maybe will complete.
 * 2. When there is a user in the database, Maybe will trigger onSuccess and it will complete.
 * 3. If the user is updated after Maybe was completed, nothing happens.
 *
 *  Flowable/Observable
 * 1. When there is no user in the database and the query returns no rows,
 *  the Flowable will not emit, neither onNext, nor onError.
 * 2. When there is a user in the database, the Flowable will trigger onNext.
 * 3. Every time the user data is updated, the Flowable object will emit automatically,
 * allowing you to update the UI based on the latest data.
 *
 */

@Dao
public interface GradeDao {
    @Query("SELECT * FROM LOP")
    LiveData<List<Grade>> getAllGrade();

    @Transaction
    @Query("SELECT * FROM LOP")
    List<GradeWithStudents> getGradesWithStudents();

    @Insert
    Completable insertGrade(Grade grade);

    @Update
    Completable updateGrade(Grade grade);

    @Delete
    Completable deleteGrade(Grade grade);

    @Query("SELECT * FROM LOP WHERE LOP=:gradeId")
    Maybe<Grade> getGradeById(String gradeId);

}