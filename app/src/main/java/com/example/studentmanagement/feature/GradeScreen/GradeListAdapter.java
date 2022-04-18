package com.example.studentmanagement.feature.GradeScreen;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.databinding.DialogAddGradeBinding;
import com.example.studentmanagement.databinding.DialogDelGradeBinding;

import com.example.studentmanagement.utils.AppUtils;
import com.omega_r.libs.omegarecyclerview.swipe_menu.SwipeViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GradeListAdapter extends ListAdapter<Grade, GradeListAdapter.GradeViewHolder> implements Filterable {
    private final GradeViewModel gradeViewModel;
    private final ActivityResultLauncher<Intent> activityGalleryImageLauncher;
    private int positionImageOnClicked = -1;
    private final List<Grade> gradeList = new ArrayList<>();

    public int getPositionImageOnClicked() {
        return positionImageOnClicked;
    }

    public GradeListAdapter(
            GradeViewModel gradeViewModel,
            ActivityResultLauncher<Intent> activityGalleryImageLauncher
            , @NonNull DiffUtil.ItemCallback<Grade> diffCallback) {
        super(diffCallback);
        this.gradeViewModel = gradeViewModel;
        this.activityGalleryImageLauncher = activityGalleryImageLauncher;
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (gradeList.size() == 0) gradeList.addAll(getCurrentList());
        return new GradeViewHolder(
                parent,
                gradeViewModel,
                this
        );
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        holder.bind(getItem(position));
        holder.gradeImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            activityGalleryImageLauncher.launch(intent);
            positionImageOnClicked = holder.getAdapterPosition();
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                List<Grade> list = gradeList;
                if (!strSearch.isEmpty()) {
                    List<Grade> grades = new ArrayList<>();
                    for (Grade grade : list) {
                        if (grade.getGradeId().toLowerCase().contains(strSearch.toLowerCase())) {
                            grades.add(grade);
                        }
                    }
                    list = grades;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                submitList((List<Grade>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    static class GradeViewHolder extends SwipeViewHolder implements View.OnClickListener {
        private final GradeViewModel gradeViewModel;
        private final GradeListAdapter gradeListAdapter;

        private final TextView txtEdit;
        private final TextView txtDel;
        private final TextView txtGradeName;
        private final TextView txtTeacherInfo;
        private final ImageView gradeImage;
        private Teacher teacher;
        private final List<String> teacherItems = new ArrayList();
        private Grade grade;

        public GradeViewHolder(
                ViewGroup parent,
                GradeViewModel gradeViewModel,
                GradeListAdapter gradeListAdapter
        ) {
            super(parent, R.layout.grade_item, R.layout.item_swipe_left_menu);
            this.gradeViewModel = gradeViewModel;
            this.gradeListAdapter = gradeListAdapter;
            txtGradeName = findViewById(R.id.txt_grade_name);
            txtTeacherInfo = findViewById(R.id.txt_teacher_info);
            gradeImage = findViewById(R.id.grade_image);
            txtEdit = findViewById(R.id.txtEdit);
            txtDel = findViewById(R.id.txtDel);
            txtEdit.setOnClickListener(this);
            txtDel.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Grade grade) {
            this.grade = grade;
            teacher = gradeViewModel.getTeacherById(grade.getTeacherId());
            txtGradeName.setText(getString(R.string.name_of_the_grade, grade.getGradeId()));
            txtTeacherInfo.setText(getString(R.string.teacher_of_the_grade, teacher.getTeacherName()));
            Picasso.get().load(Uri.parse(grade.getImage()))
                    .placeholder(R.drawable.no_image).into(gradeImage);
        }


        public void showToast(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
            binding.editTextGradeName.setText(
                    String.valueOf(txtGradeName.getText()).split(":")[1].trim());
            binding.editTextGradeName.setEnabled(false);
            // format??
            binding.editTextTeacherId.setText(teacher.getId() + " - " + teacher.getTeacherName());
            binding.btnAdd.setText("Sửa");
            binding.txtTitle.setText("Sửa lớp học");

            initialDropdown(binding);

            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            binding.btnAdd.setOnClickListener(v -> {
                String gradeId = String.valueOf(binding.editTextGradeName.getText());
                // format
                int teacherId = AppUtils.getTeacherIdFromDropDown(binding.editTextTeacherId.getText().toString());
                int gradeSchool ;
                try {
                    gradeSchool = Integer.parseInt(binding.editTextGradeSchool.getText().toString());
                    if(gradeSchool<1 && gradeSchool>12){
                        binding.textInputLayoutGradeName.setError("Khối không hợp lệ. (1->12)");
                        return;
                    }
                }catch (Exception e){
                    binding.textInputLayoutGradeName.setError("Khối là một số nguyên.");
                    return;
                }
                Grade gradeEdit = new Grade(gradeId, teacherId, grade.getImage(),gradeSchool );
                if (gradeViewModel.updateGrade(gradeEdit)) {
                    showToast("Cập nhật lớp thành công!");
                    gradeListAdapter.submitList(updateGradeInList(gradeEdit));
                    gradeListAdapter.notifyItemChanged(getAdapterPosition());
                    dialog.dismiss();
                } else {
                    AppUtils.showNotificationDialog(
                            getContext(), "Thông báo", "Cập nhật lớp thất bại!"
                            , null);
                }
            });
            dialog.show();
        }

        private List<Grade> updateGradeInList(Grade grade) {
            List<Grade> grades = new ArrayList(gradeListAdapter.getCurrentList());
            System.out.println(grades);
            grades.get(getAdapterPosition()).setTeacherId(grade.getTeacherId());
            System.out.println(grades);
            return grades;
        }

        private void initialDropdown(DialogAddGradeBinding binding) {
            List<String> teacherList = gradeViewModel.getTeacherHaveNotGrade().stream()
                    .map(item -> item.getId() + " - " + item.getTeacherName())
                    .collect(Collectors.toList());
            teacherItems.clear();
            teacherItems.addAll(teacherList);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),
                    R.layout.dropdown_item, teacherItems);
            if (teacherItems.size() > 0) {
                binding.editTextTeacherId.setAdapter(arrayAdapter);
            } else binding.editTextTeacherId.setText("Danh sách giáo viên trống!");
        }

        private void showDelGradeDialog(Context context) {
            Dialog dialog = new Dialog(context, R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogDelGradeBinding binding = DialogDelGradeBinding.inflate(
                    LayoutInflater.from(context)
            );
            dialog.setContentView(binding.getRoot());
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
            binding.txtTitle.setText("Thông báo");
            binding.txtContent.setText("Bạn có chắc chắn muốn xóa " + txtGradeName.getText() + " không?");

            binding.btnCancel.setOnClickListener(v -> dialog.dismiss());
            binding.btnDel.setOnClickListener(v -> {
                String gradeId = String.valueOf(txtGradeName.getText()).split(":")[1].trim();
//                String teacherName = AppUtils.formatPersonName(String.valueOf(txtTeacherName.getText()).split(":")[1].trim());
//                int teacherId = Integer.parseInt();
                Grade gradeDel = new Grade(gradeId, teacher.getId(), "", grade.getGradeSchool());
                // Kiem tra co hs trong lop khong
                if (gradeViewModel.getNumberOfStudentByGrade(gradeDel.getGradeId()) == 0) {
                    if (gradeViewModel.deleteGrade(gradeDel.getGradeId())) {
                        List<Grade> grades = new ArrayList<Grade>(gradeListAdapter.getCurrentList());
                        grades.remove(gradeDel);
                        gradeListAdapter.submitList(grades);
                        gradeListAdapter.notifyItemRemoved(getAdapterPosition());
                        showToast("Xóa lớp thành công!");
                        dialog.dismiss();
                    } else {
                        AppUtils.showNotificationDialog(
                                getContext(), "Thông báo", "Xóa lớp thất bại!", null);
                    }
                } else {
                    AppUtils.showNotificationDialog(
                            getContext(), "Thông báo", "Lớp có học sinh không thể xóa!", null);
                }
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
            return oldItem.equals(newItem);
        }
    }
}