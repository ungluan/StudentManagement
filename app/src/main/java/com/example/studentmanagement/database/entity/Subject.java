package com.example.studentmanagement.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Member;

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
        return subjectId + "-" + subjectName;
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
    public boolean equals(Object sub2) {
        if (!(sub2 instanceof Subject)) {
            return false;
        }
        Subject subject2 = (Subject) sub2;
        return subject2.getId().equals(this.getId());
    }
}
