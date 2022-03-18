package com.example.studentmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.example.studentmanagement.database.AppDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this,R.layout.activity_main);
//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
//                findFragmentById(R.id.fragmentContainerView);
//        NavController navController = navHostFragment.getNavController();
    }

}