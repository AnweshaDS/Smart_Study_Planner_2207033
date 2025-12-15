package com.example.myapp.model;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private int age;
    private String qualification;

    public User(int id, String username, String email, String password,
                String fullName, int age, String qualification) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.age = age;
        this.qualification = qualification;
    }

    public User(String username, String email, String password,
                String fullName, int age, String qualification) {
        this(0, username, email, password, fullName, age, qualification);
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public int getAge() { return age; }
    public String getQualification() { return qualification; }
}
