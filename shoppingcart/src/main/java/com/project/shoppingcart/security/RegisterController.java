 package com.project.shoppingcart.security;

 import javax.validation.Valid;

 import com.project.shoppingcart.model.UserRepository;
import com.project.shoppingcart.model.data.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping
    public String register(User user)
    {
         return "register";
    } 

    @PostMapping
    public String register(@Valid User user, BindingResult bindingResult , Model model)
    {    
         if(bindingResult.hasErrors())
         {
              return "register";
         }
         if(!user.getPassword().equals(user.getConfirmPassword()))
         {
             model.addAttribute("passwordMatchProblem", "Passwords do not match");
             return "register";
          } 
         user.setPassword(passwordEncoder.encode(user.getPassword()));
         userRepository.save(user);
         return "redirect:/login";
    } 
    
     
 }