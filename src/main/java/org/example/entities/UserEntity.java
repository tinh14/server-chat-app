package org.example.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserEntity {

    @Id
    @Column(name = "user_id", length = 36)
    private String id;

    @Column(name = "user_name", length = 50)
    private String name;

    @Column(name = "user_avatar_code")
    private String avatarCode;

    @ManyToMany(mappedBy = "participants")
    private List<ConversationEntity> conversations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<MessageReadStatusEntity> messageReadStatuses;
}
