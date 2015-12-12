package com.example.altam.pasoseguro;

/**
 * Created by altam on 12/12/2015.
 */
public class User {
    String user, pass, email, age;

    public User(String user, String pass, String email, String age) {
        this.user = user;
        this.pass = pass;
        this.email = email;
        this.age = age;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
