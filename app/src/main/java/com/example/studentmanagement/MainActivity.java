package com.example.studentmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.studentmanagement.database.AppDatabase;
import com.example.studentmanagement.database.entity.Grade;
import com.example.studentmanagement.database_sqlite.Dao.GradeDao;
import com.example.studentmanagement.utils.AppUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this,R.layout.activity_main);
    }

}