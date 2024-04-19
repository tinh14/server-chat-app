package org.example.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MessageReadStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_read_status_id")
    private Long id;

    @Column(name = "message_read_status_read")
    private Boolean read;

    @ManyToOne
    @JoinColumn(name = "message_read_status_user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "message_read_status_message_id")
    private MessageEntity message;
}
