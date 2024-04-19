package org.example.services;

import org.example.dtos.MessageDTO;
import org.example.dtos.ResponseDTO;

import java.util.List;

public interface MessageService {
    ResponseDTO<List<MessageDTO>> findByConversationId(String conversationId, int page);
}
