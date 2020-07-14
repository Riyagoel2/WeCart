package com.project.shoppingcart;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.project.shoppingcart.model.CategoryRepository;
import com.project.shoppingcart.model.PageRepository;
import com.project.shoppingcart.model.data.Cart;
import com.project.shoppingcart.model.data.Category;
import com.project.shoppingcart.model.data.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice // to share something in all views
@SuppressWarnings("unchecked")
public class Common {
    @Autowired
    PageRepository pageRepo;
    @Autowired
    CategoryRepository categoryRepo;

    @ModelAttribute
    public void shared(Model model, HttpSession session , Principal principal) {
       if(principal!=null)
       {
           model.addAttribute("principal" , principal.getName());
       }
        List<Category> categories = categoryRepo.findAll();
        List<pages> pages = pageRepo.findAllByOrderBySortingAsc();
        boolean cartActive = false;
        if (session.getAttribute("cart") != null) {
            HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
            double total = 0;
            int size = 0;
            for (Cart value : cart.values()) {
                size += value.getQuantity();
                total += Double.parseDouble(value.getPrice()) * value.getQuantity();
            }
           
            model.addAttribute("size", size);
            model.addAttribute("total", total);
            cartActive = true;
            model.addAttribute("cartActive", cartActive);
        }
        model.addAttribute("cpages", pages);
        model.addAttribute("ccategories", categories);

    }

}