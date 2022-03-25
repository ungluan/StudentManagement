package com.example.studentmanagement.feature.MarkScreen;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Student;
import com.example.studentmanagement.feature.StudentScreen.StudentViewModel;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

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
        public void onClick(View v) {
            if(v.getId()==R.id.txtDel){
                Toast.makeText(getContext(), "click nhap diem", Toast.LENGTH_SHORT).show();
            }
        }

        public void bind(Mark mark) {
            this.mark = mark;
            markViewModel.getStudentById(mark.getStudentId()).subscribe(
                    student -> {
                        txtName.setText(student.getFirstName() + " " + student.getLastName());
                        txtGenre.setText(student.getGender());
                        txtBirthdate.setText(student.getBirthday());
                         txtMark.setText(Double.toString(mark.getMark()));
                        btnMark.setOnClickListener(this);
                    },
                            throwable -> Log.d("MarkViewHolder:", throwable.getMessage()),
                    () -> Log.d("MarkViewHolder", "onCompleted")
            );


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
