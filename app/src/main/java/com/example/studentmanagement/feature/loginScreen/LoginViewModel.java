package com.example.studentmanagement.feature.loginScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.database_sqlite.Dao.LoginDao;
import com.example.studentmanagement.database_sqlite.Dao.TeacherDao;

public class LoginViewModel extends AndroidViewModel {
    final LoginDao loginDao;
    final TeacherDao teacherDao;
    private String email;
    private String password;
    private String phone;
    private int idUserForgotPass ;
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginDao = new LoginDao(application);
        teacherDao = new TeacherDao(application);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIdUserForgotPass() {
        return idUserForgotPass;
    }

    public void setIdUserForgotPass(int idUserForgotPass) {
        this.idUserForgotPass = idUserForgotPass;
    }

    public void saveInformation(String email, String password){
        this.email = email;
        this.password = password;
    }
    public boolean login(String email, String password){
        return loginDao.login(email,password);
    }
    public int getTeacherIdByEmail(String email){
        return teacherDao.getIdTeacherByEmail(email);
    }
    public Teacher getTeacherByEmail(String email){
        return teacherDao.getTeacherByEmail(email);
    }
    public Teacher getTeacherById(int teacherId){return teacherDao.getTeacherById(teacherId);}
    public boolean isUpdateInformation(int teacherId){
        Teacher teacher = getTeacherById(teacherId);
        return !teacher.getTeacherName().isEmpty();
    }
    public boolean updateTeacher(Teacher teacher){
        return teacherDao.updateTeacher(teacher);
    }
    public int getTeacherIdByPhone(String phone){
        return teacherDao.getIdTeacherByPhone(phone);
    }
}
