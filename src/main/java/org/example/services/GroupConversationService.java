package org.example.services;

import org.example.dtos.ConversationDTO;
import org.example.dtos.GroupConversationDTO;
import org.example.dtos.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface GroupConversationService {
    ResponseDTO<ConversationDTO> create(GroupConversationDTO groupConversationDTO, MultipartFile avatar) throws Exception;
    ResponseDTO<String> update(GroupConversationDTO groupConversationDTO, MultipartFile avatar) throws Exception;

    ResponseDTO<ConversationDTO> joinConversation(String conversationId, String joinerId) throws Exception;

    ResponseDTO<Void> leaveConversation(String conversationId, String leaverId) throws Exception;
}
