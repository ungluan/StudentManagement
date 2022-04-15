package com.example.studentmanagement.feature.SubjectScreen;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SubjectListAdapter extends ListAdapter<Subject, SubjectListAdapter.SubjectViewHolder> {
    SubjectViewModel subjectViewModel;

    interface ListenListener {
        boolean updateSubject(Subject subject);
    }

    protected SubjectListAdapter(SubjectViewModel subjectViewModel, @NonNull DiffUtil.ItemCallback<Subject> diffCallback) {
        super(diffCallback);
        this.subjectViewModel = subjectViewModel;
    }


    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubjectViewHolder(
                parent,
                this.subjectViewModel,
                this
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class SubjectViewHolder extends SwipeViewHolder implements View.OnClickListener {

        private SubjectViewModel subjectViewModel;
        private SubjectListAdapter subjectListAdapter;
        private TextView txtEdit;
        private TextView txtDel;
        private TextView txtSubjectId;
        private TextView txtSubjectName;
        private TextView txtCoefficient;
        private ImageView imgSubject;
        private final int CODE = 101;


        public SubjectViewHolder(ViewGroup parent, SubjectViewModel subjectViewModel, SubjectListAdapter subjectListAdapter) {
            super(parent, R.layout.subject_item, R.layout.item_swipe_left_menu);

            this.subjectViewModel = subjectViewModel;
            this.subjectListAdapter = subjectListAdapter;
            txtSubjectId = findViewById(R.id.txt_subject_id);
            txtSubjectName = findViewById(R.id.txt_subject_name);
            txtCoefficient = findViewById(R.id.txt_subject_coefficient);
            txtEdit = findViewById(R.id.txtEdit);
            txtDel = findViewById(R.id.txtDel);
            imgSubject = findViewById(R.id.imageview_subject_item);

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


            Subject subject = subjectViewModel.getSubjectById(txtSubjectId.getText().toString().split(":")[1].trim());
            if (subject == null) return;
            // get data

            binding.editTextSubjectId.setText(subject.getSubjectId());
            binding.editTextSubjectName.setText(subject.getSubjectName());
            binding.editTextSubjectCoefficient.setText(subject.getCoefficient()+"");
            if(subject.getImage().equals("")){
                binding.imageviewSubjectDialog.setImageResource(R.drawable.art_class2);
            }else {
                try {
                    binding.imageviewSubjectDialog.setImageBitmap(MediaStore.Images.Media.getBitmap(
                            context.getContentResolver(), Uri.parse(subject.getImage())));
//                    Picasso.get().load(subject.getImage()).into(binding.imageviewSubjectDialog);
                } catch (Exception e) {
                }
            }


            binding.btnCancelAddSubject.setOnClickListener(v -> dialog.dismiss());

            binding.btnChooseImageSubject.setOnClickListener(v -> {
                AppUtils.chooseImage(context, binding.imageviewSubjectDialog, CODE);
            });

            binding.btnConfirmAddSubject.setOnClickListener(view -> {


                String name = binding.editTextSubjectName.getText().toString();
                if (name.equals("")) {
                    binding.textInputSubjectName.setError("Subject name cannot be blank");
                    return;
                }
                binding.textInputSubjectName.setErrorEnabled(false);
                int factor;
                try {
                    factor = Integer.parseInt(binding.editTextSubjectCoefficient
                            .getText().toString());
                    binding.textInputSubjectCoefficient.setErrorEnabled(false);
                } catch (Exception e) {
                    binding.textInputSubjectCoefficient.setError("Subject coefficient cannot be blank or not an integer");
                    return;
                }


                subject.setSubjectName(name);
                subject.setCoefficient(factor);
                subject.setImage(AppUtils.getImageString(CODE));
                AppUtils.deleteCode(CODE);
                boolean success = subjectViewModel.updateSubject(subject);
                if (success) {

                    AppUtils.showSuccessDialog(context
                            , "Update subject successfully!");

                    subjectListAdapter.submitList(subjectViewModel.getAllSubject());
                    dialog.dismiss();
                } else {
                    AppUtils.showErrorDialog(context
                                    , "UPDATE ERROR"
                                    , "Update subject failed!");
                        }

                    });

            dialog.show();
        }

        private List<Subject> updateSubjectInList(Subject subject) {
            List<Subject> subjects = new ArrayList(subjectListAdapter.getCurrentList());
            subjects.get(getAdapterPosition()).setSubjectName(subject.getSubjectName());
            subjects.get(getAdapterPosition()).setCoefficient(subject.getCoefficient());
            return subjects;
        }

        private void showToast(String message) {
            Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
        }


        public void bind(Subject item) {
            txtSubjectId.setText(getString(R.string.id_of_the_subject, item.getId()));
            txtSubjectName.setText(getString(R.string.name_of_the_subject, item.getSubjectName()));
            txtCoefficient.setText(getString(R.string.coefficient_of_the_subject, item.getCoefficient()));
            System.out.println("getImageSubject:" + item.getImage());
            if (!item.getImage().equals("")) {
                try {
                    imgSubject.setImageBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(item.getImage())));

                } catch (Exception e) {
                    System.out.println("load image subject:" + e.toString());
                }
            }
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
                     , "Are you sure delete this subject?",
                     new Callable<Void>() {
                         @Override
                         public Void call() throws Exception {
                             boolean check = subjectViewModel.deleteSubject(txtSubjectId.getText().toString().split(":")[1].trim());
                             if(check){
                                 AppUtils.showSuccessDialog(context
                                         , "Delete subject successfully!");
                                 subjectListAdapter.submitList(subjectViewModel.getAllSubject());
                             }else{
                                 AppUtils.showErrorDialog(context
                                         , "DELETE ERROR"
                                         , "Subject have more student!");
                             }
                             return null;
                         }
                     }
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
            return oldItem.equals(newItem);
        }
    }
}
