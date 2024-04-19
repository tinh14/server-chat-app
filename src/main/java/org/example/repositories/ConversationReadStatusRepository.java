package org.example.repositories;

import org.example.entities.ConversationEntity;
import org.example.entities.ConversationReadStatusEntity;
import org.example.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationReadStatusRepository extends JpaRepository<ConversationReadStatusEntity, Long> {
    @Query("SELECT COUNT(crs) > 0 " +
            "FROM ConversationReadStatusEntity crs " +
            "WHERE crs.user = :user and crs.conversation = :conversation and crs.read = false")
    boolean existByUserAndConversation(
            @Param("user") UserEntity user,
            @Param("conversation") ConversationEntity conversation);

    List<ConversationReadStatusEntity> findByConversation(ConversationEntity conversation);

    Optional<ConversationReadStatusEntity> findByUserAndConversation(UserEntity foundUser, ConversationEntity foundConversation);
}
