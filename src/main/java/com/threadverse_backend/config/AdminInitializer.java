package com.threadverse_backend.config;

import com.threadverse_backend.entity.User;
import com.threadverse_backend.enums.Role;
import com.threadverse_backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL:}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Value("${ADMIN_FIRST_NAME:Admin}")
    private String adminFirstName;

    @Value("${ADMIN_LAST_NAME:User}")
    private String adminLastName;

    @Override
    public void run(String... args) {

        if (adminEmail == null || adminEmail.isBlank()
                || adminPassword == null || adminPassword.isBlank()) {

            System.out.println("Admin environment variables not configured.");
            return;
        }

        if (userRepository.existsByEmail(adminEmail)) {
            System.out.println("Admin already exists: " + adminEmail);
            return;
        }

        User admin = User.builder()
                .firstName(adminFirstName)
                .lastName(adminLastName)
                .email(adminEmail)
                .passwordHash(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);

        System.out.println("Admin user created: " + adminEmail);
    }
}