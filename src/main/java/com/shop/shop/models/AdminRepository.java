package com.shop.shop.models;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.shop.models.data.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByUsername(String username);
}
