package com.example.studentmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database_sqlite.Dao.GradeDao;
import com.example.studentmanagement.utils.AppUtils;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;
//    private NavController navController;
//    private DrawerLayout drawerLayout;
//    private NavHostFragment navHostFragment;
//    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this,R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.tool_bar);
//        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
//        navigationView = findViewById(R.id.nav_view);
//
//
//
//        navController = navHostFragment.getNavController();
//
//
//
////       navController = Navigation.findNavController(this, R.id.fragmentContainerView);
//
//        drawerLayout = findViewById(R.id.drawer);
//
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
//                .setDrawerLayout(drawerLayout)
//                 .build();
//
//        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);





    }

}