package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class TextMessageEntity extends MessageEntity {
    @Column(name = "message_content", length = 1024)
    private String content;
}
