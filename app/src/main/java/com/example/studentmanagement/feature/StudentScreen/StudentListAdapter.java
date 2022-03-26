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
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.databinding.DialogAddStudentBinding;
import com.example.studentmanagement.databinding.DialogDelGradeBinding;
import com.example.studentmanagement.databinding.DialogUpdateSubjectBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.CompletableSource;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
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
        private List<Subject> subjectsSelected;
        private List<Subject> listSubjectRemove = new ArrayList<>();
        private List<Subject> saveSubjectSelected = new ArrayList<>();

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
            } else if (v.getId() == txtUpdateSubject.getId()) {
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
            if (student.getGender().equals("Nam")) binding.radioButtonNam.setChecked(true);
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

        private void addChips(@NonNull Subject subject) {
            Chip chip = (Chip) ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_chip_subject, null, false);
            chip.setText(subject.getSubjectName());
            chipGroupSubjects.addView(chip);
            subjectsSelected.forEach(sub -> {
                if (sub.getId().equals(subject.getId())) chip.setChecked(true);
            });
            chip.setTag(subject.getId());
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Log.d("StudentFragment", "Tag: " + buttonView.getTag());
                Log.d("StudentFragment", "IsChecked: " + isChecked);
                if (isChecked)
                    allSubject.forEach(s -> {
                        if (s.getId() == buttonView.getTag()) {
                            subjectsSelected.add(s);
                        }
                    });
                else
                    subjectsSelected.removeIf(sub -> sub.getId().equals(buttonView.getTag()));
                Log.d("StudentFragment", "SubjectSelected: " + String.valueOf(subjectsSelected));
                Log.d("StudentFragment", "SavedSubjectSelected: " + String.valueOf(saveSubjectSelected));


            });
        }

        private void reloadChip() {
            for (int i = 0; i < chipGroupSubjects.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupSubjects.getChildAt(i);

                saveSubjectSelected.forEach(subject -> {
                    if (subject.getId().equals(chip.getTag())) {
                        chip.setChecked(true);
                    }
                });
            }
        }

        private void showUpdateSubjectDialog(Context context) {

            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogUpdateSubjectBinding binding = DialogUpdateSubjectBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
            binding.btnAdd.setText("Cập nhật");

            dialog.setOnDismissListener(dialog1 -> {
                subjectsSelected.clear();
                listSubjectRemove.clear();
                Log.d("StudentFragment", "Cleared SubjectSelected");
            });

            studentViewModel.getSubjectsByStudentId(student.getId()).subscribe(
                    subjects -> {
                        subjectsSelected = subjects;
                        saveSubjectSelected.addAll(subjects);
                    }
            );


            chipGroupSubjects = binding.chipGroupSubject;

            studentViewModel.getListSubject().subscribe(
                    subjects -> {
                        allSubject = subjects;
                        studentViewModel.loadChipGroupSubject(subjects)
                                .subscribe(
                                        // Chỉ khi nào add hết các Chip rồi thì mới cập nhật UI
                                        // ==> Ý tưởng Cho Load màn hình cần nhiều Query
                                        subject -> addChips(subject),
                                        throwable -> Log.d("AddSubjectDialog", "Error: " + throwable.getMessage()),
                                        () -> binding.linearLayoutCenter.setVisibility(View.INVISIBLE)

                                );
                    },
                    throwable -> Log.d("AddSubjectDialog", "Error: " + throwable.getMessage())
            );
            // Xử lý click chip -> Add vào subjectSelected

            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            binding.btnAdd.setOnClickListener(v -> {
                if (subjectsSelected.size() == 0) {
                    binding.txtError.setVisibility(View.VISIBLE);
                } else {
                    binding.txtError.setVisibility(View.INVISIBLE);
                    // Todo Bugs:
                    /***
                     *  Các Bug còn sai:
                     *  1. Có môn học 0.0đ ở ds cũ -> Khi bỏ tích -> Cập nhật -> Môn vẫn chưa đc xóa
                     *  dù đã đạt yêu cầu. -> Done
                     *  2. Hiển thị thông báo môn học không thể hủy sai. -> Done
                     *  3. Vẫn chưa check điều kiện xóa học sinh.
                     *  4. Vẫn chưa check điều kiện xóa môn học, lớp học.
                     *  5. Vận dụng ý tưởng của Dialog khi loading Chip -> Triển khai cho các màn hình còn lại
                     *  6. Custom layout cho Dialog và Button , progressbar
                     *  7. Vẫn chưa thực hiện thống kê
                     * ***/
                    studentViewModel.getSubjectAndMarkByStudentId(student.getId()).subscribe(
                            // Subjects: danh sách môn học đã chọn của học sinh này
                            // Kiểm tra danh sách này với danh sách mới
                            // Nếu không có trong danh sách mới thì phải kiểm tra điểm
                            subjectWithMarks -> {
                                studentViewModel.checkSubjectsSelected(subjectWithMarks)
                                        .subscribe(
                                                subjectAndMark -> {
                                                    // Môn học có chứa trong ds mới -> Không cần phải insert nữa
                                                    if (subjectsSelected.contains(subjectAndMark.subject)) {
                                                        subjectsSelected.remove(subjectAndMark.subject);
                                                        Log.d("StudentFragment", "Remove Subject Selected: " + subjectsSelected);
                                                    }
                                                    // Môn học không còn trong ds mới
                                                    else {
                                                        // Điểm ==0 -> Có thể hủy bỏ môn học
                                                        // Thêm vào danh sách hủy bỏ môn học
                                                        // TH1: Nếu có môn học đã có điểm thì show dialog
                                                        // TH2: Tiến hành Hủy bỏ môn học trong SubjectSelected
                                                        // và Thêm từng môn học trong SubjectSelected
                                                        if (subjectAndMark.marks.get(0).getScore() == 0.0)
                                                            listSubjectRemove.add(subjectAndMark.subject);
                                                        else {
                                                            subjectsSelected.clear();
                                                            subjectsSelected.addAll(saveSubjectSelected);
                                                            throw new Exception("Học sinh đã có điểm môn "
                                                                    + subjectAndMark.subject.getSubjectName() +
                                                                    ".Không thể bỏ chọn môn học này.");
                                                        }
                                                    }
                                                },
                                                throwable -> Observable.just(String.valueOf(throwable.getMessage()))
                                                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                                                s -> {
                                                                    AppUtils.showNotificationDialog(context, "Thông báo", s);
                                                                    reloadChip();
                                                                }
                                                        ),
                                                () -> {
                                                    studentViewModel.deleteListMark(convertSubjectToMark(listSubjectRemove))
                                                            .andThen(
                                                                    (CompletableSource) observer ->
                                                                    {
                                                                        Log.d("StudentFragment", "andThen and ThreadName: " + Thread.currentThread());
                                                                        studentViewModel.insertListMark(convertSubjectToMark(subjectsSelected)).subscribe();
                                                                        Log.d("StudentFragment", "Finished andThen and ThreadName: " + Thread.currentThread());
                                                                        Observable.empty().observeOn(AndroidSchedulers.mainThread())
                                                                                .doOnComplete(() -> {
                                                                                    dialog.dismiss();
                                                                                    Toast.makeText(context, "Cập nhật môn học thành công!", Toast.LENGTH_SHORT).show();
                                                                                })
                                                                                .doOnError(throwable -> Log.d("StudentFragment", "DismissDialog Error: " + throwable.getMessage()))
                                                                                .subscribe();
                                                                    }
                                                            ).subscribe();
//                                                    Log.d("StudentFragment", "Begin Remove SubjectSelected ");
//                                                    subjectsSelected.removeAll(listSubjectRemove);
//                                                    Log.d("StudentFragment", "Finished Remove SubjectSelected: " + subjectsSelected);
//                                                    studentViewModel.addListSubject(subjectsSelected).subscribe(
//                                                            subject -> {
//                                                                Log.d("StudentFragment", "Thread.Name before insertMark: " + Thread.currentThread().getName());
//                                                                studentViewModel.insertMark(new Mark(student.getId(), subject.getId(), 0.0)).subscribe();
//                                                            },
//                                                            throwable -> {
//                                                                Log.d("StudentFragment", "Cập nhật môn học thất bại!");
//                                                                dialog.dismiss();
//                                                            },
//                                                            () -> {
//                                                                Log.d("StudentFragment", "Thread.Name: " + Thread.currentThread().getName());
//                                                                Observable.just("Cập nhật môn học thành công")
//                                                                        .observeOn(AndroidSchedulers.mainThread()).subscribe(
//                                                                        s -> {
//                                                                            Log.d("StudentFragment", "Thread.Name (onNext): " + Thread.currentThread().getName());
//                                                                            Log.d("StudentFragment", s);
//
//                                                                        },
//                                                                        throwable -> Log.d("StudentFragment", "Cập nhật môn học thất bại! " + throwable.getMessage()),
//                                                                        () -> {
//                                                                            Log.d("StudentFragment", "Thread.Name: " + Thread.currentThread().getName());
//                                                                            Toast.makeText(context, "Cập nhật môn học thành công", Toast.LENGTH_SHORT).show();
//                                                                        }
//                                                                );
//                                                                dialog.dismiss();
////                                                                subjectsSelected.removeAll(subjectsSelected);
//                                                            }
//                                                    );
                                                }
                                        );
                            }
                    );
                }
            });
            dialog.show();
        }

        private List<Mark> convertSubjectToMark(List<Subject> list) {
            List<Mark> l = new ArrayList<>();
            list.forEach(v -> {
                l.add(new Mark(student.getId(), v.getId(), 0.0));
            });
            return l;
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
