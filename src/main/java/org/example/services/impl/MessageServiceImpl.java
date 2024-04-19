package org.example.services.impl;

import org.example.dtos.MessageDTO;
import org.example.dtos.ResponseDTO;
import org.example.entities.ConversationEntity;
import org.example.entities.MessageEntity;
import org.example.mappers.MessageMapper;
import org.example.repositories.ConversationRepository;
import org.example.repositories.MessageRepository;
import org.example.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private ConversationRepository conversationRepo;
    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private MessageMapper messageMapper;

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO<List<MessageDTO>> findByConversationId(String conversationId, int page) {
        Optional<ConversationEntity> optionalConversation = conversationRepo.findById(conversationId);
        if (optionalConversation.isEmpty()) {
            throw new RuntimeException("Conversation not found");
        }
        ConversationEntity conversation = optionalConversation.get();
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "sentAt"));
        Page<MessageEntity> messagePage = messageRepo.findByConversation(conversation, pageable);
        return ResponseDTO.<List<MessageDTO>>builder()
                .data(messageMapper.toDTO(messagePage.getContent()))
                .build();
    }
}
