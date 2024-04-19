package org.example.services.impl;


import org.example.dtos.ConversationDTO;
import org.example.dtos.ResponseDTO;
import org.example.entities.ConversationEntity;
import org.example.entities.IndividualConversationEntity;
import org.example.entities.MessageEntity;
import org.example.entities.UserEntity;
import org.example.mappers.ConversationMapper;
import org.example.mappers.MessageMapper;
import org.example.mappers.UserMapper;
import org.example.repositories.*;
import org.example.services.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private ConversationReadStatusRepository conversationReadStatusRepo;
    @Autowired
    private ConversationMapper conversationMapper;
    @Autowired
    private MessageMapper messageMapper;

    @Transactional(readOnly = true)
    @Override
    public ResponseDTO<List<ConversationDTO>> findAllByParticipantId(String participantId) {

        Optional<UserEntity> optionalUser = this.userRepo.findById(participantId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }

        UserEntity userEntity = optionalUser.get();

        List<ConversationEntity> conversationEntities = this.conversationRepo.findByParticipantId(participantId);

        List<ConversationDTO> conversationDTOs = this.conversationMapper.toDTO(conversationEntities);

        for (int i = 0; i < conversationDTOs.size(); i++) {
            ConversationEntity conversationEntity = conversationEntities.get(i);
            ConversationDTO conversationDTO = conversationDTOs.get(i);

            boolean exist = this.conversationReadStatusRepo.existByUserAndConversation(userEntity, conversationEntity);

            conversationDTO.setLastestMessage(this.messageMapper.toDTO(conversationEntity.getLastestMessage()));
            if (conversationEntity instanceof IndividualConversationEntity) {
                conversationDTO.setParticipants(conversationEntity.getParticipants().stream()
                        .map(UserMapper.INSTANCE::toDTO)
                        .toList());
            }
            conversationDTO.setHasNewMessage(exist);

        }

        return ResponseDTO.<List<ConversationDTO>>builder()
                .data(conversationDTOs)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseDTO<ConversationDTO> findOneByConversationIdAndParticipantId(String conversationId, String participantId) {
        Optional<ConversationEntity> optionalConversation = this.conversationRepo.findById(conversationId);

        if (optionalConversation.isEmpty()) {
            throw new RuntimeException("Conversation does not exist");
        }

        Optional<UserEntity> optionalUser = this.userRepo.findById(participantId);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }

        ConversationEntity conversationEntity = optionalConversation.get();
        UserEntity userEntity = optionalUser.get();

        if (!conversationEntity.getParticipants().contains(userEntity)) {
            throw new RuntimeException("User is not a participant of the conversation");
        }

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "sentAt"));
        Page<MessageEntity> messagePage = this.messageRepo.findByConversation(conversationEntity, pageable);


        ConversationDTO conversationDTO = this.conversationMapper.toDTO(conversationEntity);
        conversationDTO.setMessages(this.messageMapper.toDTO(messagePage.getContent()));
        conversationDTO.setParticipants(conversationEntity.getParticipants().stream()
                .map(UserMapper.INSTANCE::toDTO)
                .toList());

        return ResponseDTO.<ConversationDTO>builder()
                .data(conversationDTO)
                .build();

    }
}
