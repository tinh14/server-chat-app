package org.example.mappers;

import javax.annotation.processing.Generated;
import org.example.dtos.MessageDTO;
import org.example.dtos.MultimediaMessageDTO;
import org.example.dtos.TextMessageDTO;
import org.example.entities.ConversationEntity;
import org.example.entities.MessageEntity;
import org.example.entities.MultimediaMessageEntity;
import org.example.entities.TextMessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-20T00:04:28+0700",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public MessageDTO toBaseDTO(MessageEntity messageEntity) {
        if ( messageEntity == null ) {
            return null;
        }

        MessageDTO.MessageDTOBuilder<?, ?> messageDTO = MessageDTO.builder();

        messageDTO.conversationId( messageEntityConversationId( messageEntity ) );
        messageDTO.id( messageEntity.getId() );
        messageDTO.sentAt( messageEntity.getSentAt() );
        messageDTO.sender( userMapper.toDTO( messageEntity.getSender() ) );

        messageDTO.instanceOf( this.mapInstanceOf(messageEntity) );

        return messageDTO.build();
    }

    @Override
    public TextMessageDTO toTextDTO(TextMessageEntity messageEntity) {
        if ( messageEntity == null ) {
            return null;
        }

        TextMessageDTO.TextMessageDTOBuilder<?, ?> textMessageDTO = TextMessageDTO.builder();

        textMessageDTO.conversationId( messageEntityConversationId1( messageEntity ) );
        textMessageDTO.id( messageEntity.getId() );
        textMessageDTO.sentAt( messageEntity.getSentAt() );
        textMessageDTO.sender( userMapper.toDTO( messageEntity.getSender() ) );
        textMessageDTO.content( messageEntity.getContent() );

        textMessageDTO.instanceOf( this.mapInstanceOf(messageEntity) );

        return textMessageDTO.build();
    }

    @Override
    public MultimediaMessageDTO toMultimediaDTO(MultimediaMessageEntity messageEntity) {
        if ( messageEntity == null ) {
            return null;
        }

        MultimediaMessageDTO.MultimediaMessageDTOBuilder<?, ?> multimediaMessageDTO = MultimediaMessageDTO.builder();

        multimediaMessageDTO.conversationId( messageEntityConversationId2( messageEntity ) );
        multimediaMessageDTO.id( messageEntity.getId() );
        multimediaMessageDTO.sentAt( messageEntity.getSentAt() );
        multimediaMessageDTO.sender( userMapper.toDTO( messageEntity.getSender() ) );
        multimediaMessageDTO.fileCode( messageEntity.getFileCode() );
        multimediaMessageDTO.fileName( messageEntity.getFileName() );
        if ( messageEntity.getFileType() != null ) {
            multimediaMessageDTO.fileType( messageEntity.getFileType().name() );
        }

        multimediaMessageDTO.instanceOf( this.mapInstanceOf(messageEntity) );

        return multimediaMessageDTO.build();
    }

    private String messageEntityConversationId(MessageEntity messageEntity) {
        if ( messageEntity == null ) {
            return null;
        }
        ConversationEntity conversation = messageEntity.getConversation();
        if ( conversation == null ) {
            return null;
        }
        String id = conversation.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String messageEntityConversationId1(TextMessageEntity textMessageEntity) {
        if ( textMessageEntity == null ) {
            return null;
        }
        ConversationEntity conversation = textMessageEntity.getConversation();
        if ( conversation == null ) {
            return null;
        }
        String id = conversation.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String messageEntityConversationId2(MultimediaMessageEntity multimediaMessageEntity) {
        if ( multimediaMessageEntity == null ) {
            return null;
        }
        ConversationEntity conversation = multimediaMessageEntity.getConversation();
        if ( conversation == null ) {
            return null;
        }
        String id = conversation.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
