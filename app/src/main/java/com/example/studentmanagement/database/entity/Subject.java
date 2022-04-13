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

    @ColumnInfo(name ="HINHANH")
    String image="";

    public Subject() {
    }

    public Subject(String subjectId, String subjectName, int coefficient) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.coefficient = coefficient;
    }

    public Subject(@NonNull String subjectId, String subjectName, int coefficient, String image) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.coefficient = coefficient;
        this.image = image;
    }

    @NonNull
    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(@NonNull String subjectId) {
        this.subjectId = subjectId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
    public boolean equals(@Nullable Object obj) {
        if(obj==null || obj.getClass() != Subject.class) return false;
        return ((Subject) obj).subjectId.equals(this.subjectId)
                && ((Subject) obj).subjectName.equals(this.subjectName)
                && ((Subject) obj).coefficient == this.coefficient
                &&  ((Subject) obj).image.equals(this.image) ;
    }

}
