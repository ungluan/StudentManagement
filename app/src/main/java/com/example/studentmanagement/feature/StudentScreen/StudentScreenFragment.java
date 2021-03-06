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
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.database.entity.relationship.StudentWithMarks;
import com.example.studentmanagement.databinding.DialogAddStudentBinding;
import com.example.studentmanagement.databinding.FragmentStudentScreenBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class StudentScreenFragment extends Fragment {
    private FragmentStudentScreenBinding binding;
    private StudentViewModel studentViewModel;
    private AutoCompleteTextView editTextGradeName;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> dropdownItems = new ArrayList<>();
    private OmegaRecyclerView recyclerView;
    private StudentListAdapter studentListAdapter;
    private TextView txtListEmpty;
//    private ChipGroup chipGroupSubject;
//    private List<Subject> allSubject;

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
        txtListEmpty = binding.txtListEmpty;


        initialStudentScreen();


        editTextGradeName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Clicked", "Position: " + position + " ID: " + id);
                loadRecyclerViewStudent(dropdownItems.get(position));
            }
        });
        binding.fab.setOnClickListener(v -> {
            showAddStudentDialog(requireContext());
//            studentViewModel.insertMark(new Mark(100,"MH001",0.0));

//            Log.d("StudentFragment", "Insert Student");
//            studentViewModel.insertMark(new Mark(12001, "MH001", 10.0)).subscribe(
//                    () -> {
//                        Log.d("StudentFragment", "Successfull Insert Student");
//                    },
//                    throwable -> Log.d("StudentFragment", throwable.getMessage())
//
//            );
//            Log.d("StudentFragment", "Finished Insert Student");

        });
        binding.btnBack.setOnClickListener(v -> {
            NavDirections action = StudentScreenFragmentDirections.actionStudentScreenFragmentToHomeFragment();
            Navigation.findNavController(v).navigate(action);
        });


    }

    private void loadRecyclerViewStudent(String gradeId) {
        studentViewModel.getStudentsByGradeId(gradeId).subscribe(
                students -> studentListAdapter.submitList(students),
                throwable -> Log.d("StudentFragment", throwable.getMessage()),
                () -> {
                    if (studentListAdapter.getCurrentList().size() != 0)
                        txtListEmpty.setVisibility(View.INVISIBLE);
                    else txtListEmpty.setVisibility(View.VISIBLE);
                }
        );
    }

    private void initialDropdown() {
        arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, dropdownItems);
        editTextGradeName.setText(dropdownItems.get(0));
        editTextGradeName.setAdapter(arrayAdapter);
        loadRecyclerViewStudent(dropdownItems.get(0));
    }

    private void initialStudentScreen() {
        studentViewModel.getListGrade().subscribe(
                grades -> Observable.just(grades).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                grades1 -> grades.forEach(grade -> dropdownItems.add(grade.getGradeId())),
                                throwable -> Log.d("StudentFragment", "Error: " + throwable.getMessage()),
                                () -> initialDropdown()
                        ),
                throwable -> Log.d("StudentFragment", "Error: " + throwable.getMessage())
        );
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
        // Set ChipGroup
//        chipGroupSubject = binding.chipGroupSubject;
//        studentViewModel.getListSubject().subscribe(
//                subjects -> {
//                    allSubject = subjects;
//                    studentViewModel.loadChipGroupSubject(subjects).subscribe(
//                            subject -> addChips(subject),
//                            throwable -> Log.d("StudentFragment", "Error: " + throwable.getMessage())
//                    );
//                },
//                throwable -> Log.d("StudentFragment", "Error: " + throwable.getMessage())
//        );

        // Set Action
        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
        binding.textInputLayoutBirthday.setEndIconOnClickListener(v -> {
//            Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();
            MaterialDatePicker<Long> dateRangePicker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select dates")
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
            String gender = binding.radioButtonNam.isChecked() ? "Nam" : "N???";
            String birthday = binding.editTextBirthday.getText().toString();

//            List<Integer> subjectIds = chipGroupSubject.getCheckedChipIds();
//            Log.d("StudentFragment", String.valueOf(chipGroupSubject.getCheckedChipIds()));
//            subjectIds.forEach(id -> {
//                Chip chip = chipGroupSubject.findViewById(id);
//                allSubject.forEach(subject -> {
//                            if (subject.getId() == chip.getTag()) subjectsSelected.add(subject);
//                        }
//                );
//            });

            if (firstName.equals("") || lastName.equals("") || birthday.equals("")) {
                if (firstName.equals(""))
                    binding.textInputLayoutFirstName.setError("H??? kh??ng ???????c tr???ng.");
                if (lastName.equals(""))
                    binding.textInputLayoutLastName.setError("T??n kh??ng ???????c tr???ng.");
                return;
            }
            Student student = new Student(firstName, lastName, gender, birthday, gradeId);
            studentViewModel.insertStudent(student).subscribe(
                    new CompletableObserver() {
                        @Override
                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            Toast.makeText(context, "Th??m h???c sinh th??nh c??ng!",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            Log.d("StudentFragment", "Th??m h???c sinh th???t b???i");
                        }
                    }
            );
        });
        dialog.show();
    }


}