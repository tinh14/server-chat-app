package org.example.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Entity
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class IndividualConversationEntity extends ConversationEntity {
}
