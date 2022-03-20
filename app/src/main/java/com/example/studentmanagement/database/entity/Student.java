package com.example.studentmanagement.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "HOCSINH")
public class Student {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "MAHOCSINH")
    int id;
    @ColumnInfo(name = "HO")
    String firstName;
    @ColumnInfo(name = "TEN")
    String lastName;
    @ColumnInfo(name = "PHAI")
    String gender;
    @ColumnInfo(name = "NGAYSINH")
    String birthday;
    @ColumnInfo(name = "LOP")
    int idLop;
}
