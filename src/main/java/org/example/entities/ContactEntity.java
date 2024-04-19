package org.example.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Getter
public class ContactEntity {

    public enum ContactStatus {
        PENDING,
        ACCEPTED
    }

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "contact_sender_id")
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "contact_receiver_id")
    private UserEntity receiver;

    @Enumerated
    @Column(name = "contact_status")
    private ContactStatus status;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "contact_conversation_id")
    private IndividualConversationEntity conversation;
}
