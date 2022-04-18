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
import com.example.studentmanagement.feature.loginScreen.LoginViewModel;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private RegisterViewModel registerViewModel;
    private LoginViewModel loginViewModel;
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
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        registerViewModel.setIsRegisterPage(true);

        handleInputTextFields();

        binding.btnBack.setOnClickListener(v -> navigateToLoginPage(view));


        binding.btnRegister.setOnClickListener(v -> {
            String email = String.valueOf(binding.editTextEmail.getText());
            String password = String.valueOf(binding.editTextPassword.getText());
            String repeatPassword = String.valueOf(binding.editTextRepeatPassword.getText());
            String phone = String.valueOf(binding.editTextPhone.getText());

            validator(email,password,repeatPassword,phone);

           if(!binding.textInputLayoutEmail.isErrorEnabled() && !binding.textInputLayoutPassword.isErrorEnabled()
            && !binding.textInputLayoutRepeatPassword.isErrorEnabled() && !binding.textInputLayoutPhone.isErrorEnabled()){
                registerViewModel.saveInformationRegister(phone,email,password);
                loginViewModel.saveInformation(email,password);
                navigateToOTPPage(v);
            }
        });
    }
    private void navigateToLoginPage(View view){
        NavDirections action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();
        Navigation.findNavController(view).navigate(action);
    }
    private void navigateToOTPPage(View view){
        NavDirections action = RegisterFragmentDirections.actionRegisterFragmentToOtpFragment();
        Navigation.findNavController(view).navigate(action);
    }
    private void handleInputTextFields(){
        binding.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isValidEmail(s)) binding.textInputLayoutEmail.setError(getString(R.string.validate_email_01));
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
                if(s.length()<6) binding.textInputLayoutPassword.setError(getString(R.string.validate_password_01));
                else if (!s.toString().equals(binding.editTextRepeatPassword.getText().toString())) {
                    binding.textInputLayoutPassword.setErrorEnabled(false);
                    binding.textInputLayoutRepeatPassword.setError(getString(R.string.validate_repeat_password_01));
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
                    binding.textInputLayoutRepeatPassword.setError(getString(R.string.validate_repeat_password_02));
                else {
                    binding.textInputLayoutRepeatPassword.setErrorEnabled(false);
                    if(binding.editTextRepeatPassword.getText().toString().length()>=6){
                        binding.textInputLayoutPassword.setErrorEnabled(false);
                    }
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
                    binding.textInputLayoutPhone.setError(getString(R.string.validate_phone_01));
                else binding.textInputLayoutPhone.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void validator(String email, String password, String repeatPassword, String phone){
        if(email.isEmpty()) binding.textInputLayoutEmail.setError("Email không được trống.");
        if(password.isEmpty()) binding.textInputLayoutPassword.setError("Mật khẩu không được trống.");
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
    }
}