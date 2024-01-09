package com.gang.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private String token;
    private Long id;
    private String userId;
    private String role;
    private String password;
}
