package com.project.shoppingcart.security;

import javax.transaction.Transactional;

import com.project.shoppingcart.model.AdminRepository;
import com.project.shoppingcart.model.UserRepository;
import com.project.shoppingcart.model.data.Admin;
import com.project.shoppingcart.model.data.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
   @Autowired
   UserRepository userRepo;
   @Autowired
   AdminRepository adminRepo;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        Admin admin = adminRepo.findByUsername(username);

        if (user != null) {
            return user;
        }

        if (admin != null) {
            return admin;
        }

        throw new UsernameNotFoundException("User: " + username + " not found!");
    }
    
}