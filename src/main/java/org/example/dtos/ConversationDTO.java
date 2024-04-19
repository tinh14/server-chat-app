package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class ConversationDTO {
    String id;
    MessageDTO lastestMessage;
    List<MessageDTO> messages;
    List<UserDTO> participants;
    String instanceOf;
    boolean hasNewMessage;
}
