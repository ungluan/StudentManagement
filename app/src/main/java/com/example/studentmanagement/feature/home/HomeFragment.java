package com.example.studentmanagement.feature.home;

import static com.example.studentmanagement.utils.AppUtils.updateTeacherId;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database_sqlite.DataBaseHelper;
import com.example.studentmanagement.databinding.FragmentHomeBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.google.android.material.navigation.NavigationView;


public class HomeFragment extends Fragment {

    //save state

    public static final String TAG = HomeFragment.class.getName();
    // HomeViewModel initial in onCreateView
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        binding.txtName.setText(getString(R.string.str_teacher_name,homeViewModel.getFullNameTeacher(
                AppUtils.getTeacherId(requireActivity()))));
        binding.cardViewGrade.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToGradeScreenFragment();
            Navigation.findNavController(v).navigate(action);
        });
        binding.cardViewStudent.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToStudentScreenFragment();
            Navigation.findNavController(v).navigate(action);
        });
        binding.cardViewSubject.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToSubjectScreenFragment();
            Navigation.findNavController(v).navigate(action);
        });
        binding.cardViewMark.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToMarkScreenFragment();
            Navigation.findNavController(v).navigate(action);
        });

        binding.cardViewQuery.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_fragmentReportScreen);
        });

        binding.cardViewTeacher.setOnClickListener(v -> {
                    Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_teacherScreenFragment);
                });


        binding.buttonAvatar.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToProfileFragment();
            Navigation.findNavController(v).navigate(action);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.txtNumberOfGrades.setText(getString(R.string.number_and_noun,homeViewModel.getNumberOfGrades(),"Lớp"));
        binding.txtNumberOfStudents.setText(getString(R.string.number_and_noun,homeViewModel.getNumberOfStudents(),"Học sinh"));
        binding.txtNumberOfSubjects.setText(getString(R.string.number_and_noun,homeViewModel.getNumberOfSubjects(),"Môn"));
    }
}