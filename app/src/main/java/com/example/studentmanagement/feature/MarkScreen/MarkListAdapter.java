package com.example.studentmanagement.feature.MarkScreen;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.databinding.DialogAddSubjectBinding;
import com.example.studentmanagement.databinding.DialogEnterMarkBinding;
import com.example.studentmanagement.feature.StudentScreen.StudentViewModel;
import com.example.studentmanagement.utils.AppUtils;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

public class MarkListAdapter extends ListAdapter<Mark, MarkListAdapter.MarkViewHolder> {

    private MarkViewModel markViewModel;


    protected MarkListAdapter(MarkViewModel markViewModel, @NonNull DiffUtil.ItemCallback<Mark> diffCallback) {
        super(diffCallback);

        this.markViewModel = markViewModel;

    }

    @NonNull
    @Override
    public MarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MarkViewHolder(
                parent,
                R.layout.student_mark_item,
                R.layout.item_swipe_left_menu,
                markViewModel
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MarkViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class MarkViewHolder extends SwipeViewHolder implements View.OnClickListener {

        private MarkViewModel markViewModel;
        private TextView txtName;
        private TextView txtGenre;
        private TextView txtBirthdate;
        private TextView txtEdit;
        private TextView btnMark;
        private TextView txtMark;
        private Mark mark;

        public MarkViewHolder(ViewGroup parent, int contentRes, int swipeLeftMenuRes, MarkViewModel markViewModel) {
            super(parent, contentRes, swipeLeftMenuRes);
            txtName = findViewById(R.id.txt_name_mark_item);
            txtGenre = findViewById(R.id.txt_genre_mark_item);
            txtBirthdate = findViewById(R.id.txt_birth_day_mark_item);
            txtMark = findViewById(R.id.txt_mark_mark_item);
            txtEdit = findViewById(R.id.txtEdit);
            txtEdit.setVisibility(View.GONE);
            btnMark = findViewById(R.id.txtDel);
            btnMark.setText("Nhập điểm");
            this.markViewModel = markViewModel;
        }

        public MarkViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v){
            if(v.getId()==R.id.txtDel){
                Toast.makeText(getContext(), "click nhap diem", Toast.LENGTH_SHORT).show();
            }
        }

        public void bind(Mark mark) {
            this.mark = mark;
//            markViewModel.getStudentById(mark.getStudentId()).subscribe(
//
//            );
            markViewModel.getStudentById(mark.getStudentId())
                    .subscribe(student -> {
                        txtName.setText(student.getFirstName() + " " + student.getLastName());
                        txtGenre.setText(student.getGender());
                        txtBirthdate.setText(student.getBirthday());
                        txtMark.setText(Double.toString(mark.getMark()));
                        btnMark.setOnClickListener(v -> {
                            showEditMarkOfStudentDialog(getContext(), student
                                    , mark.getSubjectId(), mark.getMark());
                        });
                    },
                            throwable -> Log.d("bind student info by mark", throwable.getMessage()));




//            txtName.setText(student.getFirstName() + " " + student.getLastName());
//                        txtGenre.setText(student.getGender());
//                        txtBirthdate.setText(student.getBirthday());
//                         txtMark.setText(Double.toString(mark.getMark()));
//                        btnMark.setOnClickListener(v -> {
//                            showEditMarkOfStudentDialog(getContext(), student
//                                    , mark.getSubjectId(), mark.getMark());
//                        });


        }

        private void showEditMarkOfStudentDialog(Context context, Student student
                ,String subjectId, Double mark){
            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogEnterMarkBinding binding = DialogEnterMarkBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);

            binding.editTextStudentGradeMark.setText(student.getGradeId());
            binding.editTextStudentNameMark.setText(student.getLastName()+ " " + student.getFirstName());
            binding.editTextStudentIdMark.setText(student.getId()+"");
            binding.editTextSubjectIdMark.setText(subjectId);
            binding.editTextStudentMarkMark.setText(mark+"");

            binding.btnEnterMark.setOnClickListener(v->{
                this.mark.setMark(Double.parseDouble(
                        binding.editTextStudentMarkMark.getText().toString()));
                markViewModel.updateMark(this.mark).subscribe(
                        () -> Observable.just("Cập nhật điểm thành công!")
                                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                                message -> {
                                    Toast.makeText(
                                            context, message,
                                            Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                        ),
                        throwable -> AppUtils.showNotificationDialog(
                                context,
                                "Cập nhật điểm thất bại!",
                                throwable.getLocalizedMessage()
                        )
                );
                dialog.dismiss();
            });

            binding.btnCancelMark.setOnClickListener(v -> dialog.dismiss());
            dialog.show();

        }
    }

    static class MarkDiff extends DiffUtil.ItemCallback<Mark>{



        @Override
        public boolean areItemsTheSame(@NonNull Mark oldItem, @NonNull Mark newItem) {
            return oldItem.getSubjectId()==newItem.getSubjectId()
                    && oldItem.getStudentId()==newItem.getStudentId();
        }


        @Override
        public boolean areContentsTheSame(@NonNull Mark oldItem, @NonNull Mark newItem) {
            return oldItem.equals(newItem);
        }
    }


}
