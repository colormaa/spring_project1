package com.shop.shop.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.shop.models.CategoryRepository;
import com.shop.shop.models.data.Category;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoriesController {
    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model) {
        List<Category> categories = categoryRepo.findAllByOrderBySortingAsc();
        model.addAttribute("categories", categories);
        return "admin/categories/index";
    }

    @ModelAttribute("category")
    public Category getCategory() {
        return new Category();
    }

    @GetMapping("/add")
    // public String add(@ModelAttribute Category category)
    public String add() {
        return "admin/categories/add";
    }

    @PostMapping("/add")
    public String add(@Valid Category category, BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/categories/add";
        }
        redirectAttributes.addFlashAttribute("message", "Category added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        String slug = category.getName().toLowerCase().replace(" ", "-");
        Category categoryExists = categoryRepo.findByName(slug);
        if (categoryExists != null) {
            redirectAttributes.addFlashAttribute("message",
                    "category exists, choose another name");
            redirectAttributes.addFlashAttribute("alertClass",
                    "alert-danger");
            redirectAttributes.addFlashAttribute("categoryInfo", category);
        } else {
            category.setSlug(slug);
            category.setSorting(100);
            categoryRepo.save(category);
        }
        return "redirect:/admin/categories/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Category category = categoryRepo.getReferenceById(id);
        model.addAttribute("category", category);
        return "/admin/categories/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Category category, BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        Category categoryCurrent = categoryRepo.getReferenceById(category.getId());
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryName", categoryCurrent.getName());
            return "admin/categories/edit";
        }
        redirectAttributes.addFlashAttribute("message",
                "Category edited");
        redirectAttributes.addFlashAttribute("alertClass",
                "alert-success");
        String slug = category.getName().toLowerCase().replace(" ", "-");

        Category categoryExists = categoryRepo.findByName(category.getName());
        if (categoryExists != null) {
            redirectAttributes.addFlashAttribute("message",
                    "Category exists, choose another name");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            // redirectAttributes.addFlashAttribute("categoryInfo", category);
        } else {
            category.setSlug(slug);
            category.setSorting(100);
            categoryRepo.save(category);
        }
        return "redirect:/admin/categories/edit/" + category.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
        categoryRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Category deleted successfully");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/categories";
    }

    @PostMapping("/reorder")
    public @ResponseBody String reorder(@RequestParam("id[]") int[] id) {
        int count = 1;
        Category category;
        for (int categoryId : id) {
            category = categoryRepo.getReferenceById(categoryId);
            category.setSorting(count);
            categoryRepo.save(category);
            count++;
        }
        return "ok";
    }
}
