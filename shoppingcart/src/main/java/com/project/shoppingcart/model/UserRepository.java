package com.project.shoppingcart.model;

import com.project.shoppingcart.model.data.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User , Integer> {
    User findByUsername(String username);

}