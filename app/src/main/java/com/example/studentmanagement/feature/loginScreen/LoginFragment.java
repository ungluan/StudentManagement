package com.example.studentmanagement.feature.loginScreen;

import static com.example.studentmanagement.utils.AppUtils.isValidEmail;
import static com.example.studentmanagement.utils.AppUtils.showNotificationDialog;
import static com.example.studentmanagement.utils.AppUtils.updateInformation;
import static com.example.studentmanagement.utils.AppUtils.updateTeacherId;


import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.databinding.FragmentLoginBinding;
import com.example.studentmanagement.utils.AppUtils;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import papaya.in.sendmail.SendMail;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding ;
    private LoginViewModel loginViewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppUtils.getTeacherId(requireActivity()) != -1){
            if(AppUtils.getInformation(requireActivity())) navigateToHomePage();
            else navigateToUpdateProfilePage();
        }
    }

    private void navigateToHomePage(){
        NavDirections action = LoginFragmentDirections.actionLoginFragmentToHomeFragment();
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(action);
    }
    private void navigateToUpdateProfilePage(){
        NavDirections action = LoginFragmentDirections.actionLoginFragmentToUpdateProfileFragment();
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
        loginViewModel =
                new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        if(loginViewModel.getEmail()!=null) binding.editTextEmail.setText(loginViewModel.getEmail());
        if(loginViewModel.getPassword()!=null) binding.editTextPassword.setText(loginViewModel.getPassword());
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
//                    int teacherId = loginViewModel.getTeacherIdByEmail(email);
                    Teacher teacher = loginViewModel.getTeacherByEmail(email);
                    updateTeacherId(requireActivity(),teacher.getId());
                    updateInformation(requireActivity(),!teacher.getTeacherName().isEmpty());
                    sendEmail();
                    if (loginViewModel.isUpdateInformation(teacher.getId())) {
                        navigateToHomePage();
                    } else {
                        navigateToUpdateProfilePage();
                    }
                }else{
                    showNotificationDialog(requireContext(),"Đăng nhập thất bại",
                            "Tài khoản hoặc mật khẩu không chính xác. Vui lòng kiểm tra lại.",null);
                }
            }else{
                if(email.isEmpty()) binding.textInputLayoutEmail.setError("Email không được trống.");
                if(password.isEmpty()) binding.textInputLayoutPassword.setError("Mật khẩu không được trống.");
            }
        });
        binding.txtRegister.setOnClickListener(v -> {
            NavDirections action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment();
            Navigation.findNavController(v).navigate(action);
        });
        binding.txtForgotPassword.setOnClickListener(v -> {
            NavDirections action = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment();
            Navigation.findNavController(v).navigate(action);
        });
    }
    private void sendEmail(){
        SendMail mail = new SendMail("ungluan01@gmail.com", "testingapp",
                String.valueOf(binding.editTextEmail.getText()).trim(),
                "Cảnh báo đăng nhập.",
                "Tài khoản của bạn vừa mới đăng nhập trên thiết bị "+getDeviceName());
        mail.execute();
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}