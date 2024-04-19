package org.example.mappers;

import org.example.dtos.ConversationDTO;
import org.example.dtos.GroupConversationDTO;
import org.example.dtos.IndividualConversationDTO;
import org.example.dtos.UserDTO;
import org.example.entities.ConversationEntity;
import org.example.entities.GroupConversationEntity;
import org.example.entities.IndividualConversationEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = {MessageMapper.class, UserMapper.class})
public interface ConversationMapper {
    ConversationMapper INSTANCE = Mappers.getMapper(ConversationMapper.class);

    @Mapping(target = "instanceOf", expression = "java(this.mapInstanceOf(conversationEntity))")
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "messages", ignore = true)
    @Mapping(target = "lastestMessage", ignore = true)
    @Mapping(target = "hasNewMessage", ignore = true)
    ConversationDTO toBaseDTO(ConversationEntity conversationEntity);

    @InheritConfiguration(name = "toBaseDTO")
    @Mapping(target = "ownerId", source = "ownedBy.id")
    GroupConversationDTO toGroupDTO(GroupConversationEntity conversationEntity);

    @InheritConfiguration(name = "toBaseDTO")
    IndividualConversationDTO toIndividualDTO(IndividualConversationEntity conversationEntity);

    default List<ConversationDTO> toDTO(List<ConversationEntity> conversationEntities) {
        return conversationEntities.stream()
                .map(this::toDTO)
                .toList();
    }

    default ConversationDTO toDTO(ConversationEntity conversationEntity) {
        if (conversationEntity instanceof GroupConversationEntity gr) {
            return toGroupDTO(gr);
        } else if (conversationEntity instanceof IndividualConversationEntity in) {
            return toIndividualDTO(in);
        }

        return toDTO(conversationEntity);
    }

    @Named("mapInstanceOf")
    default String mapInstanceOf(ConversationEntity conversationEntity) {
        return (conversationEntity instanceof GroupConversationEntity) ? "GROUP" : "INDIVIDUAL";
    }

}