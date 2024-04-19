package org.example.entities;



import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Inheritance
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class MessageEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, updatable = false, name = "sentAt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP" )
    private Timestamp sentAt;

    @ManyToOne
    @JoinColumn(name = "message_sender_id")
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "message_conversation_id")
    private ConversationEntity conversation;

    @PrePersist
    public void setCreatedAt() {
        this.sentAt = new Timestamp(System.currentTimeMillis());
    }

//    @OneToMany(mappedBy = "message", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    private List<MessageReadStatusEntity> readStatuses;

}
