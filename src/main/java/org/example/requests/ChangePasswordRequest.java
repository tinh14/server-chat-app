package org.example.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ChangePasswordRequest {
    private String userId;
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
