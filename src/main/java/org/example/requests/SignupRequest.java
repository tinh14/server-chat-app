package org.example.requests;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SignupRequest {
    private String username;
    private String password;
    private String confirmPassword;
}
