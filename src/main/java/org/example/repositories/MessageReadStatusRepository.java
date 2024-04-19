package org.example.repositories;

import org.example.entities.ConversationEntity;
import org.example.entities.MessageReadStatusEntity;
import org.example.entities.MessageReadStatusEntity;
import org.example.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReadStatusRepository extends JpaRepository<MessageReadStatusEntity, Long> {

    @Query("SELECT COUNT(*) FROM MessageReadStatusEntity mrs " +
            "WHERE mrs.user = :user and mrs.message.conversation = :conversation and mrs.read = false")
    int countUnreadMessagesByUserAndConversation(
            @Param("user") UserEntity user,
            @Param("conversation") ConversationEntity conversation);

}
