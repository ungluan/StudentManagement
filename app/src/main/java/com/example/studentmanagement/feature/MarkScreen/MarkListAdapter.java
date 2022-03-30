package com.example.studentmanagement.feature.MarkScreen;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.databinding.DialogEnterMarkBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
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
                markViewModel,
                this
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
        private MarkListAdapter markListAdapter;

        public MarkViewHolder(ViewGroup parent, int contentRes, int swipeLeftMenuRes
                , MarkViewModel markViewModel, MarkListAdapter markListAdapter) {
            super(parent, contentRes, swipeLeftMenuRes);
            this.markListAdapter = markListAdapter;
            txtName = findViewById(R.id.txt_name_mark_item);
            txtGenre = findViewById(R.id.txt_genre_mark_item);
            txtBirthdate = findViewById(R.id.txt_birth_day_mark_item);
            txtMark = findViewById(R.id.txt_mark_mark_item);
            // setting swipe menu
            txtEdit = findViewById(R.id.txtEdit);
            txtEdit.setVisibility(View.GONE);
            btnMark = findViewById(R.id.txtDel);
            btnMark.setText("Nhập điểm");
            btnMark.setOnClickListener(this);
            this.markViewModel = markViewModel;
        }

        public MarkViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.txtDel) {
                Toast.makeText(getContext(), "click nhap diem", Toast.LENGTH_SHORT).show();
                showEditMarkOfStudentDialog(getContext()
                        , markViewModel.getStudentByMark(mark.getStudentId(), mark.getSubjectId())
                        , mark.getSubjectId(), mark.getScore());
            }
        }

        public void bind(Mark mark) {
            this.mark = mark;
            Student student = markViewModel.getStudentByMark(mark.getStudentId(), mark.getSubjectId());
            txtName.setText(student.getFirstName() + " " + student.getLastName());
            txtGenre.setText(student.getGender());
            txtBirthdate.setText(student.getBirthday());
            txtMark.setText(Double.toString(mark.getScore()));
//            }
//            markViewModel.getStudentById(mark.getStudentId()).subscribe(
//
//            );
//            markViewModel.getStudentById(mark.getStudentId())
//                    .subscribe(student -> {
//                        txtName.setText(student.getFirstName() + " " + student.getLastName());
//                        txtGenre.setText(student.getGender());
//                        txtBirthdate.setText(student.getBirthday());
//                        txtMark.setText(Double.toString(mark.getScore()));
//                        btnMark.setOnClickListener(v -> {
//                            showEditMarkOfStudentDialog(getContext(), student
//                                    , mark.getSubjectId(), mark.getScore());
//                        });
//                    },
//                            throwable -> Log.d("bind student info by mark", throwable.getMessage()));
//


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
                , String subjectId, Double mark) {
            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogEnterMarkBinding binding = DialogEnterMarkBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);

            binding.editTextStudentGradeMark.setText(student.getGradeId());
            binding.editTextStudentNameMark.setText(student.getLastName() + " " + student.getFirstName());
            binding.editTextStudentIdMark.setText(student.getId() + "");
            binding.editTextSubjectIdMark.setText(subjectId);
            binding.editTextStudentMarkMark.setText(mark + "");


            Button btnEnterMark =  dialog.findViewById(R.id.btn_enter_mark);
            btnEnterMark.setOnClickListener(v -> {
                double score = 0.0;
                try{
                    score = Double.parseDouble(
                            binding.editTextStudentMarkMark.getText().toString());
                }catch (Exception e){
                    AppUtils.showErrorDialog(
                            getContext()
                            , "ParseDouble Error"
                            , e.getMessage()
                    );
                    return;
                }
                this.mark.setScore(score);
                if(markViewModel.updateMark(this.mark)){
                    AppUtils.showSuccessDialog(
                            context
                            , "Update mark successfully!"
                    );
                    markListAdapter.submitList(markViewModel.getMarks(student.getGradeId(), this.mark.getSubjectId()));
                }
                dialog.dismiss();
            });

            binding.btnCancelMark.setOnClickListener(v -> dialog.dismiss());
            dialog.show();

        }
    }

    static class MarkDiff extends DiffUtil.ItemCallback<Mark> {


        @Override
        public boolean areItemsTheSame(@NonNull Mark oldItem, @NonNull Mark newItem) {
            return oldItem.getSubjectId() == newItem.getSubjectId()
                    && oldItem.getStudentId() == newItem.getStudentId() ;
        }


        @Override
        public boolean areContentsTheSame(@NonNull Mark oldItem, @NonNull Mark newItem) {
            return Double.compare(oldItem.getScore(),newItem.getScore())!=0;
        }
    }


}