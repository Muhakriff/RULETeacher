package com.example.ruleteacher.ui.profile;

public class User {

    private String teacherID, email, name, position;

    public User(String teacherID, String email, String name, String position) {
        this.teacherID = teacherID;
        this.email = email;
        this.name = name;
        this.position = position;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }
}
