package org.example.services.impl;

import org.example.dtos.ContactDTO;
import org.example.dtos.ResponseDTO;
import org.example.entities.ContactEntity;
import org.example.entities.ConversationReadStatusEntity;
import org.example.entities.IndividualConversationEntity;
import org.example.entities.UserEntity;
import org.example.entities.ContactEntity.ContactStatus;
import org.example.mappers.ContactMapper;
import org.example.repositories.ContactRepository;
import org.example.repositories.ConversationReadStatusRepository;
import org.example.repositories.ConversationRepository;
import org.example.repositories.UserRepository;
import org.example.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ConversationRepository conversationRepo;
    @Autowired
    private ConversationReadStatusRepository conversationReadStatusRepo;
    @Autowired
    private ContactMapper contactMapper;

    @Transactional(readOnly = true)
    @Override
    public ResponseDTO<List<ContactDTO>> findAllByUserId(String userId) throws Exception {
        List<ContactEntity> contacts = this.contactRepo.findAllByUserId(userId, ContactStatus.ACCEPTED);
        return ResponseDTO.<List<ContactDTO>>builder()
                .data(this.contactMapper.toDTO(contacts))
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseDTO<List<ContactDTO>> findReceivedContactRequests(String userId) throws Exception {
        List<ContactEntity> contacts = this.contactRepo.findReceivedContactRequests(userId, ContactStatus.PENDING);
        return ResponseDTO.<List<ContactDTO>>builder()
                .data(this.contactMapper.toDTO(contacts))
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseDTO<List<ContactDTO>> findSentContactRequests(String userId) throws Exception {
        List<ContactEntity> contacts = this.contactRepo.findSentContactRequests(userId, ContactStatus.PENDING);
        return ResponseDTO.<List<ContactDTO>>builder()
                .data(this.contactMapper.toDTO(contacts))
                .build();
    }

    @Override
    public ResponseDTO<String> sendContactRequest(ContactDTO contactDTO) throws Exception {
        Optional<UserEntity> optionalSender = userRepo.findById(contactDTO.getSender().getId());

        if (optionalSender.isEmpty()){
            throw new RuntimeException(String.format("User with id %s does not exist", contactDTO.getSender().getId()));
        }

        Optional<UserEntity> optionalReceiver = userRepo.findById(contactDTO.getReceiver().getId());

        if (optionalReceiver.isEmpty()){
            throw new RuntimeException(String.format("User with id %s does not exist", contactDTO.getReceiver().getId()));
        }

        UserEntity sender = optionalSender.get();
        UserEntity receiver = optionalReceiver.get();

        Optional<ContactEntity> optionalContact = contactRepo.findOneBySenderAndReceiver(sender, receiver);

        // Contact relationship does not exist
        if (optionalContact.isEmpty()){
            ContactEntity contact = ContactEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .sender(sender)
                    .receiver(receiver)
                    .status(ContactEntity.ContactStatus.PENDING)
                    .build();
            try {
                contactRepo.save(contact);
                return ResponseDTO.<String>builder()
                        .data(contact.getId())
                        .build();
            }catch (Exception e) {
                throw new RuntimeException("Cannot send request");
            }
        }

        ContactEntity foundContact = optionalContact.get();
        ContactStatus foundContactStatus = foundContact.getStatus();

        if (foundContactStatus.equals(ContactStatus.ACCEPTED)) {
            throw new RuntimeException("Contact already exist");
        }

        if (foundContact.getSender().equals(sender)) {
            throw new RuntimeException("Contact request already sent");
        }

        IndividualConversationEntity conversation = IndividualConversationEntity
                .builder()
                .id(UUID.randomUUID().toString())
                .participants(Arrays.asList(receiver, sender))
                .build();

        ConversationReadStatusEntity conversationReadStatusEntity = ConversationReadStatusEntity.builder()
                .conversation(conversation)
                .user(sender)
                .read(true)
                .build();

        try {
            conversation = this.conversationRepo.save(conversation);

            foundContact.setStatus(ContactStatus.ACCEPTED);
            foundContact.setConversation(conversation);

            this.conversationReadStatusRepo.save(conversationReadStatusEntity);
            conversationReadStatusEntity = ConversationReadStatusEntity.builder()
                    .conversation(conversation)
                    .user(receiver)
                    .read(true)
                    .build();
            this.conversationReadStatusRepo.save(conversationReadStatusEntity);

            this.contactRepo.save(foundContact);

            return ResponseDTO.<String>builder()
                    .data(foundContact.getId())
                    .build();
        }catch (Exception e) {
            throw new RuntimeException("Cannot send request");
        }
    }

    @Override
    public ResponseDTO<Void> acceptContactRequest(String contactId) throws Exception {
        Optional<ContactEntity> optionalContact = this.contactRepo.findById(contactId);

        if (optionalContact.isEmpty()){
            throw new RuntimeException("Contact request does not exist");
        }

        ContactEntity contact = optionalContact.get();

        UserEntity sender = contact.getSender();
        UserEntity receiver = contact.getReceiver();

        IndividualConversationEntity conversation = IndividualConversationEntity.builder()
                .id(UUID.randomUUID().toString())
                .participants(Arrays.asList(sender, receiver))
                .build();

        ConversationReadStatusEntity conversationReadStatusEntity = ConversationReadStatusEntity.builder()
                .conversation(conversation)
                .user(sender)
                .read(true)
                .build();

        try {
            conversation = this.conversationRepo.save(conversation);

            contact.setStatus(ContactStatus.ACCEPTED);
            contact.setConversation(conversation);

            this.conversationReadStatusRepo.save(conversationReadStatusEntity);
            conversationReadStatusEntity = ConversationReadStatusEntity.builder()
                    .conversation(conversation)
                    .user(receiver)
                    .read(true)
                    .build();
            this.conversationReadStatusRepo.save(conversationReadStatusEntity);

            this.contactRepo.save(contact);

            return ResponseDTO.<Void>builder()
                    .build();
        }catch (Exception e){
            throw new RuntimeException("Cannot accept request");
        }
    }

    @Override
    public ResponseDTO<Void> deleteContact(String contactId) throws Exception {
        Optional<ContactEntity> optionalContact = this.contactRepo.findById(contactId);

        if (optionalContact.isEmpty()){
            throw new RuntimeException("Contact request does not exist");
        }

        ContactEntity contact = optionalContact.get();

        List<ConversationReadStatusEntity> conversationReadStatusEntities =
                this.conversationReadStatusRepo.findByConversation(contact.getConversation());

        try {
            conversationReadStatusEntities.forEach(c -> {
                this.conversationReadStatusRepo.delete(c);
            });

            this.contactRepo.delete(contact);

            return ResponseDTO.<Void>builder()
                    .build();
        }catch (Exception e){
            throw new RuntimeException("Cannot delete request");
        }
    }
}
