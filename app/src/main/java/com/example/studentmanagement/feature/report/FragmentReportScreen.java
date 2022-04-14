package com.example.studentmanagement.feature.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.FragmentReportBinding;
import com.example.studentmanagement.feature.home.HomeFragmentDirections;

public class FragmentReportScreen extends Fragment {
    private FragmentReportBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragmentReportBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setEvents();

    }

    private void setEvents() {

        changeFragment();
    }

    private void changeFragment() {
//        binding.cardViewRankStudentReport.setOnClickListener(v -> {
//            NavDirections action = FragmentReportScreenDirections.actionFragmentReportScreenToFragmentRankStudentScreen();
//            Navigation.findNavController(v).navigate(action);
//        });


        binding.cardViewRankStudentReport.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_fragmentReportScreen_to_fragmentRankStudentScreen);
        });

        // back to home
        binding.btnBackSubjectScreen.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_fragmentReportScreen_to_homeFragment);
        });
    }
}
