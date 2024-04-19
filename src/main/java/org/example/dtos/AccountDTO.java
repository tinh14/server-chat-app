package org.example.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AccountDTO {
    String username;
    String password;
}
