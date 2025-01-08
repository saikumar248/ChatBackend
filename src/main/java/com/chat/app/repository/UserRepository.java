package com.chat.app.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.app.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByPhoneNumber(String PhoneNumber);
//    Optional<User> findByEmail(String email);
}
