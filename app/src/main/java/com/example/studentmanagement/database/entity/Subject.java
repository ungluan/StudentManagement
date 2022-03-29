package com.example.studentmanagement.database.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Member;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "Subject{" +
                "subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", coefficient=" + coefficient +
                '}';
    }

    /***
     *
     *  You have to know that
     *
     *  ArrayList#removeAll(Collection)
     *
     *  ArrayList#contains(Object)
     *
     *  ArrayList#indexOf(Object)
     *
     * Object#equals
     *  So if equals is not correctly overridden (following the equals contract rules), you're not getting the correct behaviour.
     *
     ***/
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj==null || obj.getClass() != Subject.class) return false;
        return ((Subject) obj).subjectId.equals(this.subjectId)
                && ((Subject) obj).subjectName.equals(this.subjectName)
                && ((Subject) obj).coefficient == this.coefficient;
    }

}
