package com.example.studentmanagement.feature.teacher;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.databinding.DialogAddTeacherBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import java.util.concurrent.Callable;

public class TeacherListAdapter extends ListAdapter<Teacher, TeacherListAdapter.TeacherViewHolder> {
    TeacherViewModel teacherViewModel;

    interface ListenListener {
        boolean updateTeacher(Teacher teacher);
    }

    protected TeacherListAdapter(TeacherViewModel teacherViewModel, @NonNull DiffUtil.ItemCallback<Teacher> diffCallback) {
        super(diffCallback);
        this.teacherViewModel = teacherViewModel;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeacherViewHolder(
                parent,
                this.teacherViewModel,
                this
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class TeacherViewHolder extends SwipeViewHolder implements View.OnClickListener {

        private TeacherViewModel teacherViewModel;
        private TeacherListAdapter teacherListAdapter;
        private TextView txtEdit;
        private TextView txtDel;
        private TextView txtTeacherId;
        private TextView txtTeacherName;
        private TextView txtTeacherPhone;
        private TextView txtTeacherAccount;
        private ImageView imgTeacher;
        private final int CODE = 999;


        public TeacherViewHolder(ViewGroup parent, TeacherViewModel teacherViewModel, TeacherListAdapter teacherListAdapter) {
            super(parent, R.layout.teacher_item, R.layout.item_swipe_left_menu);

            this.teacherViewModel = teacherViewModel;
            this.teacherListAdapter = teacherListAdapter;
            txtTeacherId = findViewById(R.id.txt_teacher_id);
            txtTeacherName = findViewById(R.id.txt_teacher_name);
            txtTeacherPhone = findViewById(R.id.txt_teacher_phone);
            txtTeacherAccount = findViewById(R.id.txt_teacher_account);
            txtEdit = findViewById(R.id.txtEdit);
            txtDel = findViewById(R.id.txtDel);
            imgTeacher = findViewById(R.id.imageview_teacher_item);

            txtEdit.setOnClickListener(this);
            txtDel.setOnClickListener(this);

        }

        private void showEditTeacherDialog(Context context) {
            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogAddTeacherBinding binding = DialogAddTeacherBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);

            binding.dialogTitleAddTeacher.setText("Cập nhật giáo viên");
            binding.btnConfirmAddTeacher.setText("Cập nhật");
//            binding.editTextTeacherId.setEnabled(false);

            int id = 0;
            try {
                id = Integer.parseInt(txtTeacherId.getText().toString().split(":")[1].trim());
            } catch (Exception e) {
                Log.e("parse:", e.getMessage());

            }
            Teacher teacher = teacherViewModel.getTeacherById(id);
            if (teacher == null) return;
            // binding data to dialog update

            binding.editTextTeacherId.setText(teacher.getId() + "");
            binding.editTextTeacherName.setText(teacher.getTeacherName());
            binding.editTextTeacherPhone.setText(teacher.getPhone());
            binding.editTextTeacherAccount.setText(teacher.getIdAccount() + "");
            if (teacher.getImageUrl().equals("")) {
                binding.imageviewTeacherDialog.setImageResource(R.drawable.art_class2);
            } else {
                try {
                    binding.imageviewTeacherDialog.setImageBitmap(MediaStore.Images.Media.getBitmap(
                            context.getContentResolver(), Uri.parse(teacher.getImageUrl())));
//                    Picasso.get().load(teacher.getImage()).into(binding.imageviewTeacherDialog);
                } catch (Exception e) {
                    Log.e("parse", e.getMessage());
                }
            }


            binding.btnCancelAddTeacher.setOnClickListener(v -> dialog.dismiss());

            binding.btnChooseImageTeacher.setOnClickListener(v -> {
                AppUtils.chooseImage(context, binding.imageviewTeacherDialog, CODE);

            });

            binding.btnConfirmAddTeacher.setOnClickListener(view -> {
                String name = binding.editTextTeacherName.getText().toString();
                if (name.equals("")) {
                    binding.textInputTeacherName.setError("Teacher name cannot be blank");
                    return;
                }else binding.textInputTeacherName.setErrorEnabled(false);

                String phone = binding.editTextTeacherPhone.getText().toString();;
                if(name.equals("")){
                    binding.textInputTeacherName.setError("Teacher phone cannot be blank");
                    return;
                }binding.textInputTeacherName.setErrorEnabled(false);


                teacher.setTeacherName(name);
                teacher.setPhone(phone);
                teacher.setImageUrl(AppUtils.getImageString(CODE));

                AppUtils.deleteCode(CODE);// xóa khỏi map image trong utils
                boolean success = teacherViewModel.updateTeacher2(teacher);
                if (success) {

                    AppUtils.showSuccessDialog(context
                            , "Update teacher successfully!");

                    teacherListAdapter.submitList(teacherViewModel.getAllTeacher());
                    dialog.dismiss();
                } else {
                    AppUtils.showErrorDialog(context
                            , "UPDATE ERROR"
                            , "Update teacher failed!");
                }

            });

            dialog.show();
        }





        public void bind(Teacher item) {
            txtTeacherId.setText(getString(R.string.id_of_the_teacher, item.getId()));
            txtTeacherName.setText(getString(R.string.name_of_the_teacher, item.getTeacherName()));
            txtTeacherPhone.setText(getString(R.string.phone_of_the_teacher, item.getPhone()));
            txtTeacherAccount.setText(getString(R.string.account_of_the_teacher, item.getIdAccount()==0?"Chưa đăng ký":item.getIdAccount()+""));
            if (!item.getImageUrl().equals("")) {
                try {
                    imgTeacher.setImageBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(item.getImageUrl())));
                } catch (Exception e) {
                    System.out.println("load image teacher:" + e.toString());
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == txtEdit.getId()) {
                showEditTeacherDialog(getContext());
            } else if (v.getId() == txtDel.getId()) {
                String teacherId = txtTeacherId.getText().toString();
                showDelTeacherDialog(getContext(), teacherId);
            }
        }

        private void showDelTeacherDialog(Context context, String teacherId) {
            AppUtils.showNotiDialog(
                    context
                    , "Are you sure delete this teacher?",
                    new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            int id = 0;
                            try {
                                id = Integer.parseInt(txtTeacherId.getText().toString().split(":")[1].trim());
                            } catch (Exception e) {
                                Log.e("parse:", "convert string to int" + e.getMessage());

                            }
                            boolean check = teacherViewModel.deleteTeacher(id);
                            if (check) {
                                AppUtils.showSuccessDialog(context
                                        , "Delete teacher successfully!");
                                teacherListAdapter.submitList(teacherViewModel.getAllTeacher());
                            } else {
                                AppUtils.showErrorDialog(context
                                        , "DELETE ERROR"
                                        , "Teacher have more student!");
                            }
                            return null;
                        }
                    }
            );
//            if(del){
//                boolean checkFK= teacherViewModel.delete(teacherId);
//                if(checkFK){
//                    AppUtils.showSuccessDialog(context, "Delete successfully!");
//
//                }
//            }
        }
    }

    public static class TeacherDiff extends DiffUtil.ItemCallback<Teacher> {

        @Override
        public boolean areItemsTheSame(@NonNull Teacher oldItem, @NonNull Teacher newItem) {
            return oldItem.getId()==(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Teacher oldItem, @NonNull Teacher newItem) {
            return oldItem.equals(newItem);
        }
    }
}
