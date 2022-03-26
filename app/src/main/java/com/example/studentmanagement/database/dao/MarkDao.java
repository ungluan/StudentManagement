package com.example.studentmanagement.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.studentmanagement.database.entity.Mark;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import kotlinx.coroutines.Deferred;

@Dao
public interface MarkDao {
    @Insert
    Completable insertMark(Mark mark);

    @Update
    Completable updateMark(Mark mark);

    @Transaction
    @Insert
    Completable insertListMark(List<Mark> marks);

    @Transaction
    @Delete
    Completable deleteListMark(List<Mark> marks);

    @Update
    Completable deleteMark(Mark mark);

    /*@Transaction
    default void deleteAndInsertMark(
            List<Mark> listRemove, List<Mark> listSelected){
        deleteListMark(listRemove);
        insertListMark(listSelected);
    }*/
}
