package com.example.studentmanagement.feature.RegisterScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.database.entity.Account;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.database_sqlite.Dao.AccountDao;
import com.example.studentmanagement.database_sqlite.Dao.TeacherDao;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;
import com.example.studentmanagement.databinding.FragmentRegisterBinding;

// Đăng ký thành công -> Vào 1 Trang hỏi ? Đã được đăng ký thông tin - Chưa đăng ký thông tin.
// Đã được: Cho chọn DropDown Teacher -> Cập nhật thông tin.
// Chưa: Hình ảnh - Tên - SĐT (Mã TK)
// => Vào HomePage
public class RegisterViewModel extends AndroidViewModel {
    private AccountDao accountDao;
    private TeacherDao teacherDao;
    private String phone ;
    private String email ;
    private String password;
    private boolean isRegisterPage = false;

    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }

    public boolean isRegisterPage() {
        return isRegisterPage;
    }
    public void setIsRegisterPage(boolean value){
        this.isRegisterPage = value;
    }

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        accountDao = new AccountDao(application);
        teacherDao = new TeacherDao(application);
    }

    public boolean checkExistedPhone(String phone){
        return accountDao.checkExistedPhone(phone) > -1;
    }
    public boolean checkExistedEmail(String email){
        return accountDao.checkExistedEmail(email) ;
    }
    public void saveInformationRegister(String phone, String email, String password){
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
    public String getEmailByPhone(String phone){
        return accountDao.getEmailByPhone(phone);
    }
    public void insertAccount() {
        accountDao.insertAccount(new Account(1,email,password));
        int accountId = accountDao.getAccountIdByEmail(email);
        teacherDao.insertTeacher(new Teacher(1,"",accountId,"",phone));
    }
}
