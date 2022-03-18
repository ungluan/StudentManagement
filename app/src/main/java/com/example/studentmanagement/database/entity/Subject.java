package com.example.studentmanagement.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "MONHOC")
public class Subject {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "MAMONHOC")
    int id;
    @ColumnInfo(name = "TENMONHOC")
    String subjectName;
    @ColumnInfo(name = "HESO")
    int coefficient;

    public Subject(int id, String subjectName, int coefficient) {
        this.id = id;
        this.subjectName = subjectName;
        this.coefficient = coefficient;
    }

    public int getId() {
        return id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }
}
