package org.example.services;

import org.example.dtos.MessageDTO;
import org.example.dtos.MultimediaMessageDTO;
import org.example.dtos.ResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface MultimediaMessageService {
    ResponseDTO<MessageDTO> create(
            MultimediaMessageDTO multimediaMessageDTO, MultipartFile file) throws Exception;
}
