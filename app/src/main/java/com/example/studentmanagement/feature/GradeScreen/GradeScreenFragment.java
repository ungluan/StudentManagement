package com.example.studentmanagement.feature.GradeScreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.databinding.DialogAddGradeBinding;
import com.example.studentmanagement.databinding.FragmentGradeScreenBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.example.studentmanagement.utils.ItemMargin;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.Objects;
import java.util.function.Function;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class GradeScreenFragment extends Fragment {
    private FragmentGradeScreenBinding binding;
    private OmegaRecyclerView recyclerView;
    private GradeViewModel gradeViewModel;
    private Disposable disposable;

    public GradeScreenFragment() {
        // Required empty public constructor
    }

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
        GradeListAdapter adapter = new GradeListAdapter(new GradeListAdapter.GradeDiff());
        recyclerView.setAdapter(adapter);


        // Add margin to recycler view item
        recyclerView.addItemDecoration(
                new ItemMargin(16, 0, 0, 16));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        gradeViewModel.getAllGrade().observe(this.getViewLifecycleOwner(), grades -> {
            adapter.submitList(grades);
            recyclerView.setAdapter(adapter);
        });
        binding.fab.setOnClickListener(fab -> {
            showAddGradeDialog(requireContext());
//            AppUtils.showNotificationDialog(requireContext(), "", "");
//            Toast.makeText(requireContext(), "Pressed", Toast.LENGTH_SHORT).show();
            /*disposable = gradeViewModel.insertGrade(new Grade("12A3", "Trần Huy Hoàng"))
                    .subscribe(
                            () -> *//*Observable.just("Successfull").
                                    observeOn(AndroidSchedulers.mainThread()).subscribe(
                                            s -> new AlertDialog.Builder(requireContext())
                                                    .setTitle(s).show()*//*
                                Log.d("Update","Successful")
                            *//*)*//*,
                            throwable -> {
                                Observable.just(throwable.getMessage()).
                                        observeOn(AndroidSchedulers.mainThread()).subscribe(
                                        s -> new AlertDialog.Builder(requireContext())
                                                .setTitle(s).show());
                            }
                    );*/
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private void showAddGradeDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAddGradeBinding binding = DialogAddGradeBinding.inflate(
                LayoutInflater.from(context)
        );
        dialog.setContentView(binding.getRoot());
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
        binding.btnAdd.setOnClickListener(v -> {
            String gradeId = String.valueOf(binding.editTextGradeName.getText());
            String teacherName = String.valueOf(binding.editTextTeacherName.getText());
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
                            requireContext(),
                            "Thêm lớp thất bại",
                            throwable.getLocalizedMessage()
                    ),
                    () -> {
                        gradeViewModel.insertGrade(new Grade(gradeId,teacherName))
                                .subscribe(
                                        () -> {
                                            Toast.makeText(
                                                    requireContext() ,"Thêm lớp thành công!",
                                                    Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        },
                                        throwable -> AppUtils.showNotificationDialog(
                                                requireContext(),
                                                "Thêm lớp thất bại",
                                                throwable.getLocalizedMessage()
                                        )
                                );
                    }

            );
        });

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
        dialog.show();
    }
}