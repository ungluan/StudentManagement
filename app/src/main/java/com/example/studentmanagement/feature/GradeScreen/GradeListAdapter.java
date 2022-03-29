package com.example.studentmanagement.feature.GradeScreen;

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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.databinding.DialogAddGradeBinding;
import com.example.studentmanagement.databinding.DialogDelGradeBinding;

import com.example.studentmanagement.utils.AppUtils;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import kotlin.Unit;


public class GradeListAdapter extends ListAdapter<Grade, GradeListAdapter.GradeViewHolder> {
    private GradeViewModel gradeViewModel;


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
        private GradeViewModel gradeViewModel;
        private GradeListAdapter gradeListAdapter;

        private TextView txtEdit;
        private TextView txtDel;
        private TextView txtGradeName;
        private TextView txtTeacherName;


        public GradeViewHolder(
                ViewGroup parent,
                GradeViewModel gradeViewModel,
                GradeListAdapter gradeListAdapter
        ) {
            super(parent, R.layout.grade_item, R.layout.item_swipe_left_menu);
            this.gradeViewModel = gradeViewModel;
            this.gradeListAdapter = gradeListAdapter;
            txtGradeName = findViewById(R.id.txt_grade_name);
            txtTeacherName = findViewById(R.id.txt_teacher_name);
            txtEdit = findViewById(R.id.txtEdit);
            txtDel = findViewById(R.id.txtDel);
            txtEdit.setOnClickListener(this);
            txtDel.setOnClickListener(this);
        }

        public void bind(Grade grade) {
            txtTeacherName.setText(getString(R.string.teacher_of_the_grade, grade.getTeacherName()));
            txtGradeName.setText(getString(R.string.name_of_the_grade, grade.getGradeId()));
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
            binding.editTextGradeName.setText(String.valueOf(txtGradeName.getText()).split(":")[1].trim());
            binding.editTextGradeName.setEnabled(false);
            binding.editTextTeacherName.setText(String.valueOf(txtTeacherName.getText()).split(":")[1].trim());
            binding.btnAdd.setText("Sửa");
            binding.txtTitle.setText("Sửa lớp học");

            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            binding.btnAdd.setOnClickListener(v -> {
                String gradeId = String.valueOf(binding.editTextGradeName.getText());
                String teacherName = AppUtils.formatGradeName(String.valueOf(binding.editTextTeacherName.getText()));


                Grade grade = new Grade(gradeId, teacherName);
                if (gradeViewModel.updateGrade(grade)) {
                    showToast("Cập nhật lớp thành công!");

                    List<Grade> grades = updateGradeInList(grade);
                    gradeListAdapter.submitList(grades);

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
            grades.get(getAdapterPosition()).setTeacherName(grade.getTeacherName());
            return grades;
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
                String teacherName = AppUtils.formatPersonName(String.valueOf(txtTeacherName.getText()).split(":")[1].trim());

                Grade grade = new Grade(gradeId, teacherName);
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
