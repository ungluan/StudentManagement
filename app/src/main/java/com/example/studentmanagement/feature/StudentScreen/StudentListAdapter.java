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
import com.example.studentmanagement.database_sqlite.DataBaseHelper;
import com.example.studentmanagement.databinding.DialogAddStudentBinding;
import com.example.studentmanagement.databinding.DialogDelGradeBinding;
import com.example.studentmanagement.databinding.DialogUpdateSubjectBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;


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
                studentViewModel,
                this
        );
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class StudentViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private StudentViewModel studentViewModel;
        private StudentListAdapter studentListAdapter;
        private TextView txtName;
        private TextView txtGenre;
        private TextView txtBirthdate;
        private TextView txtEdit;
        private TextView txtDel;
        private TextView txtUpdateSubject;
        private Student student;
        private ChipGroup chipGroupSubjects;
        private List<Subject> listSubjects;
        private List<Subject> listSelected = new ArrayList<>();
        private List<Subject> saveList = new ArrayList<>();
        private List<Subject> delList = new ArrayList<>();
        private Map<String,Double> maps = new HashMap<>();

        public StudentViewHolder(ViewGroup parent, int contentRes, int swipeLeftMenuRes,
                                 StudentViewModel studentViewModel, StudentListAdapter studentListAdapter) {
            super(parent, contentRes, swipeLeftMenuRes);
            txtName = findViewById(R.id.txt_name);
            txtGenre = findViewById(R.id.txt_genre);
            txtBirthdate = findViewById(R.id.txt_birth_day);
            txtEdit = findViewById(R.id.txtEdit);
            txtDel = findViewById(R.id.txtDel);
            txtUpdateSubject = findViewById(R.id.txtUpdate);
            this.studentViewModel = studentViewModel;
            this.studentListAdapter = studentListAdapter;
        }

        private void bind(Student student) {
            txtName.setText(student.getFirstName() + " " + student.getLastName());
            txtGenre.setText(student.getGender());
            txtBirthdate.setText(student.getBirthday());
            this.student = student;
            txtEdit.setOnClickListener(this);
            txtDel.setOnClickListener(this);
            txtUpdateSubject.setOnClickListener(this);

            // Khoi tao
            DataBaseHelper.databaseExecutor.execute(() -> {
                listSubjects = studentViewModel.getSubjects();

            });
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == txtEdit.getId()) {
                showEditStudentDialog(getContext());
            } else if (v.getId() == txtDel.getId()) {
                showDelStudentDialog(getContext());
            } else if (v.getId() == txtUpdateSubject.getId()) {
                showUpdateSubjectDialog(getContext());
            }
        }

        private void showEditStudentDialog(Context context) {

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
                int studentId = this.student.getId() ;
                String gradeId = String.valueOf(binding.editTextGradeName.getText());
                String firstName = AppUtils.formatPersonName(String.valueOf(binding.editTextFirstName.getText()));
                String lastName = AppUtils.formatPersonName(String.valueOf(binding.editTextLastName.getText()));
                String gender = binding.radioButtonNam.isChecked() ? "Nam" : "Nữ";
                String birthday = binding.editTextBirthday.getText().toString();

                Student newStudent = new Student(studentId,firstName,lastName,gender,birthday,gradeId);

                if (firstName.equals("") || lastName.equals("") || birthday.equals("")) {
                    if (firstName.equals(""))
                        binding.textInputLayoutFirstName.setError("Họ không được trống.");
                    if (lastName.equals(""))
                        binding.textInputLayoutLastName.setError("Tên không được trống.");
                }

                if(studentViewModel.updateStudent(newStudent)){
                    showToast("Cập nhật học sinh thành công!");
                    studentListAdapter.submitList(updateStudentInList(newStudent));
                    studentListAdapter.notifyItemChanged(getAdapterPosition());
                    dialog.dismiss();
                }else{
                    AppUtils.showNotificationDialog(
                            getContext(), "Thông báo", "Cập nhật học sinh thất bại!");
                }

            });
            dialog.show();
        }
        private List<Student> updateStudentInList(Student student) {
            List<Student> students = new ArrayList(studentListAdapter.getCurrentList());
            students.get(getAdapterPosition()).setStudent(student);
            return students;
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
            // Chua check hoc sinh co diem
            binding.btnDel.setOnClickListener(v -> {
                if (studentViewModel.deleteStudent(student.getId())) {
                    showToast("Xóa học sinh thành công!");
                    List<Student> students = new ArrayList<>(studentListAdapter.getCurrentList());
                    students.remove(student);
                    studentListAdapter.submitList(students);
                    dialog.dismiss();
                } else {
                    AppUtils.showNotificationDialog(
                            getContext(), "Thông báo", "Xóa lớp thất bại!");
                }
            });
            dialog.show();
        }
        private void showUpdateSubjectDialog(Context context) {


            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogUpdateSubjectBinding binding = DialogUpdateSubjectBinding.inflate(
                    LayoutInflater.from(context)
            );
            binding.linearLayoutCenter.setVisibility(View.VISIBLE);
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
            binding.btnAdd.setText("Cập nhật");

            chipGroupSubjects = binding.chipGroupSubject;

            listSelected = studentViewModel.getSubjectSubjectId(student.getId());
            maps = studentViewModel.getSubjectsSelectedByStudentId(student.getId());

            // Load chip
            for(int i=0 ; i<listSubjects.size() ; i++){
                addChips(listSubjects.get(i));
            }
            binding.linearLayoutCenter.setVisibility(View.INVISIBLE);

            dialog.setOnDismissListener(d -> {
                saveList.clear();
                delList.clear();
                listSelected.clear();
            });

            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            binding.btnAdd.setOnClickListener(v -> {

                if (listSelected.size() == 0) {
                    binding.txtError.setVisibility(View.VISIBLE);
                } else {
                    binding.txtError.setVisibility(View.INVISIBLE);

                    //viewModel.deleteAndInsertMark


                }
            });
            dialog.show();
        }

        private void addChips(@NonNull Subject subject) {
            Chip chip = (Chip) ((Activity) getContext()).getLayoutInflater()
                    .inflate(R.layout.item_chip_subject, null, false);
            chip.setText(subject.getSubjectName());
            chip.setTag(subject.getId());

            chipGroupSubjects.addView(chip);
            // Nếu subject này có trong danh sách môn học đã chọn
            double mark = maps.get(subject.getId())==null ? -1 : maps.get(subject.getId()) ;
            if(mark != -1){
                chip.setChecked(true);
                if( mark != 0){
                    chip.setEnabled(false);
                }else{
                    saveList.add(subject);
                }
            }
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked){
                    delList.add(subject);
                    saveList.remove(subject);
                }else{
                    delList.remove(subject);
                    saveList.add(subject);
                }
            });
        }

        private void showToast(String message){
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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