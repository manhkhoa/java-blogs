package com.example.javablogs.repository;

import com.example.javablogs.entity.BlogPost;
import com.example.javablogs.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    
    Page<BlogPost> findByPublishedTrueOrderByCreatedAtDesc(Pageable pageable);
    
    List<BlogPost> findByAuthorOrderByCreatedAtDesc(User author);
    
    List<BlogPost> findByAuthorAndPublishedTrueOrderByCreatedAtDesc(User author);
    
    @Query("SELECT bp FROM BlogPost bp WHERE bp.published = true AND (bp.title LIKE %:keyword% OR bp.content LIKE %:keyword%)")
    Page<BlogPost> searchPublishedPosts(String keyword, Pageable pageable);
    
    @Query("SELECT bp FROM BlogPost bp WHERE bp.author = :author AND (bp.title LIKE %:keyword% OR bp.content LIKE %:keyword%)")
    List<BlogPost> searchPostsByAuthor(User author, String keyword);
}
