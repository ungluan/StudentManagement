package com.example.studentmanagement.feature.StudentScreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.studentmanagement.MainActivity;
import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.databinding.DialogAddStudentBinding;
import com.example.studentmanagement.databinding.DialogDelGradeBinding;
import com.example.studentmanagement.databinding.DialogUpdateSubjectBinding;
import com.example.studentmanagement.feature.home.HomeViewModel;
import com.example.studentmanagement.utils.AppUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class StudentListAdapter extends ListAdapter<Student, StudentListAdapter.StudentViewHolder> {
    private StudentViewModel studentViewModel;
    private List<Subject> allSubject;
    protected StudentListAdapter(StudentViewModel studentViewModel, @NonNull DiffUtil.ItemCallback<Student> diffCallback) {
        super(diffCallback);
        this.studentViewModel = studentViewModel;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentViewHolder(
                parent,
                R.layout.student_item,
                R.layout.item_swipe_student_menu,
                studentViewModel
        );
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class StudentViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private StudentViewModel studentViewModel;
        private TextView txtName;
        private TextView txtGenre;
        private TextView txtBirthdate;
        private TextView txtEdit;
        private TextView txtDel;
        private TextView txtUpdateSubject;
        private Student student;
        private ChipGroup chipGroupSubjects;
        private List<Subject> subjectSelected = new ArrayList<>();
        public StudentViewHolder(ViewGroup parent, int contentRes, int swipeLeftMenuRes, StudentViewModel studentViewModel) {
            super(parent, contentRes, swipeLeftMenuRes);
            txtName = findViewById(R.id.txt_name);
            txtGenre = findViewById(R.id.txt_genre);
            txtBirthdate = findViewById(R.id.txt_birth_day);
            txtEdit = findViewById(R.id.txtEdit);
            txtDel = findViewById(R.id.txtDel);
            txtUpdateSubject = findViewById(R.id.txtUpdate);
            this.studentViewModel = studentViewModel;
        }

        private void bind(Student student) {
            txtName.setText(student.getFirstName() + " " + student.getLastName());
            txtGenre.setText(student.getGender());
            txtBirthdate.setText(student.getBirthday());
            this.student = student;
            txtEdit.setOnClickListener(this);
            txtDel.setOnClickListener(this);
            txtUpdateSubject.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == txtEdit.getId()) {
                showAddStudentDialog(getContext());
            } else if (v.getId() == txtDel.getId()) {
                showDelStudentDialog(getContext());
            }else if(v.getId()==txtUpdateSubject.getId()){
                showUpdateSubjectDialog(getContext());
            }
        }

        private void showAddStudentDialog(Context context) {
            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
            DialogAddStudentBinding binding = DialogAddStudentBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            binding.editTextGradeName.setText(student.getGradeId());
            // Set title
            binding.txtTitle.setText("Sửa học sinh");
            binding.btnAdd.setText("Sửa");
            // FirstName - LastName - Gender - Birthday
            binding.editTextFirstName.setText(student.getFirstName());
            binding.editTextLastName.setText(student.getLastName());
            if(student.getGender().equals("Nam")) binding.radioButtonNam.setChecked(true);
            else binding.radioButtonNu.setChecked(true);
            binding.editTextBirthday.setText(txtBirthdate.getText());


            // Set Action
            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            binding.textInputLayoutBirthday.setEndIconOnClickListener(v -> {
//            Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();
                MaterialDatePicker<Long> datePicker =
                        MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Select dates")
                                .build();
                FragmentManager fm = ((MainActivity) getContext()).getSupportFragmentManager();
                datePicker.show(fm, "tag");
                datePicker.addOnPositiveButtonClickListener(selection -> {
                    Log.d("StudentFragment", selection.toString());
                    binding.editTextBirthday.setText(AppUtils.formatTimeStampToDate(selection));
                });
            });
            binding.btnAdd.setOnClickListener(v -> {
                String gradeId = String.valueOf(binding.editTextGradeName.getText());
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

                studentViewModel.updateStudent(
                        new Student(student.getId(), firstName, lastName, gender, birthday, gradeId)).subscribe(
                        () -> {
                            Observable.just("Cập nhật học sinh thành công!").observeOn(AndroidSchedulers.mainThread()).subscribe(
                                    s -> Toast.makeText(
                                            context, s,
                                            Toast.LENGTH_SHORT).show(),
                                    throwable -> AppUtils.showNotificationDialog(
                                            context,
                                            "Cập nhật học sinh thất bại",
                                            throwable.getLocalizedMessage()
                                    )
                            );
                            dialog.dismiss();
                        },
                        throwable -> AppUtils.showNotificationDialog(
                                context,
                                "Thêm học sinh thất bại",
                                throwable.getMessage()
                        )
                );
            });
            dialog.show();
        }
        private void showDelStudentDialog(Context context) {
            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogDelGradeBinding binding = DialogDelGradeBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
            binding.txtTitle.setText("Thông báo");
            binding.txtContent.setText("Bạn có chắc chắn muốn xóa " + txtName.getText() + " không?");

            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            binding.btnDel.setOnClickListener(v -> {
                studentViewModel.deleteStudent(student)
                        .subscribe(
                                () -> {
                                    Observable.just("Xóa học sinh thành công!").observeOn(AndroidSchedulers.mainThread()).subscribe(
                                            s -> Toast.makeText(
                                                    context, s,
                                                    Toast.LENGTH_SHORT).show(),
                                            throwable -> AppUtils.showNotificationDialog(
                                                    context,
                                                    "Xóa học sinh thất bại",
                                                    throwable.getLocalizedMessage()
                                            )
                                    );
                                    dialog.dismiss();
                                },
                                throwable -> AppUtils.showNotificationDialog(
                                        context,
                                        "Xóa học sinh thất bại!",
                                        throwable.getLocalizedMessage()
                                )
                        );

            });
            dialog.show();
        }

        private void addChips(Subject subject) {
            Chip chip = (Chip) ((Activity)getContext()).getLayoutInflater().inflate(R.layout.item_chip_subject, null, false);
            chip.setText(subject.getSubjectName());
            chipGroupSubjects.addView(chip);
            chip.setTag(subject.getId());
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                 allSubject.forEach(s -> {
                     if(s.getId()==chip.getTag()){
                         if(isChecked) {
                             subjectSelected.add(s);
                         }else{
                             subjectSelected.remove(s);
                         }
                         Log.d("StudentFragment","SubjectSelected: "+String.valueOf(subjectSelected));
                         return;
                     }
                });
            });
        }
        private void showUpdateSubjectDialog(Context context) {

            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogUpdateSubjectBinding binding = DialogUpdateSubjectBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);

            chipGroupSubjects = binding.chipGroupSubject;

            studentViewModel.getListSubject().subscribe(
                    subjects -> {
                        allSubject = subjects;
                        studentViewModel.loadChipGroupSubject(subjects).subscribe(
                                subject -> addChips(subject),
                                throwable -> Log.d("AddSubjectDialog","Error: "+throwable.getMessage())
                        );
                    },
                    throwable -> Log.d("AddSubjectDialog","Error: "+throwable.getMessage())
            );
            // Xử lý click chip -> Add vào subjectSelected

            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            binding.btnAdd.setOnClickListener(v -> {
                if(subjectSelected.size()==0){
                    binding.txtError.setVisibility(View.VISIBLE);
                }else{
                    binding.txtError.setVisibility(View.INVISIBLE);
                    Observable.fromIterable(subjectSelected)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    new Observer<Subject>() {
                                        @Override
                                        public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                                            Log.d("StudentFragment","onSubscribe");
                                        }

                                        @Override
                                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull Subject subject) {
                                            Log.d("StudentFragment","onNext");
                                            studentViewModel.insertMark(new Mark(student.getId(),subject.getId(),0.0)).subscribe();
                                        }

                                        @Override
                                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                            Log.d("StudentFragment","onError: "+e.getMessage());
                                        }

                                        @Override
                                        public void onComplete() {
                                            Log.d("StudentFragment","onComplete");
                                            Toast.makeText(context,"Add Subject Successful",Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            subjectSelected.removeAll(subjectSelected);
                                        }
                                    }
                            );
                }
            });
            dialog.show();
        }
    }


    public static class StudentDiff extends DiffUtil.ItemCallback<Student> {

        @Override
        public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
            return oldItem.equals(newItem);
        }
    }

}
