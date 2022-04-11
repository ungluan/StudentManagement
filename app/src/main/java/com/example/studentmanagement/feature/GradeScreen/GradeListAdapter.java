package com.example.studentmanagement.feature.GradeScreen;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.databinding.DialogAddGradeBinding;
import com.example.studentmanagement.databinding.DialogDelGradeBinding;

import com.example.studentmanagement.utils.AppUtils;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GradeListAdapter extends ListAdapter<Grade, GradeListAdapter.GradeViewHolder> {
    private final GradeViewModel gradeViewModel;

    public GradeListAdapter(GradeViewModel gradeViewModel, @NonNull DiffUtil.ItemCallback<Grade> diffCallback) {
        super(diffCallback);
        this.gradeViewModel = gradeViewModel;
    }

    @NonNull
    @Override
    public List<Grade> getCurrentList() {
        return super.getCurrentList();
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GradeViewHolder(
                parent,
                gradeViewModel,
                this
        );
    }
//
//    public void notiDatasetChange(){
//        this.notifyItemChanged();
//    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class GradeViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private final GradeViewModel gradeViewModel;
        private final GradeListAdapter gradeListAdapter;

        private final TextView txtEdit;
        private final TextView txtDel;
        private final TextView txtGradeName;
        private final TextView txtTeacherInfo;
        private Teacher teacher;
        private final List<String> teacherItems = new ArrayList();

        public GradeViewHolder(
                ViewGroup parent,
                GradeViewModel gradeViewModel,
                GradeListAdapter gradeListAdapter
        ) {
            super(parent, R.layout.grade_item, R.layout.item_swipe_left_menu);
            this.gradeViewModel = gradeViewModel;
            this.gradeListAdapter = gradeListAdapter;
            txtGradeName = findViewById(R.id.txt_grade_name);
            txtTeacherInfo = findViewById(R.id.txt_teacher_info);
            txtEdit = findViewById(R.id.txtEdit);
            txtDel = findViewById(R.id.txtDel);
            txtEdit.setOnClickListener(this);
            txtDel.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Grade grade) {
            teacher = gradeViewModel.getTeacherById(grade.getTeacherId());
            txtGradeName.setText(getString(R.string.name_of_the_grade, grade.getGradeId()));
            txtTeacherInfo.setText(getString(R.string.teacher_of_the_grade,teacher.getTeacherName()));
        }

        public void showToast(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.txtEdit) {
                showEditGradeDialog(getContext());
            } else if (v.getId() == R.id.txtDel) {
                showDelGradeDialog(getContext());
            }
        }

        private void showEditGradeDialog(Context context) {
            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogAddGradeBinding binding = DialogAddGradeBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
            binding.editTextGradeName.setText(
                    String.valueOf(txtGradeName.getText()).split(":")[1].trim());
            binding.editTextGradeName.setEnabled(false);
            // format??
            binding.editTextTeacherId.setText(teacher.getId()+" - "+teacher.getTeacherName());
            binding.btnAdd.setText("Sửa");
            binding.txtTitle.setText("Sửa lớp học");

            initialDropdown(binding);

            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            binding.btnAdd.setOnClickListener(v -> {
            String gradeId = String.valueOf(binding.editTextGradeName.getText());
            // format
            int teacherId = AppUtils.getTeacherIdFromDropDown(binding.editTextTeacherId.getText().toString());

                // Chưa cập nhật Image
                Grade grade = new Grade(gradeId, teacherId,"");
                if (gradeViewModel.updateGrade(grade)) {
                    showToast("Cập nhật lớp thành công!");
                    gradeListAdapter.submitList(updateGradeInList(grade));
                    gradeListAdapter.notifyItemChanged(getAdapterPosition());
                    dialog.dismiss();
                } else {
                    AppUtils.showNotificationDialog(
                            getContext(), "Thông báo", "Cập nhật lớp thất bại!");
                }
            });
            dialog.show();
        }

        private List<Grade> updateGradeInList(Grade grade) {

            List<Grade> grades = new ArrayList(gradeListAdapter.getCurrentList());
            System.out.println(grades);
            grades.get(getAdapterPosition()).setTeacherId(grade.getTeacherId());
            System.out.println(grades);
            return grades;
        }
        private void initialDropdown(DialogAddGradeBinding binding) {
            List<String> teacherList = gradeViewModel.getTeacherHaveNotGrade().stream()
                    .map(item -> item.getId() +" - "+item.getTeacherName())
                    .collect(Collectors.toList());
            teacherItems.clear();
            teacherItems.addAll(teacherList);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                    R.layout.dropdown_item, teacherItems);
            if(teacherItems.size()>0){
//                binding.editTextTeacherId.setText(teacherItems.get(0));
                binding.editTextTeacherId.setAdapter(arrayAdapter);
            }else binding.editTextTeacherId.setText("Danh sách giáo viên trống!");
        }

        private void showDelGradeDialog(Context context) {
            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogDelGradeBinding binding = DialogDelGradeBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
            binding.txtTitle.setText("Thông báo");
            binding.txtContent.setText("Bạn có chắc chắn muốn xóa " + txtGradeName.getText() + " không?");

            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            binding.btnDel.setOnClickListener(v -> {
                String gradeId = String.valueOf(txtGradeName.getText()).split(":")[1].trim();
//                String teacherName = AppUtils.formatPersonName(String.valueOf(txtTeacherName.getText()).split(":")[1].trim());
//                int teacherId = Integer.parseInt();
                Grade grade = new Grade(gradeId, teacher.getId(), "");
                // Kiem tra co hs trong lop khong
                if (gradeViewModel.deleteGrade(grade.getGradeId())) {
                    showToast("Xóa lớp thành công!");
                    List<Grade> grades = new ArrayList<Grade>(gradeListAdapter.getCurrentList());
                    grades.remove(grade);
                    gradeListAdapter.submitList(grades);
                    dialog.dismiss();
                } else {
                    AppUtils.showNotificationDialog(
                            getContext(), "Thông báo", "Xóa lớp thất bại!");
                }
            });
            dialog.show();
        }
    }

    public static class GradeDiff extends DiffUtil.ItemCallback<Grade> {
        @Override
        public boolean areItemsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return oldItem.getGradeId().equals(newItem.getGradeId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return oldItem.equals(newItem);
        }
    }
}
