package com.project.shoppingcart.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
 
@Controller
public class HomeController {
@GetMapping("/random")
public String  home()
{
    return "home";
}
    
}