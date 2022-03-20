package com.example.studentmanagement.feature.GradeScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.databinding.GradeItemBinding;
import com.example.studentmanagement.databinding.ItemSwipeLeftMenuBinding;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;

public class GradeListAdapter extends ListAdapter<Grade, GradeListAdapter.GradeViewHolder> {
    public GradeListAdapter(@NonNull DiffUtil.ItemCallback<Grade> diffCallback) {
        super(diffCallback);
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
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class GradeViewHolder extends SwipeViewHolder implements View.OnClickListener {


        private TextView txtEdit;
        private TextView txtDel;
        private TextView txtGradeName;
        private TextView txtTeacherName;
        private TextView txtNumbers;

        public GradeViewHolder(
                ViewGroup parent,
                GradeItemBinding gradeItemBinding,
                ItemSwipeLeftMenuBinding itemSwipeLeftMenuBinding) {
            super(parent, R.layout.grade_item, R.layout.item_swipe_left_menu);

            txtGradeName = findViewById(R.id.txt_grade_name);
            txtNumbers = findViewById(R.id.txt_numbers_of_student);
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

            txtNumbers.setText(getString(R.string.numbers_of_the_grade,32));
            txtTeacherName.setText(getString(R.string.teacher_of_the_grade,grade.getTeacherName()));
            txtGradeName.setText(getString(R.string.name_of_the_grade,grade.getGradeName()));

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.txtEdit) {
                txtEdit.setText("Clicked");
            } else if (v.getId() == R.id.txtDel) {
                showToast("Delete Clicked");
            }
        }

        private void showToast(String message) {
            Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public static class GradeDiff extends DiffUtil.ItemCallback<Grade> {

        @Override
        public boolean areItemsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
            return oldItem.equals(newItem);
        }
    }
}
