package com.example.studentmanagement.feature.RegisterScreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentmanagement.database_sqlite.Dao.AccountDao;
import com.example.studentmanagement.databinding.FragmentRegisterBinding;

// Đăng ký thành công -> Vào 1 Trang hỏi ? Đã được đăng ký thông tin - Chưa đăng ký thông tin.
// Đã được: Cho chọn DropDown Teacher -> Cập nhật thông tin.
// Chưa: Hình ảnh - Tên - SĐT (Mã TK)
// => Vào HomePage
public class RegisterViewModel extends AndroidViewModel {
    private AccountDao accountDao;
    public RegisterViewModel(@NonNull Application application) {
        super(application);
        accountDao = new AccountDao(application);
    }

    public boolean checkExistedPhone(String phone){
        return accountDao.checkExistedPhone(phone) > -1;
    }
    public boolean checkExistedEmail(String email){
        return accountDao.checkExistedEmail(email) ;
    }
}
