package com.project.shoppingcart.Controller;

import java.util.List;

import javax.validation.Valid;

import com.project.shoppingcart.model.CategoryRepository;
import com.project.shoppingcart.model.data.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoriesController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String index(Model model) {
        List<Category> categories = categoryRepository.findAllByOrderBySortingAsc();
        model.addAttribute("categories", categories);
        return "/admin/categories/index";

    }

    @GetMapping("/add")
    public String add(Category category) {
        //model.addAttribute("category", new Category());
        return "admin/categories/add";

    }

    @PostMapping("/add")
    public String addCategory(@Valid Category category, BindingResult bindingresult, RedirectAttributes rAttributes,
            Model model) {
        Category currentcategory = categoryRepository.getOne(category.getId());
        if (bindingresult.hasErrors()) {
            model.addAttribute("category", currentcategory.getName());
            return "admin/categories/add";
        }
        rAttributes.addFlashAttribute("message", "Category added");
        rAttributes.addFlashAttribute("alertClass", "alert-success");
        String slug = category.getName().toLowerCase().replace(" ", "-");
        Category nameExists = categoryRepository.findByName(category.getName());
        if (nameExists != null) {
            rAttributes.addFlashAttribute("message", "Category exists.Add another");
            rAttributes.addFlashAttribute("alertClass", "alert-danger");
            rAttributes.addFlashAttribute("categoryInfo", category);
        } else {
            category.setSlug(slug);
            category.setSorting(100);
            categoryRepository.save(category);
        }
        return "redirect:/admin/categories/add";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable int id, Model model) {
        Category category = categoryRepository.findById(id).get();
        model.addAttribute("category", category);
        return "admin/categories/edit";
    }

    @PostMapping("/edit")
    public String editCategory(@Valid Category category, BindingResult bindingresult, RedirectAttributes rAttributes,
            Model model) {
        Category currentcategory = categoryRepository.getOne(category.getId());
        if (bindingresult.hasErrors()) {
            model.addAttribute("categoryname", currentcategory.getName());
            return "admin/categories/edit";
        }

        Category nameExists = categoryRepository.findByName(category.getName());
        if (nameExists != null) {
            rAttributes.addFlashAttribute("message", "Category exists.Add another");
            rAttributes.addFlashAttribute("alertClass", "alert-danger");
            rAttributes.addFlashAttribute("category", category);
        } else {
            rAttributes.addFlashAttribute("message", "Category edited");
            rAttributes.addFlashAttribute("alertClass", "alert-success");
            String slug = category.getName().toLowerCase().replace(" ", "-");
            category.setSlug(slug);

            categoryRepository.save(category);
        }
        return "redirect:/admin/categories/edit/" + category.getId();
    }

    @GetMapping("/delete/{id}")
    public String deletePage(@PathVariable int id, RedirectAttributes rAttributes) {
        categoryRepository.deleteById(id);
        rAttributes.addFlashAttribute("message", "Category Deleted");
        rAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/admin/categories";
    }

    

}