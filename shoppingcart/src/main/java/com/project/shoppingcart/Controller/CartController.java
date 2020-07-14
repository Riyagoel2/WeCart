package com.project.shoppingcart.Controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.project.shoppingcart.model.ProductRepository;
import com.project.shoppingcart.model.data.Cart;
import com.project.shoppingcart.model.data.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
@SuppressWarnings("unchecked")
public class CartController {

    @Autowired
    private ProductRepository productRepo;

    @GetMapping("/add/{id}")
    public String add(@PathVariable int id, HttpSession session, Model model,
            @RequestParam(value = "cartPage", required = false) String cartPage) {
        Product product = productRepo.getOne(id);
        if (session.getAttribute("cart") == null) {
            HashMap<Integer, Cart> cart = new HashMap<>();
            cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
            session.setAttribute("cart", cart);
        }

        else {
            HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
            if (cart.containsKey(id)) {
                int qnty = cart.get(id).getQuantity();
                cart.put(id, new Cart(id, product.getName(), product.getPrice(), ++qnty, product.getImage()));
            }

            else {
                cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
                session.setAttribute("cart", cart);
            }
        }
        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
        double total = 0;
        int size = 0;
        for (Cart value : cart.values()) {
            size += value.getQuantity();
            total += Double.parseDouble(value.getPrice()) * value.getQuantity();
        }

        model.addAttribute("csize", size);
        model.addAttribute("ctotal", total);
        if (cartPage != null) {
            return "redirect:/cart/view";
        }

        return "cart_view";
    }

    @GetMapping("/subtract/{id}")
    public String subtract(@PathVariable int id, HttpSession session, Model model , HttpServletRequest httpServletRequest) {
        Product product = productRepo.getOne(id);
        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
        int qty = cart.get(id).getQuantity();
        if (qty == 1) {
            cart.remove(id);
            if (cart.size() == 0) {
                session.removeAttribute("cart");
            }

        } else {
            cart.put(id, new Cart(id, product.getName(), product.getPrice(), --qty, product.getImage()));
            
        }
        String referLink = httpServletRequest.getHeader("referer");
        return "redirect:"+referLink;

    }
    
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable int id, HttpSession session, Model model , HttpServletRequest httpServletRequest) {
        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
        
            cart.remove(id);
            if (cart.size() == 0) {
                session.removeAttribute("cart");
             }
        String referLink = httpServletRequest.getHeader("referer");
        return "redirect:"+referLink;

    }

    @GetMapping("/view")
    public String view(HttpSession session, Model model) {
        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
        if (cart == null) {
            return "redirect:/";
        } else {
            model.addAttribute("cart", cart);
            model.addAttribute("notoncartview", true);
        }
        return "cart";
    }
    
    @GetMapping("/clear")
    public String clear(HttpSession session , HttpServletRequest httpServletRequest) { // httpservletrequest header referer returns link to the last page opened {
        session.removeAttribute("cart");
        String referLink = httpServletRequest.getHeader("referer");
        return "redirect:"+referLink;
    }
}