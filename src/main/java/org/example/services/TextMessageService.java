package org.example.services;

import org.example.dtos.MessageDTO;
import org.example.dtos.ResponseDTO;
import org.example.dtos.TextMessageDTO;

public interface TextMessageService {
    ResponseDTO<MessageDTO> create(TextMessageDTO messageDTO) throws Exception;
}
