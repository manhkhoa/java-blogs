package com.example.javablogs.controller;

import com.example.javablogs.entity.BlogPost;
import com.example.javablogs.entity.User;
import com.example.javablogs.service.BlogPostService;
import com.example.javablogs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BlogPostService blogPostService;
    
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<User> users = userService.getAllUsers();
        List<BlogPost> blogPosts = blogPostService.getAllBlogPosts();
        
        model.addAttribute("users", users);
        model.addAttribute("blogPosts", blogPosts);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("totalPosts", blogPosts.size());
        
        return "admin/dashboard";
    }
    
    // User Management
    @GetMapping("/users")
    public String userList(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }
    
    @GetMapping("/users/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user-form";
    }
    
    @PostMapping("/users")
    public String createUser(@ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:/admin/users";
    }
    
    @GetMapping("/users/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "admin/user-form";
    }
    
    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/admin/users";
    }
    
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
    
    // Blog Post Management
    @GetMapping("/posts")
    public String postList(Model model) {
        List<BlogPost> blogPosts = blogPostService.getAllBlogPosts();
        model.addAttribute("blogPosts", blogPosts);
        return "admin/posts";
    }
    
    @GetMapping("/posts/new")
    public String newPostForm(Model model) {
        model.addAttribute("blogPost", new BlogPost());
        return "admin/post-form";
    }
    
    @PostMapping("/posts")
    public String createPost(@ModelAttribute BlogPost blogPost) {
        // For admin, we'll set a default author or get from session
        // This is simplified - in real app, get current user from security context
        blogPostService.createBlogPost(blogPost, null);
        return "redirect:/admin/posts";
    }
    
    @GetMapping("/posts/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        BlogPost blogPost = blogPostService.getBlogPostById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        model.addAttribute("blogPost", blogPost);
        return "admin/post-form";
    }
    
    @PostMapping("/posts/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute BlogPost blogPost) {
        blogPostService.updateBlogPost(id, blogPost);
        return "redirect:/admin/posts";
    }
    
    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        blogPostService.deleteBlogPost(id);
        return "redirect:/admin/posts";
    }
}
