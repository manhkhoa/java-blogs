package com.example.javablogs.service;

import com.example.javablogs.entity.BlogPost;
import com.example.javablogs.entity.User;
import com.example.javablogs.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {
    
    @Autowired
    private BlogPostRepository blogPostRepository;
    
    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.findAll();
    }
    
    public Page<BlogPost> getPublishedBlogPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return blogPostRepository.findByPublishedTrueOrderByCreatedAtDesc(pageable);
    }
    
    public List<BlogPost> getBlogPostsByAuthor(User author) {
        return blogPostRepository.findByAuthorOrderByCreatedAtDesc(author);
    }
    
    public List<BlogPost> getPublishedBlogPostsByAuthor(User author) {
        return blogPostRepository.findByAuthorAndPublishedTrueOrderByCreatedAtDesc(author);
    }
    
    public Optional<BlogPost> getBlogPostById(Long id) {
        return blogPostRepository.findById(id);
    }
    
    public BlogPost createBlogPost(BlogPost blogPost, User author) {
        blogPost.setAuthor(author);
        return blogPostRepository.save(blogPost);
    }
    
    public BlogPost updateBlogPost(Long id, BlogPost blogPostDetails) {
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        
        blogPost.setTitle(blogPostDetails.getTitle());
        blogPost.setContent(blogPostDetails.getContent());
        blogPost.setPublished(blogPostDetails.isPublished());
        
        return blogPostRepository.save(blogPost);
    }
    
    public void deleteBlogPost(Long id) {
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));
        blogPostRepository.delete(blogPost);
    }
    
    public Page<BlogPost> searchPublishedPosts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return blogPostRepository.searchPublishedPosts(keyword, pageable);
    }
    
    public List<BlogPost> searchPostsByAuthor(User author, String keyword) {
        return blogPostRepository.searchPostsByAuthor(author, keyword);
    }
    
    public boolean canEditPost(User user, BlogPost post) {
        return user.getRole().equals(com.example.javablogs.entity.Role.ADMIN) || 
               post.getAuthor().getId().equals(user.getId());
    }
    
    public boolean canDeletePost(User user, BlogPost post) {
        return user.getRole().equals(com.example.javablogs.entity.Role.ADMIN) || 
               post.getAuthor().getId().equals(user.getId());
    }
}
