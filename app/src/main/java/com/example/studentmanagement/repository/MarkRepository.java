package com.example.studentmanagement.repository;

import android.app.Application;

import com.example.studentmanagement.database.AppDatabase;
import com.example.studentmanagement.database.dao.MarkDao;
import com.example.studentmanagement.database.entity.Mark;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MarkRepository {
    private MarkDao markDao;

    public MarkRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        markDao = db.markDao();
    }

    public Completable insertMark(Mark mark) {
        return markDao.insertMark(mark);
    }

    public Completable updateMark(Mark mark) {
        return markDao.updateMark(mark);
    }

    public Completable insertListMark(List<Mark> marks) {
        return markDao.insertListMark(marks);
    }

    public Completable deleteListMark(List<Mark> marks) {
        return markDao.deleteListMark(marks);
    }

    /*public void deleteAndInsertMark(List<Mark> listRemove, List<Mark> listSelected){
        markDao.deleteAndInsertMark(listRemove,listSelected);
    }*/
}
