package com.example.studentmanagement.database.entity;

public class Teacher {
    private int id;
    private String teacherName;
    private String phone;
    private String imageUrl;
    private int idAccount;

    public Teacher(){}

    public Teacher(int id, String teacherName, int idAccount, String imageUrl, String phone ) {
        this.id = id;
        this.teacherName = teacherName;
        this.idAccount = idAccount;
        this.imageUrl = imageUrl;
        this.phone = phone;
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
}
