package com.example.studentmanagement.feature.loginScreen;

import static com.example.studentmanagement.utils.AppUtils.isValidEmail;
import static com.example.studentmanagement.utils.AppUtils.showNotificationDialog;
import static com.example.studentmanagement.utils.AppUtils.updateTeacherId;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentmanagement.databinding.FragmentLoginBinding;
import com.example.studentmanagement.utils.AppUtils;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppUtils.getTeacherId(requireActivity()) != -1){
            navigateToHomePage();
        }
    }

    private void navigateToHomePage(){
        NavDirections action = LoginFragmentDirections.actionLoginFragmentToHomeFragment();
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(action);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginViewModel loginViewModel = 
                new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
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
                else binding.textInputLayoutPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.btnLogin.setOnClickListener(v -> {
            String email = String.valueOf(binding.editTextEmail.getText());
            String password = String.valueOf(binding.editTextPassword.getText());
            if(!email.isEmpty() && !password.isEmpty()
                && !binding.textInputLayoutEmail.isErrorEnabled()
                && !binding.textInputLayoutPassword.isErrorEnabled()
            ){
                if(loginViewModel.login(email,password)){
                    Toast.makeText(getContext(), "Login Thành công", Toast.LENGTH_SHORT).show();
                    int teacherId = loginViewModel.getTeacherIdByEmail(email);
                    updateTeacherId(requireActivity(),teacherId);
                    navigateToHomePage();
                }else{
                    showNotificationDialog(requireContext(),"Đăng nhập thất bại",
                            "Tài khoản hoặc mật khẩu không chính xác. Vui lòng kiểm tra lại.",null);
                }
            }else{
                if(email.isEmpty()) binding.textInputLayoutEmail.setError("Email không được trống.");
                if(password.isEmpty()) binding.textInputLayoutPassword.setError("Mật khẩu không được trống.");
            }
        });
    }

}