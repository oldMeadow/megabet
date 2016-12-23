package com.example.eqoram.alpha;

/**
 * Created by eqoram on 09.11.16.
 */

public class User {
    String email;
    String name;

    public User(String email, String name){
        this.email = email;
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public String getName(){
        return name;
    }

    public void setEmail(String newEmail){
        email = newEmail;
    }

    public void setName(String newName){
        name = newName;
    }
}
