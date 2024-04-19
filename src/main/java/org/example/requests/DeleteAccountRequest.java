package org.example.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class DeleteAccountRequest {
    String userId;
    String password;
}
