package com.example.studentmanagement.feature.RegisterScreen;

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
import com.example.studentmanagement.databinding.FragmentForgotPasswordBinding;
import com.example.studentmanagement.databinding.FragmentOtpBinding;
import com.example.studentmanagement.feature.ProfileScreen.ChangePasswordViewModel;

public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding binding;
    private RegisterViewModel registerViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerViewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
        registerViewModel.setIsRegisterPage(false);
        binding.editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()!=10)
                    binding.textInputLayoutPhone.setError("Số điện thoại không hợp lệ.");
                else binding.textInputLayoutPhone.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.btnBack.setOnClickListener(v -> {
            NavDirections action = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment() ;
            Navigation.findNavController(v).navigate(action);
        });
        binding.btnConfirm.setOnClickListener(v -> {
            if(binding.editTextPhone.length()==0) {
                return;
            }
            if(!registerViewModel.checkExistedPhone(binding.editTextPhone.getText().toString())){
                binding.textInputLayoutPhone.setError("Số điện thoại không tồn tại.");
                return;
            }
            if(!binding.textInputLayoutPhone.isErrorEnabled()){
                String phone = binding.editTextPhone.getText().toString();
                String email = registerViewModel.getEmailByPhone(phone);
                registerViewModel.saveInformationRegister(phone,email,"");
                navigateToOtpPage(v);
            }
        });
    }
    private void navigateToOtpPage(View view){
        NavDirections action = ForgotPasswordFragmentDirections
                .actionForgotPasswordFragmentToOtpFragment();
        Navigation.findNavController(view).navigate(action);
    }
}