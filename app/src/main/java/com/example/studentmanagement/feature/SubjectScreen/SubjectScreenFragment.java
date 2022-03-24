package com.example.studentmanagement.feature.SubjectScreen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.databinding.DialogAddSubjectBinding;
import com.example.studentmanagement.databinding.FragmentSubjectScreenBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.example.studentmanagement.utils.ItemMargin;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

public class SubjectScreenFragment extends Fragment {
    private FragmentSubjectScreenBinding binding;
    private OmegaRecyclerView recyclerView;
    private com.example.studentmanagement.feature.SubjectScreen.SubjectViewModel subjectViewModel;

    public SubjectScreenFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubjectScreenBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subjectViewModel = new
                ViewModelProvider(requireActivity()).get(com.example.studentmanagement.feature.SubjectScreen.SubjectViewModel.class);

        recyclerView = binding.recyclerViewSubject;

        //set data to recycler view
        SubjectListAdapter adapter = new
                SubjectListAdapter(subjectViewModel, new SubjectListAdapter.SubjectDiff());
        recyclerView.setAdapter(adapter);

        // add margin to recyccler view item

        recyclerView.addItemDecoration(new ItemMargin(
                16,0,0,16
        ));

        subjectViewModel.getAllSubject().observe(this.getViewLifecycleOwner(), subjects ->
                {
                    adapter.submitList(subjects);
                    recyclerView.setAdapter(adapter);
                }
                );
        binding.btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddSubjectDialog(requireContext());
            }
        });
    }

    private void showAddSubjectDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);

        DialogAddSubjectBinding binding = DialogAddSubjectBinding.inflate(
                LayoutInflater.from(context)
        );
        dialog.setContentView(binding.getRoot());

        binding.dialogTitleAddSubject.setText("Thêm môn học");
        binding.btnConfirmAddSubject.setText("THÊM");

        //check input



        binding.btnConfirmAddSubject.setOnClickListener(
                view -> {
                    String id = binding.editTextSubjectId.getText().toString();
                    String name = binding.editTextSubjectName.getText().toString();
                    String factor = binding.editTextSubjectCoefficient.getText().toString();
                    if (id.equals("") || name.equals("")|| factor.equals("")) {
//            if (gradeId.equals(""))
//                binding.textInputLayoutGradeName.setError("Tên lớp không được trống.");
//            if (teacherName.equals(""))
//                binding.textInputLayoutTeacherName.setError("Tên GVCN không được trống.");
                        return;
                    }
                    // check ok

                    int fac = Integer.parseInt(factor);
                    subjectViewModel.getSubjectById(id).subscribe(
                            subject -> binding.textInputSubjectId.setError("Mã môn học đã tồn tại"),
                            throwable -> AppUtils.showNotificationDialog(
                                    context,
                                    "Thêm lớp thất bại",
                                    throwable.getLocalizedMessage()),
                            () -> subjectViewModel.insertSubject(new Subject(id, name, fac)).
                                    subscribe(
                                            () -> {Toast.makeText(context, "Thêm môn học thành công", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();},
                                            throwable -> AppUtils.showNotificationDialog(
                                                    context,
                                                    "Thêm môn học thất bại",
                                                    throwable.getLocalizedMessage()
                                            )
                                    )

                    );


                }
        );
        dialog.show();


    }
}
