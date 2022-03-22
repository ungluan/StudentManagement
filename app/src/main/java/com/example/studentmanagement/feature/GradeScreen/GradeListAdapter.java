package com.example.studentmanagement.feature.GradeScreen;

import android.app.Dialog;
import android.content.Context;
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
import com.example.studentmanagement.databinding.GradeItemBinding;
import com.example.studentmanagement.databinding.ItemSwipeLeftMenuBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GradeListAdapter extends ListAdapter<Grade, GradeListAdapter.GradeViewHolder> {
    private GradeViewModel gradeViewModel;

    public GradeListAdapter(GradeViewModel gradeViewModel, @NonNull DiffUtil.ItemCallback<Grade> diffCallback) {
        super(diffCallback);
        this.gradeViewModel = gradeViewModel;
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GradeViewHolder(
                parent,
                GradeItemBinding.inflate(
                        LayoutInflater.from(parent.getContext())
                ),
                ItemSwipeLeftMenuBinding.inflate(
                        LayoutInflater.from(parent.getContext())
                ),
                gradeViewModel
        );
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class GradeViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private GradeViewModel gradeViewModel;

        private TextView txtEdit;
        private TextView txtDel;
        private TextView txtGradeName;
        private TextView txtTeacherName;

        public GradeViewHolder(
                ViewGroup parent,
                GradeItemBinding gradeItemBinding,
                ItemSwipeLeftMenuBinding itemSwipeLeftMenuBinding,
                GradeViewModel gradeViewModel) {
            super(parent, R.layout.grade_item, R.layout.item_swipe_left_menu);
            this.gradeViewModel = gradeViewModel;
            txtGradeName = findViewById(R.id.txt_grade_name);
            txtTeacherName = findViewById(R.id.txt_teacher_name);
            txtEdit = findViewById(R.id.txtEdit);
            txtDel = findViewById(R.id.txtDel);
            txtEdit.setOnClickListener(this);
            txtDel.setOnClickListener(this);
        }

//         public GradeViewHolder(View contentView, @Nullable View swipeLeftMenuView) {
//             super(contentView, R.layout.item_swipe_left_menu);
//         }


        public void bind(Grade grade) {

            txtTeacherName.setText(getString(R.string.teacher_of_the_grade, grade.getTeacherName()));
            txtGradeName.setText(getString(R.string.name_of_the_grade, grade.getGradeId()));

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

            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            binding.btnAdd.setText("Sửa");
            binding.btnAdd.setOnClickListener(v -> {
                String gradeId = String.valueOf(binding.editTextGradeName.getText());
                String teacherName = String.valueOf(binding.editTextTeacherName.getText());

                gradeViewModel.updateGrade(new Grade(gradeId, teacherName))
                        .subscribe(
                                () -> Observable.just("Sửa lớp thành công!").observeOn(AndroidSchedulers.mainThread()).subscribe(
                                        message -> {
                                            Toast.makeText(
                                                    context.getApplicationContext(), message,
                                                    Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                ),
                                throwable -> AppUtils.showNotificationDialog(
                                        context,
                                        "Sửa lớp thất bại",
                                        throwable.getLocalizedMessage()
                                )
                        );

            });
            dialog.show();
        }

        private void showDelGradeDialog(Context context) {
            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogDelGradeBinding binding = DialogDelGradeBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            binding.txtTitle.setText("Thông báo");
            binding.txtContent.setText("Bạn có chắc chắn muốn xóa " + txtGradeName.getText() + " không?");
            binding.btnDel.setOnClickListener(v -> {
                String gradeId = String.valueOf(txtGradeName.getText()).split(":")[1].trim();
                String teacherName = String.valueOf(txtTeacherName.getText()).split(":")[1].trim();
                gradeViewModel.deleteGrade(new Grade(gradeId, teacherName))
                        .subscribe(
                                () -> {
                                    Toast.makeText(
                                            context, "Xóa lớp thành công!",
                                            Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                },

                                throwable -> AppUtils.showNotificationDialog(
                                        context,
                                        "Xóa lớp thất bại",
                                        throwable.getLocalizedMessage()
                                )
                        );

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
            return oldItem.getTeacherName().equals(newItem.getTeacherName());
        }
    }
}
