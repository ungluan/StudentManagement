package com.example.studentmanagement.database_sqlite.Dao;

public enum ERankStudent {

    Average("Trung Bình",0, 5),
    Good("Khá",5,8),
    Excellent("Giỏi",8, 10.1f);


    String rank;
    float from, to;

    ERankStudent(String rank, float from, float to) {
        this.rank = rank;
        this.from = from;
        this.to = to;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public float getFrom() {
        return from;
    }

    public void setFrom(float from) {
        this.from = from;
    }

    public float getTo() {
        return to;
    }

    public void setTo(float to) {
        this.to = to;
    }
}
