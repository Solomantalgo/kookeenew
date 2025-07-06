package com.kookee.merchandiser_backend;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "merchandiserkookee") // this MUST match your table name in DB
public class Merchandiser {

    @Id
    private String username;

    private String password;

    public Merchandiser() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}