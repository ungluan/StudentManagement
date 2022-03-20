package com.example.studentmanagement.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "MONHOC")
public class Subject {
    @PrimaryKey
    @ColumnInfo(name = "MAMONHOC")
    @NonNull
    String subjectId;
    @ColumnInfo(name = "TENMONHOC")
    String subjectName;
    @ColumnInfo(name = "HESO")
    int coefficient;

    public Subject(String subjectId, String subjectName, int coefficient) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.coefficient = coefficient;
    }

    public String getId() {
        return subjectId;
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
