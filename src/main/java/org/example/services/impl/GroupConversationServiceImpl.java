package org.example.services.impl;


import org.example.dtos.ConversationDTO;
import org.example.dtos.GroupConversationDTO;
import org.example.dtos.ResponseDTO;
import org.example.entities.ConversationReadStatusEntity;
import org.example.entities.GroupConversationEntity;
import org.example.entities.UserEntity;
import org.example.mappers.ConversationMapper;
import org.example.repositories.ConversationReadStatusRepository;
import org.example.repositories.ConversationRepository;
import org.example.repositories.GroupConversationRepository;
import org.example.repositories.UserRepository;
import org.example.services.GroupConversationService;
import org.example.utils.FileUtils;
import org.example.validators.GroupConversationValidator;
import org.example.validators.MultipartFileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class GroupConversationServiceImpl implements GroupConversationService {

    @Autowired
    private GroupConversationRepository groupConversationRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ConversationReadStatusRepository conversationReadStatusRepo;
    @Autowired
    private GroupConversationValidator groupConversationValidator;
    @Autowired
    private MultipartFileValidator multipartFileValidator;
    @Autowired
    private ConversationMapper conversationMapper;;

    @Override
    public ResponseDTO<ConversationDTO> create(GroupConversationDTO groupConversationDTO, MultipartFile avatar) throws Exception {

        try {
            this.groupConversationValidator.validate(groupConversationDTO);
            this.multipartFileValidator.validate(avatar);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        String conversationId = UUID.randomUUID().toString();

        Optional<GroupConversationEntity> optionalConversation = this.groupConversationRepo.findById(conversationId);

        if (optionalConversation.isPresent()) {
            throw new RuntimeException("Conversation already exists");
        }

        String avatarCode = FileUtils.getFileCode(avatar);

        UserEntity owner = UserEntity.builder()
                .id(groupConversationDTO.getOwnerId())
                .build();

        GroupConversationEntity groupConversationEntity = GroupConversationEntity.builder()
                .id(conversationId)
                .name(groupConversationDTO.getName())
                .ownedBy(owner)
                .participants(List.of(owner))
                .avatarCode(avatarCode)
                .build();

        ConversationReadStatusEntity conversationReadStatusEntity = ConversationReadStatusEntity.builder()
                .conversation(groupConversationEntity)
                .user(owner)
                .read(true)
                .build();

        try {
            groupConversationEntity = groupConversationRepo.save(groupConversationEntity);
            FileUtils.storeFile(avatarCode, avatar);

            this.conversationReadStatusRepo.save(conversationReadStatusEntity);

            return ResponseDTO.<ConversationDTO>builder()
                    .data(this.conversationMapper.toDTO(groupConversationEntity))
                    .build();

        }catch (Exception e) {
            throw new RuntimeException("Cannot create group conversation");
        }

    }

    @Override
    public ResponseDTO<String> update(GroupConversationDTO groupConversationDTO, MultipartFile avatar) throws Exception {
        this.groupConversationValidator.validate(groupConversationDTO);

        Optional<GroupConversationEntity> optionalConversation =
                this.groupConversationRepo.findById(groupConversationDTO.getId());

        if (optionalConversation.isEmpty()) {
            throw new RuntimeException("Group conversation does not exist");
        }

        GroupConversationEntity foundConversation = optionalConversation.get();
        foundConversation.setName(groupConversationDTO.getName());

        try {

            String newFileCode = foundConversation.getAvatarCode();

            if (avatar != null) {
                newFileCode = FileUtils.getFileCode(avatar);
                String oldFileCode = foundConversation.getAvatarCode();

                foundConversation.setAvatarCode(newFileCode);

                FileUtils.storeFile(oldFileCode, newFileCode, avatar);
            }

            this.groupConversationRepo.save(foundConversation);

            return ResponseDTO.<String>builder()
                    .data(newFileCode)
                    .build();
        }catch (Exception e) {
            throw new RuntimeException("Cannot update group conversation");
        }
    }

    @Override
    public ResponseDTO<ConversationDTO> joinConversation(String conversationId, String joinerId) throws Exception {
        Optional<GroupConversationEntity> optionalGroupConversation =
                this.groupConversationRepo.findById(conversationId);

        if (optionalGroupConversation.isEmpty()) {
            throw new RuntimeException("Group conversation does not exist");
        }

        Optional<UserEntity> optionalUser = this.userRepo.findById(joinerId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }

        GroupConversationEntity foundConversation = optionalGroupConversation.get();
        UserEntity foundUser = optionalUser.get();

        boolean exist = foundConversation.getParticipants().stream()
                .anyMatch(user -> user.equals(foundUser));

        if (exist) {
            throw new RuntimeException("User already in conversation");
        }

        foundConversation.getParticipants().add(foundUser);

        ConversationReadStatusEntity conversationReadStatusEntity = ConversationReadStatusEntity.builder()
                .conversation(foundConversation)
                .user(foundUser)
                .read(true)
                .build();

        try {
            foundConversation = this.groupConversationRepo.save(foundConversation);
            this.conversationReadStatusRepo.save(conversationReadStatusEntity);

            ConversationDTO conversationDTO = this.conversationMapper.toDTO(foundConversation);



            return ResponseDTO.<ConversationDTO>builder()
                    .data(conversationDTO)
                    .build();

        }catch (Exception e) {
            throw new RuntimeException("Cannot join group conversation");
        }

    }

    @Override
    public ResponseDTO<Void> leaveConversation(String conversationId, String leaverId) {
        Optional<GroupConversationEntity> optionalGroupConversation = this.groupConversationRepo.findById(conversationId);
        if (optionalGroupConversation.isEmpty()) {
            throw new RuntimeException("Group conversation does not exist");
        }

        Optional<UserEntity> optionalUser = this.userRepo.findById(leaverId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User does not exist");
        }

        GroupConversationEntity foundConversation = optionalGroupConversation.get();
        UserEntity foundUser = optionalUser.get();

        boolean exist = foundConversation.getParticipants().stream()
                .anyMatch(user -> user.equals(foundUser));

        if (!exist) {
            throw new RuntimeException("User not in conversation");
        }

        Optional<ConversationReadStatusEntity> optionalConversationReadStatus =
                this.conversationReadStatusRepo.findByUserAndConversation(foundUser, foundConversation);

        if (optionalConversationReadStatus.isEmpty()) {
            throw new RuntimeException("Conversation read status not found");
        }

        ConversationReadStatusEntity conversationReadStatusEntity = optionalConversationReadStatus.get();

        try {
            // If user is not the owner of the conversation then remove the user from the participants list
            if (!foundConversation.getOwnedBy().equals(foundUser)) {

                foundConversation.getParticipants().remove(foundUser);

                this.groupConversationRepo.save(foundConversation);

                return ResponseDTO.<Void>builder()
                        .build();
            }

            // If user is the owner of the conversation then remove the participants from the conversation
            // and delete the conversation
            foundConversation.getParticipants().forEach(participant -> {
                participant.getConversations().remove(foundConversation);
            });

            this.conversationReadStatusRepo.delete(conversationReadStatusEntity);

            groupConversationRepo.delete(foundConversation);

            return ResponseDTO.<Void>builder()
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Cannot leave group conversation");
        }
    }
}
