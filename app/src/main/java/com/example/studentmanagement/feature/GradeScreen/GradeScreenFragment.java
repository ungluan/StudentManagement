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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.databinding.DialogAddGradeBinding;
import com.example.studentmanagement.databinding.FragmentGradeScreenBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.example.studentmanagement.utils.ItemMargin;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GradeScreenFragment extends Fragment {
    private FragmentGradeScreenBinding binding;
    private OmegaRecyclerView recyclerView;
    private GradeViewModel gradeViewModel;
    private GradeListAdapter adapter;
    private final List<String> teacherItems = new ArrayList();
    private AutoCompleteTextView editTextTeacherId;

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

        List<Grade> gradeList = gradeViewModel.getGrades();
        adapter.submitList(gradeList);
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

    private void initialDropdown() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, teacherItems);
        if(teacherItems.size()>0){
            editTextTeacherId.setText(teacherItems.get(0));
            editTextTeacherId.setAdapter(arrayAdapter);
        }else editTextTeacherId.setText("Danh sách giáo viên trống!");
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showAddGradeDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAddGradeBinding binding = DialogAddGradeBinding.inflate(
                LayoutInflater.from(context)
        );
        dialog.setContentView(binding.getRoot());

        List<String> teacherList = gradeViewModel.getTeacherHaveNotGrade().stream()
                .map(item -> item.getId() +" - "+item.getTeacherName())
                .collect(Collectors.toList());
        teacherItems.clear();
        teacherItems.addAll(teacherList);

        editTextTeacherId = binding.editTextTeacherId;
        initialDropdown();
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);

        if(teacherItems.size()>0){
            binding.btnAdd.setOnClickListener(v -> {
                String gradeId = AppUtils.formatGradeName(
                        String.valueOf(binding.editTextGradeName.getText()));
                String teacherText = binding.editTextTeacherId.getText().toString();
                int teacherId = Integer.parseInt(teacherText.substring(0,teacherText.indexOf(" ")));
                if (gradeId.equals("")) {
                    binding.textInputLayoutGradeName.setError("Tên lớp không được trống.");
                    return;
                }
                // Chưa set Image
                Grade grade = new Grade(gradeId,teacherId,"");

                if(!gradeViewModel.checkGradeId(gradeId)){
                    if(gradeViewModel.insertGrade(grade)){
                        showToast("Thêm lớp thành công");
                        List<Grade> gradeList = new ArrayList<>(adapter.getCurrentList());
                        gradeList.add(grade);
                        adapter.submitList(gradeList);
                        adapter.notifyDataSetChanged();
                    }else{
                        AppUtils.showNotificationDialog(context,"Thông báo","Thêm lớp thất bại!");
                    }
                    dialog.dismiss();
                }else{
                    AppUtils.showNotificationDialog(context,"Thông báo","Lớp "+gradeId+" đã tồn tại!");
                }
            });
        }else{
            binding.btnAdd.setVisibility(View.INVISIBLE);
        }
        dialog.show();
    }
}