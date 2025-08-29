-- Java Blogs Database Initialization Script

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS javablogs;
USE javablogs;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create blog_posts table
CREATE TABLE IF NOT EXISTS blog_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    author_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_published BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert default admin user (password: admin123)
INSERT INTO users (username, email, password, first_name, last_name, role) 
VALUES ('admin', 'admin@javablogs.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Admin', 'User', 'ADMIN')
ON DUPLICATE KEY UPDATE username=username;

-- Insert default demo user (password: demo123)
INSERT INTO users (username, email, password, first_name, last_name, role) 
VALUES ('demo', 'demo@javablogs.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Demo', 'User', 'USER')
ON DUPLICATE KEY UPDATE username=username;

-- Insert sample blog posts
INSERT INTO blog_posts (title, content, author_id, is_published) VALUES
('Welcome to Java Blogs', 'This is your first blog post. Welcome to the Java Blogs platform built with Spring Boot and MySQL!', 1, TRUE),
('Getting Started with Spring Boot', 'Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".', 1, TRUE),
('MySQL Best Practices', 'Learn about database design, indexing, and optimization techniques for MySQL.', 2, TRUE)
ON DUPLICATE KEY UPDATE title=title;

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_blog_posts_author ON blog_posts(author_id);
CREATE INDEX IF NOT EXISTS idx_blog_posts_created_at ON blog_posts(created_at);
CREATE INDEX IF NOT EXISTS idx_blog_posts_published ON blog_posts(is_published);
