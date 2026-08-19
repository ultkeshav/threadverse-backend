package com.threadverse_backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;

    private String type;
    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String role;
}