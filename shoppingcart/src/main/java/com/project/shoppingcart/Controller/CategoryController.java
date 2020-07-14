package com.project.shoppingcart.Controller;

import java.util.HashMap;
import java.util.List;

import com.project.shoppingcart.model.CategoryRepository;
import com.project.shoppingcart.model.ProductRepository;
import com.project.shoppingcart.model.data.Category;
import com.project.shoppingcart.model.data.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/{slug}")
    public String category(@PathVariable String slug,Model model, @RequestParam(value="page", required = false)Integer  p)
    { List<Category> categories=categoryRepository.findAll();
        HashMap<Integer,String> catego=new HashMap<>();
       for(Category cat:categories)
        catego.put(cat.getId(),cat.getName());
       int  productperpage=6;
       int page=(p!=null )? p:0;
       long count=0;
       Pageable pageable=PageRequest.of(page, productperpage);
       if(slug.equals("all"))
       {
         Page<Product> products=productRepository.findAll(pageable);
        count=productRepository.count();
        model.addAttribute("products", products);
        }
      
        else{
 Category category=categoryRepository.findBySlug(slug);
 if(category==null)
 {
     return "redirect:/";

 }

int categoryId=category.getId();
String categoryName=category.getName();    
List<Product> products=productRepository.findAllByCategoryId(Integer.toString(categoryId),pageable);
count=productRepository.countByCategoryId(Integer.toString(categoryId));
model.addAttribute("products", products);
model.addAttribute("categoryName", categoryName);
}
        
       
        
        double pageCount=Math.ceil((double)count/(double)productperpage);
        model.addAttribute("count", count);
        model.addAttribute("perPage", productperpage);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("page", page);
       
      
        return "product";
    }
    
       
    
}