package com.project.shoppingcart.Controller;

import java.util.List;

import javax.validation.Valid;

import com.project.shoppingcart.model.PageRepository;
import com.project.shoppingcart.model.data.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pages")
public class AdminController {
    @Autowired
    PageRepository pageRepository;

    @GetMapping
    public String getpages(Model model) {
        List<pages> pages = pageRepository.findAllByOrderBySortingAsc();
        model.addAttribute("pages", pages);

        return "admin/pages/index";
    }

    @GetMapping("/add")
    public String add(Model model)
    {
        model.addAttribute("page", new pages());
        return "admin/pages/add";
    }

    @PostMapping("/add")
    public String add(@Valid pages page, BindingResult bindingresult, RedirectAttributes rAttributes, Model model) {
        if (bindingresult.hasErrors()) {
            return "admin/pages/add";
        }
        rAttributes.addFlashAttribute("message", "Page added");
        rAttributes.addFlashAttribute("alertClass", "alert-success");
        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-")
                : page.getSlug().toLowerCase().replace(" ", "-");
        pages slugExist = pageRepository.findBySlug(slug);
        if (slugExist != null) {
            rAttributes.addFlashAttribute("message", "Slug exists.Add another");
            rAttributes.addFlashAttribute("alertClass", "alert-danger");
            rAttributes.addFlashAttribute("page", page);
        } else {
            page.setSlug(slug);
            page.setSorting(100);
            pageRepository.save(page);
        }
        return "redirect:/admin/pages/add";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable int id, Model model) {
        pages pages = pageRepository.findById(id).get();
        model.addAttribute("page", pages);
        return "admin/pages/edit";
    }

    @PostMapping("/edit")
    public String editpage(@Valid pages page, BindingResult bindingresult, RedirectAttributes rAttributes,
            Model model) {
        if (bindingresult.hasErrors()) {
            return "admin/pages/edit";
        }
        rAttributes.addFlashAttribute("message", "Page edited");
        rAttributes.addFlashAttribute("alertClass", "alert-success");
        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-")
                : page.getSlug().toLowerCase().replace(" ", "-");
        pages slugExist = pageRepository.findBySlugAndIdNot(slug, page.getId());
        if (slugExist != null) {
            rAttributes.addFlashAttribute("message", "Slug exists.Add another");
            rAttributes.addFlashAttribute("alertClass", "alert-danger");
            rAttributes.addFlashAttribute("page", page);
        } else {
            page.setSlug(slug);

            pageRepository.save(page);
        }
        return "redirect:/admin/pages/edit/" + page.getId();
    }

    @GetMapping("/delete/{id}")
    public String deletePage(@PathVariable int id, RedirectAttributes rAttributes) {
        pageRepository.deleteById(id);
        rAttributes.addFlashAttribute("message", "Page Deleted");
        rAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/admin/pages";
    }

    @PostMapping("/reorder")
    public @ResponseBody String reorder(@RequestParam("id[]") int[] id) {

        int count = 1;
        pages page;

        for (int pageId : id) {
            page = pageRepository.getOne(pageId);
            page.setSorting(count);
            pageRepository.save(page);
            count++;
        }

        return "ok";
    }

}