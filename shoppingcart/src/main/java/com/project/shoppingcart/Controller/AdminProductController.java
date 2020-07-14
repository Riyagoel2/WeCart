package com.project.shoppingcart.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping
    public String index(Model model, @RequestParam(value = "page", required = false) Integer p) {
        List<Category> categories = categoryRepository.findAll();
        HashMap<Integer, String> catego = new HashMap<>();
        for (Category cat : categories)
            catego.put(cat.getId(), cat.getName());
        int productperpage = 6;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, productperpage);
        Page<Product> products = productRepository.findAll(pageable);
        long count = productRepository.count();
        double pageCount = Math.ceil((double) count / (double) productperpage);
        model.addAttribute("count", count);
        model.addAttribute("perPage", productperpage);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("page", page);
        model.addAttribute("products", products);
        model.addAttribute("categories", catego);
        return "admin/products/index";
    }

    @GetMapping("/add")
    public String add(Product product, Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "admin/products/add";

    }

    @PostMapping("/add")
    public String add(@Valid Product product, BindingResult bindingresult, MultipartFile file,
            RedirectAttributes rAttributes, Model model) throws IOException {
        List<Category> categories = categoryRepository.findAll();
        if (bindingresult.hasErrors()) {
            model.addAttribute("categories", categories);
            return "admin/products/add";
        }

        boolean fileOk = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);
        if (filename.endsWith("jpg") || filename.endsWith("png")) {
            fileOk = true;
        }

        rAttributes.addFlashAttribute("message", "Product added");
        rAttributes.addFlashAttribute("alertClass", "alert-success");
        String slug = product.getName().toLowerCase().replace(" ", "-");
        Product productExists = productRepository.findByName(product.getName());
        if (!fileOk) {
            rAttributes.addFlashAttribute("message", "Image must be a jpg or a png");
            rAttributes.addFlashAttribute("alertClass", "alert-danger");
            rAttributes.addFlashAttribute("product", product);
        } else {
            if (productExists != null) {
                rAttributes.addFlashAttribute("message", "Product exists.Add another");
                rAttributes.addFlashAttribute("alertClass", "alert-danger");
                rAttributes.addFlashAttribute("product", product);
            } else {
                product.setSlug(slug);
                product.setImage(filename);
                productRepository.save(product);
                Files.write(path, bytes);
            }
        }
        return "redirect:/admin/products/add";

    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable int id, Model model) {
        Product product = productRepository.getOne(id);
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "admin/products/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Product product, BindingResult bindingresult, MultipartFile file,
            RedirectAttributes rAttributes, Model model) throws IOException {
       
       Product cproduct=productRepository.getOne(product.getId());
       
                List<Category> categories = categoryRepository.findAll();
        if (bindingresult.hasErrors()) {
            model.addAttribute("categories", categories);
            model.addAttribute("productName", cproduct.getName());
            model.addAttribute("productImage", cproduct.getImage());
            return "admin/products/edit";
        }

        boolean fileOk = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        
        Path path = Paths.get("src/main/resources/static/media/" + filename);
        
        
        if(!file.isEmpty()){
        if (filename.endsWith("jpg") || filename.endsWith("png")) {
            fileOk = true;
        }
    }
        else{
            fileOk = true;

        }
    

        rAttributes.addFlashAttribute("message", "Product edited");
        rAttributes.addFlashAttribute("alertClass", "alert-success");
        String slug = product.getName().toLowerCase().replace(" ", "-");
        Product productExists = productRepository.findBySlugAndIdNot(slug , product.getId());
        
        if (!fileOk) {
            rAttributes.addFlashAttribute("message", "Image must be a jpg or a png");
            rAttributes.addFlashAttribute("alertClass", "alert-danger");
            rAttributes.addFlashAttribute("product", product);
        } else {
            if (productExists != null) {
                rAttributes.addFlashAttribute("message", "Product exists.Add another");
                rAttributes.addFlashAttribute("alertClass", "alert-danger");
                rAttributes.addFlashAttribute("product", product);
            } else {
                product.setSlug(slug);
                if(!file.isEmpty())
                {
                    Path path2 = Paths.get("src/main/resources/static/media/" + cproduct.getImage());
                    Files.delete(path2);
                    product.setImage(filename);
                    Files.write(path, bytes);
                }
                else
                {
                    product.setImage(cproduct.getImage());
                }


               
                productRepository.save(product);
               
            }
        }
        return "redirect:/admin/products/edit/"+product.getId();

    }

    @GetMapping("/delete/{id}")
    public String deletePage(@PathVariable int id, RedirectAttributes rAttributes) throws IOException {
        Product product = productRepository.getOne(id);
        Product cproduct=productRepository.getOne(product.getId());
        Path path2 = Paths.get("src/main/resources/static/media/" + cproduct.getImage());
        Files.delete(path2);
        productRepository.deleteById(id);
      
        rAttributes.addFlashAttribute("message", "Product Deleted");
        rAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/admin/products";
    }
}