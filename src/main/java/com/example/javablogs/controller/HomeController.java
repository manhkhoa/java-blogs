package com.example.javablogs.controller;

import com.example.javablogs.entity.BlogPost;
import com.example.javablogs.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    
    @Autowired
    private BlogPostService blogPostService;
    
    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }
    
    @GetMapping("/home")
    public String homePage(Model model) {
        return "home";
    }
    
    @GetMapping("/blog")
    public String blogList(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String search,
                          Model model) {
        
        Page<BlogPost> blogPosts;
        if (search != null && !search.trim().isEmpty()) {
            blogPosts = blogPostService.searchPublishedPosts(search, page, size);
            model.addAttribute("search", search);
        } else {
            blogPosts = blogPostService.getPublishedBlogPosts(page, size);
        }
        
        model.addAttribute("blogPosts", blogPosts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", blogPosts.getTotalPages());
        model.addAttribute("totalItems", blogPosts.getTotalElements());
        
        return "blog/list";
    }
    
    @GetMapping("/blog/{id}")
    public String blogDetail(@RequestParam Long id, Model model) {
        BlogPost blogPost = blogPostService.getBlogPostById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        
        model.addAttribute("blogPost", blogPost);
        return "blog/detail";
    }
}
