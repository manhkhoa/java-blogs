package com.example.javablogs.controller;

import com.example.javablogs.entity.BlogPost;
import com.example.javablogs.entity.User;
import com.example.javablogs.service.BlogPostService;
import com.example.javablogs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String createUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("success", "User created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating user: " + e.getMessage());
        }
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
    public String updateUser(@PathVariable Long id, @ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, user);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
    
    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting user: " + e.getMessage());
        }
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
    public String createPost(@ModelAttribute BlogPost blogPost, RedirectAttributes redirectAttributes) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User currentUser = userService.getUserByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Current user not found"));
            
            blogPostService.createBlogPost(blogPost, currentUser);
            redirectAttributes.addFlashAttribute("success", "Blog post created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating blog post: " + e.getMessage());
        }
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
    public String updatePost(@PathVariable Long id, @ModelAttribute BlogPost blogPost, RedirectAttributes redirectAttributes) {
        try {
            blogPostService.updateBlogPost(id, blogPost);
            redirectAttributes.addFlashAttribute("success", "Blog post updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating blog post: " + e.getMessage());
        }
        return "redirect:/admin/posts";
    }
    
    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            blogPostService.deleteBlogPost(id);
            redirectAttributes.addFlashAttribute("success", "Blog post deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting blog post: " + e.getMessage());
        }
        return "redirect:/admin/posts";
    }
}
