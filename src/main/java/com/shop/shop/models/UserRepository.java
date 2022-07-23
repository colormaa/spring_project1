package com.shop.shop.models;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.shop.models.data.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
