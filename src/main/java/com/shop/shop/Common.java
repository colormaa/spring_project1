package com.shop.shop;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.shop.shop.models.Cart;
import com.shop.shop.models.CategoryRepository;
import com.shop.shop.models.PageRepository;
import com.shop.shop.models.data.Category;
import com.shop.shop.models.data.Page;

@ControllerAdvice
@SuppressWarnings("unchecked")
public class Common {
    @Autowired
    PageRepository pageRepo;

    @Autowired
    CategoryRepository categoryRepo;

    @ModelAttribute
    public void sharedData(Model model, HttpSession session, Principal principal) {
        if (principal != null) {
            model.addAttribute("principal", principal.getName());
        }

        List<Page> pages = pageRepo.findAllByOrderBySortingAsc();
        List<Category> categories = categoryRepo.findAllByOrderBySortingAsc();

        boolean cartActive = false;
        if (session.getAttribute("cart") != null) {
            HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
            int size = 0;
            double total = 0;
            for (Cart value : cart.values()) {
                size += value.getQuantity();
                total += value.getQuantity() * Double.parseDouble(value.getPrice());
            }
            model.addAttribute("ctotal", total);
            model.addAttribute("csize", size);
            cartActive = true;
        }

        model.addAttribute("cartActive", cartActive);

        model.addAttribute("cpages", pages);
        model.addAttribute("ccategories", categories);

    }

}
