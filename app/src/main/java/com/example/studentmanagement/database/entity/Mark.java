package com.example.studentmanagement.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DIEM", primaryKeys = {"MAHOCSINH","MAMONHOC"})
public class Mark {
    @ColumnInfo(name = "MAHOCSINH")
    int studentId;
    @ColumnInfo(name = "MAMONHOC")
    int subjectId;
    @ColumnInfo(name = "DIEM")
    double mark;

    public Mark(int studentId, int subjectId, double mark) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.mark = mark;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }
}
