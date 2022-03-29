package com.example.studentmanagement.feature.GradeScreen;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database_sqlite.Dao.GradeDao;


import java.util.List;

public class GradeViewModel extends AndroidViewModel {
    private GradeDao gradeDao;

    public GradeViewModel(@NonNull Application application) {
        super(application);
        gradeDao = new GradeDao(application);
    }

    public List<Grade> getGrades() {
        return gradeDao.getGrades();
    }

    public Boolean updateGrade(Grade grade) {
        return gradeDao.updateGrade(grade);
    }

    public Boolean deleteGrade(String gradeId) {
        return gradeDao.deleteGrade(gradeId);
    }

    public Boolean insertGrade(Grade grade) {
        return gradeDao.insertGrade(grade);
    }

    public Boolean checkGradeId(String gradeId) {
        return gradeDao.checkGradeId(gradeId);
    }

}

