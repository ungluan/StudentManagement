package com.example.studentmanagement.feature.StudentScreen;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Student;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;


public class StudentListAdapter extends ListAdapter<Student, StudentListAdapter.StudentViewHolder> {

    protected StudentListAdapter(@NonNull DiffUtil.ItemCallback<Student> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentViewHolder(
                parent,
                R.layout.student_item,
                R.layout.item_swipe_left_menu
        );
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class StudentViewHolder extends SwipeViewHolder implements View.OnClickListener {

        private TextView txtName;
        private TextView txtGenre;
        private TextView txtBirthdate;
        private TextView txtEdit;
        private TextView txtDel;

        public StudentViewHolder(ViewGroup parent, int contentRes, int swipeLeftMenuRes) {
            super(parent, contentRes, swipeLeftMenuRes);
            txtName = findViewById(R.id.txt_name);
            txtGenre = findViewById(R.id.txt_genre);
            txtBirthdate = findViewById(R.id.txt_birth_day);
            txtEdit = findViewById(R.id.txtEdit);
            txtDel = findViewById(R.id.txtDel);
        }

        private void bind(Student student){
            txtName.setText(student.getFirstName()+" "+student.getLastName());
            txtGenre.setText(student.getGender());
            txtBirthdate.setText(student.getBirthday());
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==txtEdit.getId()){
                Toast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT).show();
            }else if(v.getId()==txtDel.getId()){
                Toast.makeText(getContext(), "Delete", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class StudentDiff extends DiffUtil.ItemCallback<Student>{

        @Override
        public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
            return oldItem.equals(newItem);
        }
    }
}
