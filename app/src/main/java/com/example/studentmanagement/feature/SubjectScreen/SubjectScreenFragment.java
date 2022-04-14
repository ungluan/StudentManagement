package com.example.studentmanagement.feature.SubjectScreen;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.studentmanagement.R;
import com.example.studentmanagement.database.entity.Subject;
import com.example.studentmanagement.databinding.DialogAddSubjectBinding;
import com.example.studentmanagement.databinding.FragmentSubjectScreenBinding;
import com.example.studentmanagement.utils.AppUtils;
import com.example.studentmanagement.utils.ItemMargin;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;


public class SubjectScreenFragment extends Fragment {
    private FragmentSubjectScreenBinding binding;
    private OmegaRecyclerView recyclerView;
    private SubjectViewModel subjectViewModel;
    private  SubjectListAdapter adapter;
    private ImageView imageViewSubject;
    private String imageString="";
    private int CODE=100;
    public SubjectScreenFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubjectScreenBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subjectViewModel = new
                ViewModelProvider(requireActivity()).get(com.example.studentmanagement.feature.SubjectScreen.SubjectViewModel.class);

        recyclerView = binding.recyclerViewSubject;

        //set data to recycler view
         adapter = new
                SubjectListAdapter(subjectViewModel, new SubjectListAdapter.SubjectDiff());
        recyclerView.setAdapter(adapter);

        // add margin to recyccler view item

        recyclerView.addItemDecoration(new ItemMargin(
                16, 0, 0, 16
        ));
        adapter.submitList(subjectViewModel.getAllSubject());
        recyclerView.setAdapter(adapter);
        binding.btnAddSubject.setOnClickListener(v -> showDialogAddSubject(requireContext()));
        binding.btnBackSubjectScreen.setOnClickListener(v->{
            NavDirections action = SubjectScreenFragmentDirections.actionSubjectScreenFragmentToHomeFragment();
            Navigation.findNavController(v).navigate(action);
        });


    }

    private void showDialogAddSubject(Context context) {

        Dialog dialog = new Dialog(context, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAddSubjectBinding binding = DialogAddSubjectBinding.inflate(
                LayoutInflater.from(context)
        );
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_white_color);

        imageViewSubject = binding.imageviewSubjectDialog;
        binding.dialogTitleAddSubject.setText("Thêm môn học");
        binding.btnConfirmAddSubject.setText("LƯU");
        binding.editTextSubjectId.setEnabled(true);

        // get data
        // cancel button
        binding.btnCancelAddSubject.setOnClickListener(v -> dialog.dismiss());
        
        // confirm add subject
        binding.btnConfirmAddSubject.setOnClickListener(view -> {
            String id = binding.editTextSubjectId.getText().toString();
            if(id.equals("")){
                binding.textInputSubjectId.setError("Subject id cannot be blank");
                return;
            }else binding.textInputSubjectId.setErrorEnabled(false);

            String name = binding.editTextSubjectName.getText().toString();
            if(name.equals("")){
                binding.textInputSubjectName.setError("Subject name cannot be blank");
                return;
            }binding.textInputSubjectName.setErrorEnabled(false);
            int factor;
            try{
               factor = Integer.parseInt(binding.editTextSubjectCoefficient
                        .getText().toString());
                binding.textInputSubjectCoefficient.setErrorEnabled(false);
            }catch (Exception e){
                binding.textInputSubjectCoefficient.setError("Subject coefficient cannot be blank or not an integer");
                return;
            }

            // set normal





            Subject subject = new Subject(id, name, factor, AppUtils.getImageString(CODE));
            AppUtils.deleteCode(CODE);
            // check constraint



            boolean success = subjectViewModel.insertSubejct(subject);
            if (success) {
                AppUtils.showSuccessDialog(context
                        , "INSERT SUCCESS");
                dialog.dismiss();
                adapter.submitList(subjectViewModel.getAllSubject());

            } else {
                AppUtils.showErrorDialog(context
                        , "INSERT FAILED",
                        "Subject id have conflict");
            }

        });
        
        //Choose image
        binding.btnChooseImageSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AppUtils.chooseImage(requireContext(), imageViewSubject, CODE);
                imageString = AppUtils.getImageString(CODE);
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
//                            imageViewSubject.setImageBitmap(
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
