package org.example.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Inheritance
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@EqualsAndHashCode
public class ConversationEntity {

    @Id
    @Column(length = 36)
    private String id;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "conversation_lastest_message_id")
    private MessageEntity lastestMessage;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MessageEntity> messages;

    @ManyToMany
    @JoinTable(name = "conversation_participant_entity",
            joinColumns = @JoinColumn(name = "conversation_participant_conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "conversation_participant_participant_id"))
    private List<UserEntity> participants;

}