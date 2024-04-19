package org.example.services;

import org.example.dtos.ConversationDTO;
import org.example.dtos.ResponseDTO;

import java.util.List;

public interface ConversationService {
    ResponseDTO<List<ConversationDTO>> findAllByParticipantId(String participantId);
    ResponseDTO<ConversationDTO> findOneByConversationIdAndParticipantId(String conversationId, String participantId);

}
