package com.example.Nansy_server.model;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    @Column(nullable = false)
    private String username;

    public void setUsername(String username) { this.username = username; }

    public String getUsername() {
        return username;
    }

    @Column(nullable = false)
    private String password;

    public void setPassword(String password) { this.password = password; }

    public String getPassword() {
        return password;
    }
}
