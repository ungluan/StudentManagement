package com.example.studentmanagement.feature.GradeScreen;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.AppDatabase;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.databinding.DialogAddGradeBinding;
import com.example.studentmanagement.databinding.FragmentGradeScreenBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.example.studentmanagement.utils.ItemMargin;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


public class GradeScreenFragment extends Fragment {
    private FragmentGradeScreenBinding binding;
    private OmegaRecyclerView recyclerView;
    private GradeViewModel gradeViewModel;
    private GradeListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGradeScreenBinding.inflate(inflater);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gradeViewModel = new
                ViewModelProvider(requireActivity()).get(GradeViewModel.class);

        recyclerView = binding.recyclerViewGrade;

        // Set data to recycler view
        adapter = new GradeListAdapter(gradeViewModel, new GradeListAdapter.GradeDiff());
        recyclerView.setAdapter(adapter);

        // Add margin to recycler view item
        recyclerView.addItemDecoration(
                new ItemMargin(16, 0, 0, 16));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter.submitList(gradeViewModel.getGrades());
        recyclerView.setAdapter(adapter);

        binding.fab.setOnClickListener(fab -> showAddGradeDialog(requireContext()));
        binding.btnBack.setOnClickListener(
                v -> {
                    NavDirections action = GradeScreenFragmentDirections.actionGradeScreenFragmentToHomeFragment();
                    Navigation.findNavController(v).navigate(action);
                }
        );
    }

    public void showToast(String message){
        Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showAddGradeDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAddGradeBinding binding = DialogAddGradeBinding.inflate(
                LayoutInflater.from(context)
        );
        dialog.setContentView(binding.getRoot());
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);


        binding.btnAdd.setOnClickListener(v -> {
            String gradeId = AppUtils.formatGradeName(String.valueOf(binding.editTextGradeName.getText()));
            String teacherName = AppUtils.formatPersonName(String.valueOf(binding.editTextTeacherName.getText()));

            if (gradeId.equals("") || teacherName.equals("")) {
                if (gradeId.equals(""))
                    binding.textInputLayoutGradeName.setError("Tên lớp không được trống.");
                if (teacherName.equals(""))
                    binding.textInputLayoutTeacherName.setError("Tên GVCN không được trống.");
                return;
            }
            Grade grade = new Grade(gradeId,teacherName);
            // Chưa check id
            if(!gradeViewModel.checkGradeId(gradeId)){
                if(gradeViewModel.insertGrade(grade)){
                    showToast("Thêm lớp thành công");
                    List<Grade> gradeList = new ArrayList<>(adapter.getCurrentList());
                    gradeList.add(grade);
                    adapter.submitList(gradeList);
                    dialog.dismiss();
                }else{
                    AppUtils.showNotificationDialog(context,"Thông báo","Thêm lớp thất bại!");
                    dialog.dismiss();
                }
            }else{
                AppUtils.showNotificationDialog(context,"Thông báo","Lớp "+gradeId+" đã tồn tại!");
            }
        });
        dialog.show();
    }
}