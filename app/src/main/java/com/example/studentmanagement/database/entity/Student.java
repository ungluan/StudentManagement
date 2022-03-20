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


    public Student(int id, String firstName, String lastName, String gender, String birthday, int idLop) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.idLop = idLop;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getIdLop() {
        return idLop;
    }

    public void setIdLop(int idLop) {
        this.idLop = idLop;
    }
}


