package org.example.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserDTO {
    private String id;
    private String name;
    private String avatarCode;
}
