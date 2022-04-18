package com.example.studentmanagement.feature.StudentScreen;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.databinding.DialogAddStudentBinding;
import com.example.studentmanagement.databinding.FragmentStudentScreenBinding;
import com.example.studentmanagement.utils.AppUtils;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import io.reactivex.rxjava3.core.Observable;


public class StudentScreenFragment extends Fragment {
    private FragmentStudentScreenBinding binding;
    private StudentViewModel studentViewModel;
    private AutoCompleteTextView editTextGradeName;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> dropdownItems = new ArrayList<>();
    private OmegaRecyclerView recyclerView;
    private StudentListAdapter studentListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentScreenBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Call
        studentViewModel = new ViewModelProvider(requireActivity()).get(StudentViewModel.class);
        editTextGradeName = binding.editTextGradeName;
        // setup Recycler View
        studentListAdapter = new StudentListAdapter(studentViewModel, new StudentListAdapter.StudentDiff());
        recyclerView = binding.recyclerViewStudent;
        recyclerView.setAdapter(studentListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        initialStudentScreen();


        editTextGradeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Clicked", "Position: " + position + " ID: " + id);
                loadRecyclerViewStudent(dropdownItems.get(position));
            }
        });

        binding.fab.setOnClickListener(v -> showAddStudentDialog(requireContext()));

        binding.btnBack.setOnClickListener(v -> {
            NavDirections action = StudentScreenFragmentDirections.actionStudentScreenFragmentToHomeFragment();
            Navigation.findNavController(v).navigate(action);
        });


    }

    private void loadRecyclerViewStudent(String gradeId) {
        studentListAdapter.submitList(studentViewModel.getStudentsByGradeId(gradeId));
    }

    private void initialDropdown() {
        arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, dropdownItems);
        if(dropdownItems.size()>0){
            editTextGradeName.setText(dropdownItems.get(0));
            editTextGradeName.setAdapter(arrayAdapter);
            loadRecyclerViewStudent(dropdownItems.get(0));
        }
    }

    private void initialStudentScreen() {
        dropdownItems = Observable.fromIterable(studentViewModel.getGrades())
                .map(Grade::getGradeId).toList().blockingGet();
        initialDropdown();
    }

    private void showAddStudentDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
        DialogAddStudentBinding binding = DialogAddStudentBinding.inflate(
                LayoutInflater.from(context)
        );
        dialog.setContentView(binding.getRoot());
        binding.editTextGradeName.setText(editTextGradeName.getText());
        // Set Date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binding.editTextBirthday.setText(simpleDateFormat.format(new Date()));


        // Set Action
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
        binding.textInputLayoutBirthday.setEndIconOnClickListener(v -> {
            MaterialDatePicker<Long> dateRangePicker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select BirthDate")
                            .build();
            dateRangePicker.show(requireActivity().getSupportFragmentManager(), "tag");
            dateRangePicker.addOnPositiveButtonClickListener(selection -> {
                Log.d("StudentFragment", selection.toString());
                binding.editTextBirthday.setText(AppUtils.formatTimeStampToDate(selection));
            });
        });

        binding.btnAdd.setOnClickListener(v -> {
            String gradeId = AppUtils.formatGradeName(String.valueOf(binding.editTextGradeName.getText()));
            String firstName = AppUtils.formatPersonName(String.valueOf(binding.editTextFirstName.getText()));
            String lastName = AppUtils.formatPersonName(String.valueOf(binding.editTextLastName.getText()));
            String gender = binding.radioButtonNam.isChecked() ? "Nam" : "Nữ";
            String birthday = binding.editTextBirthday.getText().toString();

            if (firstName.equals("") || lastName.equals("") || birthday.equals("")) {
                if (firstName.equals(""))
                    binding.textInputLayoutFirstName.setError("Họ không được trống.");
                if (lastName.equals(""))
                    binding.textInputLayoutLastName.setError("Tên không được trống.");
                return;
            }
            Student student = new Student(firstName, lastName, gender, birthday, gradeId);

            if(dropdownItems.isEmpty()) {
                AppUtils.showNotificationDialog(context,"Thông báo","Danh sách lớp trống không thể thêm.",null);
                return;
            }
            if(studentViewModel.insertStudent(student)){
                showToast("Thêm học sinh thành công");
                List<Student> students = new ArrayList<>(studentListAdapter.getCurrentList());
                students.add(student);
                studentListAdapter.submitList(students);
                dialog.dismiss();
            }else{
                AppUtils.showNotificationDialog(context,"Thông báo","Thêm học sinh thất bại!",null);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void showToast(String message){
        Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show();
    }


}