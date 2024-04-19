package org.example.services.impl;

import org.example.dtos.MessageDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.TextMessageDTO;
import org.example.entities.*;
import org.example.mappers.MessageMapper;
import org.example.repositories.ConversationReadStatusRepository;
import org.example.repositories.ConversationRepository;
import org.example.repositories.MessageRepository;
import org.example.repositories.UserRepository;
import org.example.services.TextMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TextMessageServiceImpl implements TextMessageService {

    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private ConversationRepository conversationRepo;
    @Autowired
    private ConversationReadStatusRepository conversationReadStatusRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public ResponseDTO<MessageDTO> create(TextMessageDTO messageDTO) {

        if (messageDTO.getContent().length() > 1024) {
            throw new RuntimeException("Message must be less than 1024 characters");
        }

        String messageId = UUID.randomUUID().toString();

        Optional<MessageEntity> optionalMessage = messageRepo.findById(messageId);

        if (optionalMessage.isPresent()) {
            throw new RuntimeException("Message already exists");
        }

        Optional<ConversationEntity> optionalConversation = conversationRepo.findById(messageDTO.getConversationId());

        if (optionalConversation.isEmpty()) {
            throw new RuntimeException("Conversation not found");
        }

        Optional<UserEntity> optionalUser = userRepo.findById(messageDTO.getSender().getId());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        ConversationEntity conversation = optionalConversation.get();
        UserEntity user = optionalUser.get();
        List<ConversationReadStatusEntity> conversationReadStatusEntities =
                this.conversationReadStatusRepo.findByConversation(conversation);



        TextMessageEntity message = TextMessageEntity.builder()
                .id(messageId)
                .sender(user)
                .conversation(conversation)
                .content(messageDTO.getContent())
                .build();

        try {
            message = this.messageRepo.saveAndFlush(message);

            conversation.setLastestMessage(message);
            conversationRepo.save(conversation);

            conversationReadStatusEntities.forEach(c -> {
                c.setRead(c.getUser().equals(user));
                this.conversationReadStatusRepo.save(c);
            });

            return ResponseDTO.<MessageDTO>builder()
                    .data(messageMapper.toDTO(message))
                    .build();
        }catch (Exception e) {
            throw new RuntimeException("Cannot create message");
        }

    }
}
