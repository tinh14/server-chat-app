package org.example.repositories;

import org.example.entities.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, String> {
    @Query("SELECT c FROM ConversationEntity c " +
            "LEFT JOIN c.lastestMessage lm " +
            "JOIN c.participants p " +
            "WHERE p.id = :participantId " +
            "ORDER BY lm.sentAt DESC")
    List<ConversationEntity> findByParticipantId(@Param("participantId") String participantId);
}
