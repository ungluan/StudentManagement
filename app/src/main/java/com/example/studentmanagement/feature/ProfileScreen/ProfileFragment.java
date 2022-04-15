package com.example.studentmanagement.feature.ProfileScreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.FragmentProfileBinding;
import com.example.studentmanagement.feature.home.HomeFragmentDirections;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnBack.setOnClickListener(v -> {
            NavDirections action = ProfileFragmentDirections.actionProfileFragmentToHomeFragment();
            Navigation.findNavController(v).navigate(action);
        });
        binding.buttonProfile.setOnClickListener(v -> {
            NavDirections action = ProfileFragmentDirections.actionProfileFragmentToInformationFragment();
            Navigation.findNavController(v).navigate(action);
        });
        binding.buttonChangePassword.setOnClickListener(v -> {
            NavDirections action = ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment();
            Navigation.findNavController(v).navigate(action);
        });
        binding.buttonCreateAccount.setOnClickListener(v -> {

        });
    }
}