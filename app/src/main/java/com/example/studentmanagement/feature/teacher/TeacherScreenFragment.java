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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Mark;
import com.example.studentmanagement.database.entity.Teacher;
import com.example.studentmanagement.databinding.DialogAddTeacherBinding;
import com.example.studentmanagement.databinding.FragmentTeacherScreenBinding;
//import com.example.studentmanagement.feature.teacher.TeacherScreenFragmentDirections;
import com.example.studentmanagement.utils.AppUtils;
import com.example.studentmanagement.utils.ItemMargin;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.regex.Pattern;


public class TeacherScreenFragment extends Fragment {
    private FragmentTeacherScreenBinding binding;
    private OmegaRecyclerView recyclerView;
    private TeacherViewModel teacherViewModel;
    private TeacherListAdapter adapter;
    private ImageView imageViewTeacher;
    private int CODE=998;
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

        System.out.println("Rank student:"+new Mark(1, "a", 7).rankStudent());

        setUpSearchView();

        teacherViewModel = new
                ViewModelProvider(requireActivity()).get(TeacherViewModel.class);

        recyclerView = binding.recyclerViewTeacher;

        //set data to recycler view
         adapter = new
                 TeacherListAdapter(teacherViewModel, new TeacherListAdapter.TeacherDiff());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
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
            NavDirections action = TeacherScreenFragmentDirections.actionTeacherScreenFragmentToHomeFragment();
            Navigation.findNavController(v).navigate(action);
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
        binding.dialogTitleAddTeacher.setText("Thêm môn học");
        binding.btnConfirmAddTeacher.setText("LƯU");
        binding.editTextTeacherId.setEnabled(true);

        // get data
        // cancel button
        binding.btnCancelAddTeacher.setOnClickListener(v -> dialog.dismiss());
        
        // confirm add teacher
        binding.btnConfirmAddTeacher.setOnClickListener(view -> {



            String name = binding.editTextTeacherName.getText().toString();
            if(name.equals("")){
                binding.textInputTeacherName.setError("Teacher name cannot be blank");
                return;
            }else binding.textInputTeacherName.setErrorEnabled(false);

            String phone = binding.editTextTeacherPhone.getText().toString();
            if(phone.equals("")){
                binding.textInputTeacherPhone.setError("Teacher phone cannot be blank");
                return;
            }else{

                if(!phone.matches("0[0-9]{9}")){
                    binding.textInputTeacherPhone.setError("Teacher phone dont match format");
                    return;
                }

                binding.textInputTeacherPhone.setErrorEnabled(false);
            }





            // set normal





            Teacher teacher = new Teacher(name, phone, AppUtils.getImageString(CODE));
            AppUtils.deleteCode(CODE);
            // check constraint



            boolean success = teacherViewModel.insertTeacher(teacher);
            if (success) {
                AppUtils.showSuccessDialog(context
                        , "INSERT SUCCESS");
                dialog.dismiss();
                adapter.submitList(teacherViewModel.getAllTeacher());

            } else {
                AppUtils.showErrorDialog(context
                        , "INSERT FAILED",
                        "Teacher id have conflict");
            }

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

//    private void chooseImage() {
//        requestPermission();
//    }
//
//    private void requestPermission() {
//        PermissionListener permissionListener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
//                openImagePicker();
//            }
//
//            @Override
//            public void onPermissionDenied(List<String> deniedPermissions) {
//                Toast.makeText(requireContext(), "Permission deny\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
//            }
//        };
//        TedPermission.create()
//                .setPermissionListener(permissionListener)
//                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
//                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .check();
//    }
//
//    private void openImagePicker() {
//
//        TedBottomPicker.with(requireActivity())
//                .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
//                    @Override
//                    public void onImageSelected(Uri uri) {
//                        // here is selected image uri
//                        imageString = uri.toString();
//                        System.out.println("uri:" +imageString);
//
//                        try {
//                            imageViewTeacher.setImageBitmap(
//                                    MediaStore.Images.Media.getBitmap(getContext().getContentResolver()
//                                            , Uri.parse(imageString)));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//    }
}
