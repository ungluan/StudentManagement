package com.example.studentmanagement.repository;

import android.app.Application;

import com.example.studentmanagement.database.AppDatabase;
import com.example.studentmanagement.database.dao.MarkDao;
import com.example.studentmanagement.database.entity.Mark;

import io.reactivex.rxjava3.core.Completable;

public class MarkRepository {
    private MarkDao markDao;

    public MarkRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        markDao = db.markDao();
    }

    public Completable insertMark(Mark mark) {
        return markDao.insertMark(mark);
    }

    public Completable updateMark(Mark mark){
        return markDao.updateMark(mark);
    }
}
