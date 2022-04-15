package com.example.studentmanagement.feature.RegisterScreen;

import static com.example.studentmanagement.utils.AppUtils.isValidEmail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerViewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
        binding.btnBack.setOnClickListener(v -> {
            NavDirections action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();
            Navigation.findNavController(v).navigate(action);
        });

        binding.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isValidEmail(s)) binding.textInputLayoutEmail.setError("Email không hợp lệ.");
                else binding.textInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<6) binding.textInputLayoutPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
                else if (!s.toString().equals(binding.editTextRepeatPassword.getText().toString())) {
                    binding.textInputLayoutPassword.setErrorEnabled(false);
                    binding.textInputLayoutRepeatPassword.setError("Mật khẩu phải trùng với mật khẩu ở trên.");
                } else {
                    binding.textInputLayoutPassword.setErrorEnabled(false);
                    binding.textInputLayoutRepeatPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.editTextRepeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean x = s.toString().equals(binding.editTextPassword.getText().toString());
                if (!x)
                    binding.textInputLayoutRepeatPassword.setError("Mật khẩu phải trùng với mật khẩu mới");
                else {
                    binding.textInputLayoutPassword.setErrorEnabled(false);
                    binding.textInputLayoutRepeatPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=10 || s.charAt(0) != '0' )
                    binding.textInputLayoutPhone.setError("Số điện thoại không hợp lệ.");
                else binding.textInputLayoutPhone.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            String repeatPassword = binding.editTextRepeatPassword.getText().toString();
            String phone = binding.editTextPhone.getText().toString();

            if(email.isEmpty()) binding.textInputLayoutEmail.setError("Email không được trống.");
            if(password.isEmpty()) binding.textInputLayoutEmail.setError("Mật khẩu không được trống.");
            if(repeatPassword.isEmpty()) binding.textInputLayoutRepeatPassword.setError("Nhập lại mật khẩu không được trống.");
            if(phone.isEmpty()) binding.textInputLayoutPhone.setError("Số điện thoại không được trống");
            // Check email existed?
            if(!binding.textInputLayoutEmail.isErrorEnabled() &&
                    registerViewModel.checkExistedEmail(email))
                binding.textInputLayoutEmail.setError("Email đã tồn tại.");
            // Check phone existed?
            if(!binding.textInputLayoutPhone.isErrorEnabled() &&
                    registerViewModel.checkExistedPhone(phone))
                binding.textInputLayoutPhone.setError("Số điện thoại đã tồn tại.");
            // Số điện thoại này đã có tài khoản nào đăng ký chưa
            if(!binding.textInputLayoutEmail.isErrorEnabled() && !binding.textInputLayoutPassword.isErrorEnabled()
            && !binding.textInputLayoutRepeatPassword.isErrorEnabled() && !binding.textInputLayoutPhone.isErrorEnabled()){
                // SĐT 2 trường hợp:
                // TH1: Đã đăng ký cho giáo viên nhưng chưa tạo tài khoản
                // TH2: Và chưa đăng ký gì cả

                // Nhảy quả OTP_page ==> Nếu TH1: Nhảy thẳng vào Home
                // ===> TH2: Nhảy vào UpdateProfilePage "Hinh anh - Ten"

                //=======> Cứ tạo Teacher Và Account như bình thường
                //=======> Login thành công sẽ phải kiểm tra
                // TH1: gv này đã có đầy đủ thông tin chưa ? Chưa thì cập nhật : Nhảy vào Home

                // Logic - SendOTP -> Navigate to OTP_Page

                NavDirections action = OtpFragmentDirections.actionOtpFragmentToRegisterFragment();
                Navigation.findNavController(v).navigate(action);
            }
        });
    }
}