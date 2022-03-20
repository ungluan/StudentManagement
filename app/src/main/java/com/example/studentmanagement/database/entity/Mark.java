package com.example.studentmanagement.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "DIEM", primaryKeys = {"MAHOCSINH","MAMONHOC"})
public class Mark {
    @ColumnInfo(name = "MAHOCSINH")
    int studentId;
    @ColumnInfo(name = "MAMONHOC")
    int subjectId;
    @ColumnInfo(name = "DIEM")
    double mark;
}
