package com.threadverse_backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String role;
}