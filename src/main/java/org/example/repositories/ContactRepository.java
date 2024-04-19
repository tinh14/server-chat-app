package org.example.repositories;

import org.example.entities.ContactEntity;
import org.example.entities.ContactEntity.ContactStatus;
import org.example.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, String> {
    @Query("SELECT c FROM ContactEntity c WHERE (c.sender.id = :userId or c.receiver.id = :userId) " +
            "and c.status = :status")
    List<ContactEntity> findAllByUserId(@Param("userId") String userId, ContactStatus status);

    @Query("SELECT c FROM ContactEntity c WHERE c.receiver.id = :receiverId and c.status = :status")
    List<ContactEntity> findReceivedContactRequests(
            @Param("receiverId") String receiverId, @Param("status") ContactStatus status);

    @Query("SELECT c FROM ContactEntity c WHERE c.sender.id = :senderId and c.status = :status")
    List<ContactEntity> findSentContactRequests
            (@Param("senderId") String senderId, @Param("status") ContactStatus status);

    @Query("SELECT c FROM ContactEntity c WHERE (c.sender = :sender and c.receiver = :receiver) " +
            "or (c.sender = :receiver and c.receiver = :sender)")
    Optional<ContactEntity> findOneBySenderAndReceiver
            (@Param("sender") UserEntity sender, @Param("receiver") UserEntity receiver);

    void deleteBySenderOrReceiver(UserEntity foundUser, UserEntity foundUser1);
}
