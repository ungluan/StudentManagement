package com.example.studentmanagement.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentmanagement.database.entity.Mark;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface MarkDao {
    @Insert
    Completable insertMark(Mark mark);

    @Update
    Completable updateMark(Mark mark);
}
