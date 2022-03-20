package com.example.studentmanagement.database.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "HOCSINH", foreignKeys =
    @ForeignKey(entity = Grade.class, parentColumns = "LOP",
            childColumns = "LOP", onUpdate = CASCADE),
        indices = @Index(value = "LOP")
)
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
    String gradeId;

    public Student(int id, String firstName, String lastName, String gender, String birthday, String gradeId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.gradeId = gradeId;
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

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }
}
