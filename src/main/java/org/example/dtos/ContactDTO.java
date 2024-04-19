package org.example.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ContactDTO {
    private String id;
    private UserDTO sender;
    private UserDTO receiver;
    private String conversationId;
}
