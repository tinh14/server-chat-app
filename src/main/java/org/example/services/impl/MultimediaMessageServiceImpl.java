package org.example.services.impl;

import org.example.dtos.MessageDTO;
import org.example.dtos.MultimediaMessageDTO;
import org.example.dtos.ResponseDTO;
import org.example.entities.*;
import org.example.entities.MultimediaMessageEntity.MultimediaMessageType;
import org.example.mappers.MessageMapper;
import org.example.repositories.ConversationRepository;
import org.example.repositories.MessageRepository;
import org.example.repositories.UserRepository;
import org.example.services.MultimediaMessageService;
import org.example.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class MultimediaMessageServiceImpl implements MultimediaMessageService {
    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private ConversationRepository conversationRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public ResponseDTO<MessageDTO> create(MultimediaMessageDTO multimediaMessageDTO, MultipartFile file) {

        if (file.getSize() > 1024 * 1024 * 300) {
            throw new RuntimeException("File must be less than 300MB");
        }

        String messageId = UUID.randomUUID().toString();

        Optional<MessageEntity> optionalMessage = messageRepo.findById(messageId);

        if (optionalMessage.isPresent()) {
            throw new RuntimeException("Message already exists");
        }

        Optional<ConversationEntity> optionalConversation =
                conversationRepo.findById(multimediaMessageDTO.getConversationId());

        if (optionalConversation.isEmpty()) {
            throw new RuntimeException("Conversation not found");
        }

        Optional<UserEntity> optionalUser = userRepo.findById(multimediaMessageDTO.getSender().getId());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        ConversationEntity conversation = optionalConversation.get();
        UserEntity user = optionalUser.get();

        try {
            String fileCode = FileUtils.getFileCode(file);

            MultimediaMessageEntity message = MultimediaMessageEntity.builder()
                    .id(messageId)
                    .sentAt(multimediaMessageDTO.getSentAt())
                    .sender(user)
                    .conversation(conversation)
                    .fileCode(fileCode)
                    .fileName(multimediaMessageDTO.getFileName())
                    .fileType(MultimediaMessageType.valueOf(multimediaMessageDTO.getFileType()))
                    .build();

            message = this.messageRepo.saveAndFlush(message);

            conversation.setLastestMessage(message);
            this.conversationRepo.save(conversation);

            FileUtils.storeFile(fileCode, file);

            return ResponseDTO.<MessageDTO>builder()
                    .data(messageMapper.toDTO(message))
                    .build();
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot create message");
        }

    }
}
