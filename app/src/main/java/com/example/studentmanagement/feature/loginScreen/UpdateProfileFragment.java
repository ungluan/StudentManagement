package com.example.studentmanagement.feature.loginScreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.databinding.FragmentUpdateProfileBinding;
import com.example.studentmanagement.utils.AppUtils;

public class UpdateProfileFragment extends Fragment {
    private FragmentUpdateProfileBinding binding;
    private int CODE = 123;
    private String imageString;
    private LoginViewModel loginViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUpdateProfileBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        binding.btnChooseImageTeacher.setOnClickListener(v -> {
            AppUtils.chooseImage(requireContext(), binding.imageViewTeacher, CODE);
            imageString = AppUtils.getImageString(CODE);
        });

        binding.btnUpdate.setOnClickListener(v -> {
            if(binding.editTextFullName.getText().toString().isEmpty()){
                binding.textInputLayoutFullName.setError("Họ và tên không được trống.");
                return;
            }
            updateTeacher();
            navigateToHomePage(v);
        });
    }
    private void navigationToLoginPage(View view){
        // Này bắt buộc cập nhật
    }
    private void updateTeacher(){
        Teacher teacher = loginViewModel.getTeacherById(AppUtils.getTeacherId(requireActivity()));
        teacher.setTeacherName(binding.editTextFullName.getText().toString());
        teacher.setImageUrl(AppUtils.getImageString(CODE));
        loginViewModel.updateTeacher(teacher);
    }
    private void navigateToHomePage(View view){
        NavDirections action = UpdateProfileFragmentDirections.actionUpdateProfileFragmentToHomeFragment();
        Navigation.findNavController(view).navigate(action);
    }
}