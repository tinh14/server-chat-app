package org.example.services.impl;

import org.example.dtos.ResponseDTO;
import org.example.entities.ConversationEntity;
import org.example.entities.ConversationReadStatusEntity;
import org.example.entities.UserEntity;
import org.example.repositories.ConversationReadStatusRepository;
import org.example.repositories.ConversationRepository;
import org.example.repositories.UserRepository;
import org.example.services.ConversationReadStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConversationReadStatusServiceImpl implements ConversationReadStatusService {

    @Autowired
    private ConversationReadStatusRepository conversationReadStatusRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ConversationRepository conversationRepo;

    @Override
    public ResponseDTO<Void> markNewMessagesAsRead(String userId, String conversationId) throws Exception {
        Optional<UserEntity> optionalUser = this.userRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found");
        }
        Optional<ConversationEntity> optionalConversation = this.conversationRepo.findById(conversationId);

        if (optionalConversation.isEmpty()) {
            throw new Exception("Conversation not found");
        }

        UserEntity user = optionalUser.get();
        ConversationEntity conversation = optionalConversation.get();

        Optional<ConversationReadStatusEntity> optionalConversationReadStatus =
                this.conversationReadStatusRepo.findByUserAndConversation(user, conversation);

        if (optionalConversationReadStatus.isEmpty()) {
            throw new Exception("Conversation read status not found");
        }
        ConversationReadStatusEntity conversationReadStatus = optionalConversationReadStatus.get();
        conversationReadStatus.setRead(true);

        try {
            this.conversationReadStatusRepo.save(conversationReadStatus);
            return ResponseDTO.<Void>builder().build();
        }catch (Exception e) {
            throw new Exception("Failed to mark new messages as read");
        }
    }
}
