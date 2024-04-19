package org.example.mappers;

import org.example.dtos.ContactDTO;
import org.example.dtos.UserDTO;
import org.example.entities.ContactEntity;
import org.example.entities.ConversationEntity;
import org.example.entities.UserEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    @IterableMapping(elementTargetType = ContactDTO.class)
    List<ContactDTO> toDTO(List<ContactEntity> contacts);

    @Mapping(target = "conversationId", source = "conversation.id")
    ContactDTO toDTO(ContactEntity contact);

}
