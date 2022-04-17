package com.example.studentmanagement.feature.ProfileScreen;

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

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.FragmentInfomationBinding;
import com.example.studentmanagement.feature.home.HomeFragmentDirections;
import com.example.studentmanagement.utils.AppUtils;


public class InformationFragment extends Fragment {
    private FragmentInfomationBinding binding;
    private InformationViewModel informationViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInfomationBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        informationViewModel = new ViewModelProvider(requireActivity()).get(InformationViewModel.class);

        binding.setTeacher(informationViewModel.getTeacherById(AppUtils.getTeacherId(requireActivity())));
        int accountId = binding.getTeacher().getIdAccount();
        binding.txtEmail.setText(informationViewModel.getAccountById(accountId).getEmail());

        binding.btnBack.setOnClickListener(this::navigateToProfilePage);
    }
    private void navigateToProfilePage(View view){
        NavDirections action = InformationFragmentDirections.actionInformationFragmentToProfileFragment();
        Navigation.findNavController(view).navigate(action);
    }
}