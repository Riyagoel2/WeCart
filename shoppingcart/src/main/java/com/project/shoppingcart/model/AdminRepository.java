package com.project.shoppingcart.model;

import com.project.shoppingcart.model.data.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AdminRepository extends JpaRepository<Admin , Integer> {
    Admin findByUsername(String username);
    

}