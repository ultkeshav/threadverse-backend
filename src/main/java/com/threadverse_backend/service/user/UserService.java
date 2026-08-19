package com.threadverse_backend.service.user;

import com.threadverse_backend.dto.request.UpdateProfileRequest;
import com.threadverse_backend.dto.response.UserProfileResponse;

public interface UserService {

    UserProfileResponse getProfile(String email);

    UserProfileResponse updateProfile(
            String currentEmail,
            UpdateProfileRequest request
    );
}