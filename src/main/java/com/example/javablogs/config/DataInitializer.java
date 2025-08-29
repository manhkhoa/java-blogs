package com.example.javablogs.config;

import com.example.javablogs.entity.Role;
import com.example.javablogs.entity.User;
import com.example.javablogs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if admin user exists, if not create one
        if (!userService.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@javablogs.com");
            admin.setPassword("admin123");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setRole(Role.ADMIN);
            
            userService.createUser(admin);
            System.out.println("Default admin user created: admin/admin123");
        }
        
        // Check if demo user exists, if not create one
        if (!userService.existsByUsername("demo")) {
            User demoUser = new User();
            demoUser.setUsername("demo");
            demoUser.setEmail("demo@javablogs.com");
            demoUser.setPassword("demo123");
            demoUser.setFirstName("Demo");
            demoUser.setLastName("User");
            demoUser.setRole(Role.USER);
            
            userService.createUser(demoUser);
            System.out.println("Default demo user created: demo/demo123");
        }
    }
}

