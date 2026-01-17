package com.example.Nansy_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.Nansy_server.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}