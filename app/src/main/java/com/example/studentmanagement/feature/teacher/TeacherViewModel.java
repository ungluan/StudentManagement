package com.example.studentmanagement.feature.teacher;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.database.entity.Account;
import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.database_sqlite.Dao.AccountDao;
import com.example.studentmanagement.database_sqlite.Dao.TeacherDao;
import com.example.studentmanagement.database_sqlite.Dao.TeacherDao;

import java.util.List;

public class TeacherViewModel extends AndroidViewModel {
    private TeacherDao teacherDao;
    private AccountDao accountDao;

    public TeacherViewModel(@NonNull Application application) {
        super(application);
        this.teacherDao = new TeacherDao(application);
        this.accountDao = new AccountDao(application);
    }

    public List<Account> getAccountNotInTeacher(){
        return accountDao.getAccountNotInTeacher();
    }
    public List<Teacher> getAllTeacher(){return teacherDao.getTeachers();}

    public boolean insertTeacher(Teacher teacher){
        return teacherDao.insertTeacher(teacher);
    }

    public boolean insertTeacher2(Teacher teacher){
        return teacherDao.insertTeacher2(teacher);
    }

    public Teacher getTeacherById(int id){
        return teacherDao.getTeacherById(id);
    }

    public boolean updateTeacher(Teacher teacher){
        return teacherDao.updateTeacher(teacher);
    }

    public boolean updateTeacher2(Teacher teacher){
        return teacherDao.updateTeacher2(teacher);
    }

    public boolean deleteTeacher(int teacherId){
        return teacherDao.deleteTeacher(teacherId);
    }

    public List<Teacher> searchTeacherBySameNameOrPhoneOrAccount(String search){
        return teacherDao.searchTeacherBySameNameOrPhoneOrAccount(search);

    }
}
