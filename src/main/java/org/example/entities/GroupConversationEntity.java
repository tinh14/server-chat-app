package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class GroupConversationEntity extends ConversationEntity {

    @Column(name = "conversation_name", length = 50)
    private String name;

    @Column(name = "conversation_avatar_code")
    private String avatarCode;

    @ManyToOne
    @JoinColumn(name = "conversation_owned_by")
    private UserEntity ownedBy;
}
