package com.example.studentmanagement.feature.ProfileScreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.FragmentChangePasswordBinding;


public class ChangePasswordFragment extends Fragment {
    private FragmentChangePasswordBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonChangePassword.setOnClickListener(v -> {

        });
    }
}