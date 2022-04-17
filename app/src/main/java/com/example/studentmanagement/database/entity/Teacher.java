package com.example.studentmanagement.database.entity;

import java.util.Objects;

public class Teacher {
    private int id;
    private String teacherName;
    private String phone;
    private String imageUrl;
    private int idAccount;

    public Teacher(){}


//    public Teacher(int id, String teacherName, String phone, String imageUrl, int idAccount) {
//        this.id = id;
//        this.teacherName = teacherName;
//        this.phone = phone;
//        this.imageUrl = imageUrl;
//        this.idAccount = idAccount;
//    }

    public Teacher(int id, String teacherName, int idAccount, String imageUrl, String phone ) {
        this.id = id;
        this.teacherName = teacherName;
        this.idAccount = idAccount;
        this.imageUrl = imageUrl;
        this.phone = phone;
    }

    public Teacher(String teacherName, String phone, String imageUrl) {
        this.teacherName = teacherName;
        this.phone = phone;
        this.imageUrl = imageUrl;
    }

    public Teacher(String teacherName, String phone, String imageUrl, int idAccount) {
        this.teacherName = teacherName;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.idAccount = idAccount;
    }

    public boolean checkAccount(){
        try{
            getIdAccount();
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        Teacher teacher = (Teacher) o;
        return getId() == teacher.getId() &&
                getIdAccount() == teacher.getIdAccount() &&
                Objects.equals(getTeacherName(), teacher.getTeacherName()) &&
                Objects.equals(getPhone(), teacher.getPhone()) &&
                Objects.equals(getImageUrl(), teacher.getImageUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTeacherName(), getPhone(), getImageUrl(), getIdAccount());
    }
}
