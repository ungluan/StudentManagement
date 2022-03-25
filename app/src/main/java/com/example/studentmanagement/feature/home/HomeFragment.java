package com.example.studentmanagement.feature.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {
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
        // Hiện tại đang phải call tuần tự hay bất đồng bộ?
        homeViewModel.getNumberOfGrades().subscribe(
                number -> binding.txtNumberOfGrades.setText(
                        getString(R.string.number_and_noun, number, "Lớp")),
                throwable -> Log.d("HomeFragment", throwable.getMessage()),
                () -> Log.d("HomeFragment", "onCompleted")
        );
        homeViewModel.getNumberOfSubjects().subscribe(
                number -> binding.txtNumberOfSubjects.setText(
                        getString(R.string.number_and_noun, number, "Môn")),
                throwable -> Log.d("HomeFragment", throwable.getMessage()),
                () -> Log.d("HomeFragment", "onCompleted")
        );
        homeViewModel.getNumberOfStudents().subscribe(
                number -> binding.txtNumberOfStudents.setText(
                        getString(R.string.number_and_noun, number, "Học sinh")),
                throwable -> Log.d("HomeFragment", throwable.getMessage()),
                () -> Log.d("HomeFragment", "onCompleted")
        );
        binding.cardViewGrade.setOnClickListener(
                v -> {
                    NavDirections action = HomeFragmentDirections.actionHomeFragmentToGradeScreenFragment();
                    Navigation.findNavController(v).navigate(action);
                }
        );




        binding.cardViewStudent.setOnClickListener(
                v -> {
                    NavDirections action = HomeFragmentDirections.actionHomeFragmentToStudentScreenFragment();
                    Navigation.findNavController(v).navigate(action);
                }
        );
        binding.cardViewSubject.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToMarkScreenFragment();
            Navigation.findNavController(v).navigate(action);
        });
        /*homeViewModel.getGradesWithStudents().subscribe(
                gradeWithStudents -> {
                    gradeWithStudents.forEach(
                           gradeWithStudents1 -> Log.d(
                                   "HomeFragment",
                                   gradeWithStudents1.toString()
                           )
                    );
                }
        );*/

    }
}