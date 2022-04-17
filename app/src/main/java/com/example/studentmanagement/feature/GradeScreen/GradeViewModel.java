package com.example.studentmanagement.feature.GradeScreen;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.database_sqlite.Dao.GradeDao;
import com.example.studentmanagement.database_sqlite.Dao.StudentDao;
import com.example.studentmanagement.database_sqlite.Dao.TeacherDao;


import java.util.List;

public class GradeViewModel extends AndroidViewModel {
    private GradeDao gradeDao;
    private TeacherDao teacherDao;
    private StudentDao studentDao;
    public GradeViewModel(@NonNull Application application) {
        super(application);
        gradeDao = new GradeDao(application);
        teacherDao = new TeacherDao(application);
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

    public int getNumberOfStudentByGrade(String gradeId){
        return studentDao.getStudentsByGradeId(gradeId).size();
    }
    public List<Teacher> getTeacherHaveNotGrade(){
        return teacherDao.getTeacherHaveNotGrade();
    }
    public Teacher getTeacherById(int teacherId){
        return teacherDao.getTeacherById(teacherId);
    }
}

