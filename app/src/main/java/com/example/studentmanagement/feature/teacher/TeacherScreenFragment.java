package com.example.studentmanagement.feature.teacher;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Account;
import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.databinding.DialogAddTeacherBinding;
import com.example.studentmanagement.databinding.FragmentTeacherScreenBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.example.studentmanagement.utils.ItemMargin;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.List;


public class TeacherScreenFragment extends Fragment {
    private FragmentTeacherScreenBinding binding;
    private OmegaRecyclerView recyclerView;
    private TeacherViewModel teacherViewModel;
    private TeacherListAdapter adapter;
    private ImageView imageViewTeacher;
    private int CODE=998;

    private List<Account> accounts;
    private AccountSpinnerAdapter spinnerAdapter;
    public TeacherScreenFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTeacherScreenBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpSearchView();
        teacherViewModel = new ViewModelProvider(requireActivity()).get(TeacherViewModel.class);
        recyclerView = binding.recyclerViewTeacher;
        //set data to recycler view
        adapter = new TeacherListAdapter(teacherViewModel, new TeacherListAdapter.TeacherDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    binding.btnAddTeacher.setVisibility(View.GONE);
                }else{
                    binding.btnAddTeacher.setVisibility(View.VISIBLE);
                }
            }
        });
        // add margin to recyccler view item
        recyclerView.addItemDecoration(new ItemMargin(
                16, 0, 0, 16
        ));
        adapter.submitList(teacherViewModel.getAllTeacher());
        recyclerView.setAdapter(adapter);
        binding.btnAddTeacher.setOnClickListener(v -> showDialogAddTeacher(requireContext()));
        binding.btnBackTeacherScreen.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.homeFragment);
        });


    }
    // get data from search view & query api to get the results
    private void setUpSearchView() {
        binding.searchViewTeacherList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.submitList(teacherViewModel.searchTeacherBySameNameOrPhoneOrAccount(query));
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.submitList(teacherViewModel.searchTeacherBySameNameOrPhoneOrAccount(newText));
                return false;
            }
        });
    }
    private void showDialogAddTeacher(Context context) {


        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAddTeacherBinding binding = DialogAddTeacherBinding.inflate(
                LayoutInflater.from(context)
        );
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);
        imageViewTeacher = binding.imageviewTeacherDialog;
        binding.dialogTitleAddTeacher.setText("Thêm giáo viêns");
        binding.btnConfirmAddTeacher.setText("LƯU");
        binding.editTextTeacherId.setEnabled(true);
        // load spinner
        loadAccountSpinner(binding);
        // get data
        // cancel button
        binding.btnCancelAddTeacher.setOnClickListener(v -> dialog.dismiss());
        // confirm add teacher
        binding.btnConfirmAddTeacher.setOnClickListener(view -> {
            Teacher teacher = checkInput(binding);
            if (teacher == null) return;
            AppUtils.deleteCode(CODE);// delete image global
            insertTeacher(teacher, dialog);
        });
        //Choose image
        binding.btnChooseImageTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.chooseImage(requireContext(), imageViewTeacher, CODE);
            }
        });
        dialog.show();

    }

    private void loadAccountSpinner(DialogAddTeacherBinding binding) {
       accounts = teacherViewModel.getAccountNotInTeacher();
        spinnerAdapter = new AccountSpinnerAdapter(requireContext(), accounts);
        binding.spinnerAccount.setAdapter(spinnerAdapter);

    }

    private void insertTeacher(Teacher teacher, Dialog dialog) {
        boolean success = teacherViewModel.insertTeacher2(teacher);
        if (success) {
            AppUtils.showSuccessDialog(requireContext()
                    , "Thêm mới thành công");
            dialog.dismiss();
            adapter.submitList(teacherViewModel.getAllTeacher());

        } else {
            AppUtils.showErrorDialog(requireContext()
                    , "Thêm mới thất bại",
                    "Mã giáo viên bị trùng");
        }
    }

    public Teacher checkInput(DialogAddTeacherBinding binding) {
        String name = binding.editTextTeacherName.getText().toString();
        if (name.equals("")) {
            binding.textInputTeacherName.setError("Tên giáo viên không được rỗng");
        } else binding.textInputTeacherName.setErrorEnabled(false);

        String phone = binding.editTextTeacherPhone.getText().toString();
        if (phone.equals("")) {
            binding.textInputTeacherPhone.setError("Số điện thoại giáo viên không được trống");
        } else {

            if (!phone.matches("0[0-9]{9}")) {
                binding.textInputTeacherPhone.setError("Số điện thoại không hợp lệ");
            }
            binding.textInputTeacherPhone.setErrorEnabled(false);
        }
        if (binding.textInputTeacherName.isErrorEnabled() || binding.textInputTeacherPhone.isErrorEnabled()) {
            return null;
        } else return new Teacher(name, phone, AppUtils.getImageString(CODE).equals("")?"":AppUtils.getImageString(CODE));
    }
}
