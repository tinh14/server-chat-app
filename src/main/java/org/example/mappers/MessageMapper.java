package org.example.mappers;

import org.example.dtos.MessageDTO;
import org.example.dtos.MultimediaMessageDTO;
import org.example.dtos.TextMessageDTO;
import org.example.entities.ConversationEntity;
import org.example.entities.MessageEntity;
import org.example.entities.MultimediaMessageEntity;
import org.example.entities.TextMessageEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper( MessageMapper.class );

    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "instanceOf", expression = "java(this.mapInstanceOf(messageEntity))")
    MessageDTO toBaseDTO(MessageEntity messageEntity);

    @InheritConfiguration(name = "toBaseDTO")
    TextMessageDTO toTextDTO(TextMessageEntity messageEntity);

    @InheritConfiguration(name = "toBaseDTO")
    MultimediaMessageDTO toMultimediaDTO(MultimediaMessageEntity messageEntity);

    default List<MessageDTO> toDTO(List<MessageEntity> messageEntities) {
        return messageEntities.stream()
                .map(this::toDTO)
                .toList();
    }

    default MessageDTO toDTO(MessageEntity messageEntity) {
        if (messageEntity instanceof TextMessageEntity text) {
            return toTextDTO(text);
        } else if (messageEntity instanceof MultimediaMessageEntity multimedia) {
            return toMultimediaDTO(multimedia);
        }
        return toBaseDTO(messageEntity);
    }

    @Named("mapInstanceOf")
    default String mapInstanceOf(MessageEntity messageEntity) {
        return (messageEntity instanceof TextMessageEntity) ? "TEXT" : "MULTIMEDIA";
    }

}
