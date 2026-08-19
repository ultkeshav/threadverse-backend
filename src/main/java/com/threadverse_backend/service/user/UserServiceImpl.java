package com.threadverse_backend.service.user;

import com.threadverse_backend.dto.request.UpdateProfileRequest;
import com.threadverse_backend.dto.response.UserProfileResponse;
import com.threadverse_backend.entity.User;
import com.threadverse_backend.exception.BadRequestException;
import com.threadverse_backend.exception.ResourceNotFoundException;
import com.threadverse_backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return toResponse(user);
    }

    @Override
    public UserProfileResponse updateProfile(
            String currentEmail,
            UpdateProfileRequest request) {

        User user = userRepository
                .findByEmail(currentEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        if (!user.getEmail()
                .equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmail(
                request.getEmail())) {

            throw new BadRequestException(
                    "Email already exists"
            );
        }

        if (request.getPhone() != null
                && !request.getPhone()
                .equals(user.getPhone())
                && userRepository.existsByPhone(
                request.getPhone())) {

            throw new BadRequestException(
                    "Phone number already exists"
            );
        }

        user.setFirstName(
                request.getFirstName()
        );

        user.setLastName(
                request.getLastName()
        );

        user.setEmail(
                request.getEmail()
        );

        user.setPhone(
                request.getPhone()
        );

        User updatedUser =
                userRepository.save(user);

        return toResponse(updatedUser);
    }

    private UserProfileResponse toResponse(
            User user) {

        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .build();
    }
}