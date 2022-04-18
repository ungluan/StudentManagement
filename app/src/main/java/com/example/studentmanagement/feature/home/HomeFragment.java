package com.example.studentmanagement.feature.home;

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
import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.databinding.FragmentHomeBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private Teacher teacher;

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
        teacher = homeViewModel.getTeacherById(AppUtils.getTeacherId(requireActivity()));
        binding.txtName.setText(getString(R.string.str_teacher_name,AppUtils.getLastName(teacher.getTeacherName())));
        if(!teacher.getImageUrl().equals(""))
        Picasso.get().load(teacher.getImageUrl()).into(binding.imageAvatar);

        binding.cardViewGrade.setOnClickListener(this::navigateToGradePage);
        binding.cardViewStudent.setOnClickListener(this::navigateToStudentPage);
        binding.cardViewSubject.setOnClickListener(this::navigateToSubjectPage);
        binding.cardViewMark.setOnClickListener(this::navigateToMarkPage);
        binding.cardViewQuery.setOnClickListener(this::navigateToReportPage);
        binding.cardViewTeacher.setOnClickListener(this::navigateToTeacherPage);
        binding.buttonAvatar.setOnClickListener(this::navigateToProfilePage);
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.txtNumberOfGrades.setText(getString(R.string.number_and_noun,homeViewModel.getNumberOfGrades(),"Lớp"));
        binding.txtNumberOfStudents.setText(getString(R.string.number_and_noun,homeViewModel.getNumberOfStudents(),"Học sinh"));
        binding.txtNumberOfSubjects.setText(getString(R.string.number_and_noun,homeViewModel.getNumberOfSubjects(),"Môn"));
    }

    private void navigateToGradePage(View view){
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToGradeScreenFragment();
        Navigation.findNavController(view).navigate(action);
    }
    private void navigateToStudentPage(View view){
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToStudentScreenFragment();
        Navigation.findNavController(view).navigate(action);
    }
    private void navigateToMarkPage(View view){
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToMarkScreenFragment();
        Navigation.findNavController(view).navigate(action);
    }
    private void navigateToSubjectPage(View view){
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToSubjectScreenFragment();
        Navigation.findNavController(view).navigate(action);
    }
    private void navigateToReportPage(View view){
        Navigation.findNavController(view).
                navigate(R.id.action_homeFragment_to_fragmentReportScreen);
    }
    private void navigateToTeacherPage(View view){
        Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_teacherScreenFragment);
    }
    private void navigateToProfilePage(View view){
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToProfileFragment();
        Navigation.findNavController(view).navigate(action);
    }
}