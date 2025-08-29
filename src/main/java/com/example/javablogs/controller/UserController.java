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
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private BlogPostService blogPostService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/dashboard")
    public String userDashboard(Model model) {
        // In real app, get current user from security context
        // For now, we'll use a placeholder
        User currentUser = new User(); // This should come from security context
        currentUser.setId(1L); // Placeholder
        
        List<BlogPost> userPosts = blogPostService.getBlogPostsByAuthor(currentUser);
        model.addAttribute("blogPosts", userPosts);
        model.addAttribute("user", currentUser);
        
        return "user/dashboard";
    }
    
    @GetMapping("/posts")
    public String myPosts(Model model) {
        // In real app, get current user from security context
        User currentUser = new User(); // This should come from security context
        currentUser.setId(1L); // Placeholder
        
        List<BlogPost> userPosts = blogPostService.getBlogPostsByAuthor(currentUser);
        model.addAttribute("blogPosts", userPosts);
        
        return "user/posts";
    }
    
    @GetMapping("/posts/new")
    public String newPostForm(Model model) {
        model.addAttribute("blogPost", new BlogPost());
        return "user/post-form";
    }
    
    @PostMapping("/posts")
    public String createPost(@ModelAttribute BlogPost blogPost) {
        // In real app, get current user from security context
        User currentUser = new User(); // This should come from security context
        currentUser.setId(1L); // Placeholder
        
        blogPostService.createBlogPost(blogPost, currentUser);
        return "redirect:/user/posts";
    }
    
    @GetMapping("/posts/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        BlogPost blogPost = blogPostService.getBlogPostById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        
        // In real app, check if current user can edit this post
        User currentUser = new User(); // This should come from security context
        currentUser.setId(1L); // Placeholder
        
        if (!blogPostService.canEditPost(currentUser, blogPost)) {
            throw new RuntimeException("Access denied");
        }
        
        model.addAttribute("blogPost", blogPost);
        return "user/post-form";
    }
    
    @PostMapping("/posts/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute BlogPost blogPost) {
        BlogPost existingPost = blogPostService.getBlogPostById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        
        // In real app, check if current user can edit this post
        User currentUser = new User(); // This should come from security context
        currentUser.setId(1L); // Placeholder
        
        if (!blogPostService.canEditPost(currentUser, existingPost)) {
            throw new RuntimeException("Access denied");
        }
        
        blogPostService.updateBlogPost(id, blogPost);
        return "redirect:/user/posts";
    }
    
    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        BlogPost existingPost = blogPostService.getBlogPostById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        
        // In real app, check if current user can delete this post
        User currentUser = new User(); // This should come from security context
        currentUser.setId(1L); // Placeholder
        
        if (!blogPostService.canDeletePost(currentUser, existingPost)) {
            throw new RuntimeException("Access denied");
        }
        
        blogPostService.deleteBlogPost(id);
        return "redirect:/user/posts";
    }
    
    @GetMapping("/profile")
    public String profile(Model model) {
        // In real app, get current user from security context
        User currentUser = new User(); // This should come from security context
        currentUser.setId(1L); // Placeholder
        
        User user = userService.getUserById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("user", user);
        return "user/profile";
    }
    
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user) {
        // In real app, get current user from security context
        User currentUser = new User(); // This should come from security context
        currentUser.setId(1L); // Placeholder
        
        userService.updateUser(currentUser.getId(), user);
        return "redirect:/user/profile";
    }
}
