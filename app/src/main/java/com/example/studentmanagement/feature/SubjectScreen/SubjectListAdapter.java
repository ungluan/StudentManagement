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
import com.example.studentmanagement.database.entity.Subject;

import com.example.studentmanagement.databinding.DialogAddSubjectBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class SubjectListAdapter extends ListAdapter<Subject, SubjectListAdapter.SubjectViewHolder> {
    SubjectViewModel subjectViewModel;

    protected SubjectListAdapter(SubjectViewModel subjectViewModel, @NonNull DiffUtil.ItemCallback<Subject> diffCallback) {
        super(diffCallback);
        this.subjectViewModel = subjectViewModel;
    }



    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubjectViewHolder(
                parent,
                R.layout.subject_item,
                R.layout.item_swipe_left_menu,
                subjectViewModel
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class SubjectViewHolder extends SwipeViewHolder implements View.OnClickListener {

        SubjectViewModel subjectViewModel;
        private TextView txtEdit;
        private TextView txtDel;
        private TextView txtSubjectId;
        private TextView txtSubjectName;
        private TextView txtCoefficient;

        public SubjectViewHolder(ViewGroup parent, int layout, int swipe, SubjectViewModel subjectViewModel) {
            super(parent, layout, swipe);

            this.subjectViewModel = subjectViewModel;
            txtSubjectId = findViewById(R.id.txt_subject_id);
            txtSubjectName = findViewById(R.id.txt_subject_name);
            txtCoefficient = findViewById(R.id.txt_subject_coefficient);
            txtEdit = findViewById(R.id.txtEdit);
            txtDel = findViewById(R.id.txtDel);

            txtEdit.setOnClickListener(this);
            txtDel.setOnClickListener(this);

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

                        boolean success = subjectViewModel.update(new Subject(id, name, factor));
                        if(success){
                            AppUtils.showNotificationDialog(context
                                    , "UPDATE"
                                    , "Update subject successfully!");
                        }else{
                            AppUtils.showNotificationDialog(context
                                    , "UPDATE"
                                    , "Update subject failed!");
                        }

                    });

            dialog.show();
        }

        private void showToast(String message) {
            Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
        }


        public void bind(Subject item) {
            txtSubjectId.setText(getString(R.string.id_of_the_subject, item.getId()));
            txtSubjectName.setText(getString(R.string.name_of_the_subject, item.getSubjectName()));
            txtCoefficient.setText(getString(R.string.coefficient_of_the_subject, item.getCoefficient()));
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == txtEdit.getId()) {
                showEditSubjectDialog(getContext());
            } else if (v.getId() == txtDel.getId()) {
                String subjectId = txtSubjectId.getText().toString();
                showDelSubjectDialog(getContext(), subjectId);
            }
        }

        private void showDelSubjectDialog(Context context, String subjectId) {
             AppUtils.showNotiDialog(
                    context
                    , "Are you sure delete this subject?"
            );
//            if(del){
//                boolean checkFK= subjectViewModel.delete(subjectId);
//                if(checkFK){
//                    AppUtils.showSuccessDialog(context, "Delete successfully!");
//
//                }
//            }
        }
    }

    public static class SubjectDiff extends  DiffUtil.ItemCallback<Subject>{

        @Override
        public boolean areItemsTheSame(@NonNull Subject oldItem, @NonNull Subject newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Subject oldItem, @NonNull Subject newItem) {
            return oldItem.getSubjectName().equals(newItem.getSubjectName())
                    && oldItem.getCoefficient()==(newItem.getCoefficient());
        }
    }
}
