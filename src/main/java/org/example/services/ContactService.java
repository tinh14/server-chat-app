package org.example.services;

import org.example.dtos.ContactDTO;
import org.example.dtos.ResponseDTO;

import java.util.List;

public interface ContactService {
    ResponseDTO<List<ContactDTO>> findAllByUserId(String userId) throws Exception;

    ResponseDTO<List<ContactDTO>> findReceivedContactRequests(String userId) throws Exception;

    ResponseDTO<List<ContactDTO>> findSentContactRequests(String userId) throws Exception;

    ResponseDTO<String> sendContactRequest(ContactDTO contactDTO) throws Exception;

    ResponseDTO<Void> acceptContactRequest(String contactId) throws Exception;

    ResponseDTO<Void> deleteContact(String contactId) throws Exception;
}
