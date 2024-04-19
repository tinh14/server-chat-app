package org.example.services;


import org.example.dtos.ResponseDTO;

public interface ConversationReadStatusService {

    ResponseDTO<Void> markNewMessagesAsRead(String userId, String conversationId) throws Exception;
}
