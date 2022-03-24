package com.example.studentmanagement.feature.SubjectScreen;


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
import com.example.studentmanagement.database.entity.Subject;

import com.example.studentmanagement.databinding.DialogAddSubjectBinding;
import com.example.studentmanagement.databinding.DialogDelGradeBinding;
import com.example.studentmanagement.databinding.ItemSwipeLeftMenuBinding;
import com.example.studentmanagement.databinding.SubjectItemBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;


import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;


public class SubjectListAdapter extends ListAdapter<Subject, SubjectListAdapter.SubjectViewHolder> {

    private SubjectViewModel subjectViewModel;
    protected SubjectListAdapter(SubjectViewModel subjectViewModel, @NonNull DiffUtil.ItemCallback<Subject> diffCallback) {
        super(diffCallback);
        this.subjectViewModel = subjectViewModel;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new SubjectViewHolder(
                parent,
                subjectViewModel

        );
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class SubjectViewHolder extends SwipeViewHolder implements View.OnClickListener {

        private SubjectViewModel subjectViewModel;
        private TextView txtEdit;
        private TextView txtDel;
        private TextView txtSubjectId;
        private TextView txtSubjectName;
        private TextView txtCoefficient;

        public SubjectViewHolder(ViewGroup parent,
                                 SubjectViewModel subjectViewModel) {
            super(parent, R.layout.subject_item, R.layout.item_swipe_left_menu);
            this.subjectViewModel = subjectViewModel;
            txtSubjectId = findViewById(R.id.txt_subject_id);
            txtSubjectName = findViewById(R.id.txt_subject_name);
            txtCoefficient = findViewById(R.id.txt_subject_coefficient);
            txtEdit = findViewById(R.id.txtEdit);
            txtDel = findViewById(R.id.txtDel);

            txtEdit.setOnClickListener(this);
            txtDel.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.txtEdit) {
//                txtEdit.setText("Sửa");
                showEditSubjectDialog(getContext());
            } else if (view.getId() == R.id.txtDel) {
                showDelSubjectDialog(getContext());
            }

        }

        private void showDelSubjectDialog(Context context) {
            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogDelGradeBinding binding = DialogDelGradeBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
            String name = txtSubjectName.getText().toString().split(":")[1].trim();
            binding.txtTitle.setText("Thông báo");
            binding.txtContent.setText("Bạn có chắc chắn muốn xóa môn học " +
                    name + " không?");

            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            binding.btnDel.setOnClickListener(v -> {
                String id = txtSubjectId.getText().toString().split(":")[1].trim();
                int factor = Integer.parseInt(txtCoefficient.getText().toString().split(":")[1].trim());

                subjectViewModel.deleteSubject(new Subject(id, name, factor))
                        .subscribe(
                                () -> {
                                    Toast.makeText(
                                            context, "Xóa lớp thành công!",
                                            Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                },

                                throwable -> {AppUtils.showNotificationDialog(
                                        context,
                                        "Xóa lớp thất bại",
                                        throwable.getLocalizedMessage());
                                        dialog.dismiss();
                                }

                        );

            });
            dialog.show();
        }


        private void showEditSubjectDialog(Context context) {
            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogAddSubjectBinding binding = DialogAddSubjectBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);

            binding.dialogTitleAddSubject.setText("Cập nhật môn học");
            binding.btnConfirmAddSubject.setText("Cập nhật");
            binding.editTextSubjectId.setEnabled(false);

            // get data

            binding.editTextSubjectId.setText(
                    txtSubjectId.getText().toString().split(":")[1].trim());
            binding.editTextSubjectName.setText(
                    txtSubjectName.getText().toString().split(":")[1].trim());
            binding.editTextSubjectCoefficient.setText(
                    txtCoefficient.getText().toString().split(":")[1].trim());

            binding.btnCancelAddSubject.setOnClickListener(v -> dialog.dismiss());
            binding.btnConfirmAddSubject.setOnClickListener(view -> {
                        String id = binding.editTextSubjectId.getText().toString();
                        String name = binding.editTextSubjectName.getText().toString();
                        int factor = Integer.parseInt(binding.editTextSubjectCoefficient
                                .getText().toString());
                subjectViewModel.updateSubject(new Subject(id, name, factor))
                        .subscribe(
                                () -> Observable.just("Sửa môn học thành công!").observeOn(AndroidSchedulers.mainThread()).subscribe(
                                        message -> {
                                            Toast.makeText(
                                                    context, message,
                                                    Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                ),
                                throwable -> AppUtils.showNotificationDialog(
                                        context,
                                        "Sửa môn học thất bại!",
                                        throwable.getLocalizedMessage()
                                )
                        );


                    }
            );
            dialog.show();
        }

        private void showToast(String message) {
            Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
        }


        public void bind(Subject item) {
            txtSubjectId.setText(getString(R.string.id_of_the_subject, item.getSubjectId()));
            txtSubjectName.setText(getString(R.string.name_of_the_subject, item.getSubjectName()));
            txtCoefficient.setText(getString(R.string.coefficient_of_the_subject, item.getCoefficient()));
        }
    }

    public static class SubjectDiff extends DiffUtil.ItemCallback<Subject> {

        @Override
        public boolean areItemsTheSame(@NonNull Subject oldItem, @NonNull Subject newItem) {

            return oldItem.getSubjectId() == newItem.getSubjectId();

        }

        @Override
        public boolean areContentsTheSame(@NonNull Subject oldItem, @NonNull Subject newItem) {
            return oldItem.getSubjectName().equals(newItem.getSubjectName())
                    && oldItem.getCoefficient() == (newItem.getCoefficient());
        }
    }
}
