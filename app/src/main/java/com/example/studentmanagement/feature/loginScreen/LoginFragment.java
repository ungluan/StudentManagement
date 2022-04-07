package com.example.studentmanagement.feature.loginScreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentmanagement.R;
import com.example.studentmanagement.databinding.FragmentLoginBinding;
import com.example.studentmanagement.feature.home.HomeFragmentDirections;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(sharedPref.getBoolean("Authenticated", false)){
            navigateToHomePage();
        }
    }

    private void navigateToHomePage(){
        NavDirections action = LoginFragmentDirections.actionLoginFragmentToHomeFragment();
        NavController navController = NavHostFragment.findNavController(this);
//        NavController navController = navHostFragment.getNavController();
        navController.navigate(action);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginViewModel loginViewModel = 
                new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        binding.btnLogin.setOnClickListener(v -> {
            String email = String.valueOf(binding.editTextEmail.getText());
            String password = String.valueOf(binding.editTextPassword.getText());
            if(loginViewModel.login(email,password)){
                Toast.makeText(getContext(), "Login Thành công", Toast.LENGTH_SHORT).show();
                saveLogin();
                navigateToHomePage();
            }else{
                Toast.makeText(getContext(), 
                        "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveLogin(){
        SharedPreferences sharedPref = requireActivity().
                getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Authenticated",true);
        editor.apply();
    }
}