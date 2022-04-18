package com.example.studentmanagement.feature.ProfileScreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.FragmentChangePasswordBinding;
import com.example.studentmanagement.feature.loginScreen.LoginViewModel;
import com.example.studentmanagement.utils.AppUtils;


public class ChangePasswordFragment extends Fragment {
    private FragmentChangePasswordBinding binding;
    private ChangePasswordViewModel changePasswordViewModel;
    private boolean isForgetPassword;
    private LoginViewModel loginViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changePasswordViewModel = new ViewModelProvider(requireActivity()).get(ChangePasswordViewModel.class);
        isForgetPassword = changePasswordViewModel.isForgotPassword();
        if(!isForgetPassword) binding.textInputLayoutOldPassword.setVisibility(View.VISIBLE);
        else binding.textInputLayoutOldPassword.setVisibility(View.INVISIBLE);

        if(!isForgetPassword) binding.editTextOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6)
                    binding.textInputLayoutOldPassword.setError("Mật khẩu phải có ít nhất 6 ký tự.");
                else binding.textInputLayoutOldPassword.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.editTextNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() < 6)
                    binding.textInputLayoutNewPassword.setError("Mật khâu phải có ít nhất 6 ký tự.");
                else if (!s.toString().equals(binding.editTextRepeatPassword.getText().toString())) {
                    binding.textInputLayoutNewPassword.setErrorEnabled(false);
                    binding.textInputLayoutRepeatPassword.setError("Mật khẩu phải trùng với mật khẩu mới.");
                } else {
                    binding.textInputLayoutNewPassword.setErrorEnabled(false);
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
                boolean x = s.toString().equals(binding.editTextNewPassword.getText().toString());
                if (!x)
                    binding.textInputLayoutRepeatPassword.setError("Mật khẩu phải trùng với mật khẩu mới");
                else {
                    binding.textInputLayoutNewPassword.setErrorEnabled(false);
                    binding.textInputLayoutRepeatPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.btnBack.setOnClickListener(v -> {
            if(!isForgetPassword){
                NavDirections action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToProfileFragment();
                Navigation.findNavController(v).navigate(action);
            }
            else{
                NavDirections action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToForgotPasswordFragment();
                Navigation.findNavController(v).navigate(action);
            }
        });
        binding.buttonChangePassword.setOnClickListener(v -> {
            String oldPassword = binding.editTextOldPassword.getText().toString();
            String newPassword = binding.editTextNewPassword.getText().toString();
            String repeatPassword = binding.editTextRepeatPassword.getText().toString();

            if (!isForgetPassword && oldPassword.isEmpty())
                binding.textInputLayoutOldPassword.setError("Vui lòng nhập mật khẩu hiện tại.");
            if (newPassword.isEmpty())
                binding.textInputLayoutNewPassword.setError("Vui lòng nhập mật khẩu mới.");
            if (repeatPassword.isEmpty())
                binding.textInputLayoutRepeatPassword.setError("Vui lòng nhập lại mật khẩu mới.");

            boolean b1 = !binding.textInputLayoutOldPassword.isErrorEnabled();
            boolean b2 = !binding.textInputLayoutNewPassword.isErrorEnabled();
            boolean b3 = !binding.textInputLayoutRepeatPassword.isErrorEnabled();
            if (b1 && b2 && b3) {
                if (!isForgetPassword && !changePasswordViewModel.checkPassword(AppUtils.getTeacherId(requireActivity()), oldPassword))
                    binding.textInputLayoutOldPassword.setError("Mật khẩu không trùng khớp với mật khẩu hiện tại.");
                else {
                    int accountId = changePasswordViewModel.getAccountIdByTeacherId(
                            AppUtils.getTeacherId(requireActivity()));
                    changePasswordViewModel.updatePassword(accountId, newPassword);
                    AppUtils.showNotificationDialog(requireContext(),
                            "Đổi mật khẩu thành công",
                            "", () -> {
                                if(!isForgetPassword) navigateToProfilePage(view);
                                else {
                                    loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
                                    loginViewModel.setPassword(binding.editTextNewPassword.getText().toString());
                                    navigateToLoginPage(view);
                                }
                                return null;
                            });
                }
            }
        });
    }
    private void navigateToLoginPage(View view){
        NavDirections action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToLoginFragment();
        Navigation.findNavController(view).navigate(action);
    }
    private void navigateToProfilePage(View view){
        NavDirections action = ChangePasswordFragmentDirections.actionChangePasswordFragmentToProfileFragment();
        Navigation.findNavController(view).navigate(action);
    }
}