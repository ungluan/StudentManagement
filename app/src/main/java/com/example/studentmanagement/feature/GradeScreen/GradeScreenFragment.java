package com.example.studentmanagement.feature.GradeScreen;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.databinding.DialogAddGradeBinding;
import com.example.studentmanagement.databinding.FragmentGradeScreenBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.example.studentmanagement.utils.ItemMargin;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;


import io.reactivex.rxjava3.disposables.Disposable;


public class GradeScreenFragment extends Fragment {
    private FragmentGradeScreenBinding binding;
    private OmegaRecyclerView recyclerView;
    private GradeViewModel gradeViewModel;

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
        GradeListAdapter adapter = new GradeListAdapter(gradeViewModel, new GradeListAdapter.GradeDiff());
        recyclerView.setAdapter(adapter);


        // Add margin to recycler view item
        recyclerView.addItemDecoration(
                new ItemMargin(16, 0, 0, 16));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        gradeViewModel.getAllGrade().observe(this.getViewLifecycleOwner(), grades -> {
            adapter.submitList(grades);
            recyclerView.setAdapter(adapter);
        });

        binding.fab.setOnClickListener(fab -> showAddGradeDialog(requireContext()));

        binding.btnBack.setOnClickListener(
                v -> {
                    NavDirections action = GradeScreenFragmentDirections.actionGradeScreenFragmentToHomeFragment();
                    Navigation.findNavController(v).navigate(action);
                }
            );
    }


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

            gradeViewModel.getGradeById(gradeId).subscribe(
                    grade -> binding.textInputLayoutGradeName.setError("Mã lớp đã tồn tại."),
                    throwable -> AppUtils.showNotificationDialog(
                            context,
                            "Thêm lớp thất bại",
                            throwable.getLocalizedMessage()
                    ),
                    () -> gradeViewModel.insertGrade(new Grade(gradeId, teacherName))
                            .subscribe(
                                    () -> {
                                        Toast.makeText(
                                                context, "Thêm lớp thành công!",
                                                Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    },
                                    throwable -> AppUtils.showNotificationDialog(
                                            context,
                                            "Thêm lớp thất bại",
                                            throwable.getLocalizedMessage()
                                    )
                            )
            );
        });
        dialog.show();
    }
}