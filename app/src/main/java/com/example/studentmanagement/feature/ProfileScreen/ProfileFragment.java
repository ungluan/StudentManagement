package com.example.studentmanagement.feature.ProfileScreen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.FragmentProfileBinding;
import com.example.studentmanagement.feature.home.HomeFragmentDirections;
import com.example.studentmanagement.utils.AppUtils;
import com.google.android.material.navigation.NavigationView;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBack.setOnClickListener(this::navigateToHomePage);

        binding.buttonProfile.setOnClickListener(this::navigateToInformationPage);
        binding.buttonChangePassword.setOnClickListener(this::navigateToChangePasswordPage);
        binding.buttonLogout.setOnClickListener(v -> {
            AppUtils.updateTeacherId(requireActivity(), -1);
            navigateToLoginPage(v);
        });
    }
    private void navigateToHomePage(View view){
        NavDirections action = ProfileFragmentDirections.actionProfileFragmentToHomeFragment();
        Navigation.findNavController(view).navigate(action);
    }
    private void navigateToInformationPage(View view){
        NavDirections action = ProfileFragmentDirections.actionProfileFragmentToInformationFragment();
        Navigation.findNavController(view).navigate(action);
    }
    private void navigateToChangePasswordPage(View view){
        NavDirections action = ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment();
        Navigation.findNavController(view).navigate(action);
    }
    private void navigateToLoginPage(View view){
        NavDirections action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment();
        Navigation.findNavController(view).navigate(action);
    }
}































































//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
//
//        super.onCreateOptionsMenu(menu, LayoutInflater.from(requireContext()).inflate(R.menu.menu_profile, ););
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getActivity().getMenuInflater().inflate(R.menu.menu_profile, menu);
//        return true;
//    }


//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        System.out.println(item.getTitle());
//        switch (item.getItemId()){
//            case R.id.btn_register:
//                Toast.makeText(getContext(), "Click btn Register", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//
//    }