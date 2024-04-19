package org.example.repositories;

import org.example.entities.ConversationEntity;
import org.example.entities.MessageEntity;
import org.example.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, String> {

    Page<MessageEntity> findByConversation(ConversationEntity conversation, Pageable pageable);

    void deleteBySender(UserEntity foundUser);
}
