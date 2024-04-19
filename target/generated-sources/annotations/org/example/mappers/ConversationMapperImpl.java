package org.example.mappers;

import javax.annotation.processing.Generated;
import org.example.dtos.ConversationDTO;
import org.example.dtos.GroupConversationDTO;
import org.example.dtos.IndividualConversationDTO;
import org.example.entities.ConversationEntity;
import org.example.entities.GroupConversationEntity;
import org.example.entities.IndividualConversationEntity;
import org.example.entities.UserEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-20T00:04:28+0700",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class ConversationMapperImpl implements ConversationMapper {

    @Override
    public ConversationDTO toBaseDTO(ConversationEntity conversationEntity) {
        if ( conversationEntity == null ) {
            return null;
        }

        ConversationDTO.ConversationDTOBuilder<?, ?> conversationDTO = ConversationDTO.builder();

        conversationDTO.id( conversationEntity.getId() );

        conversationDTO.instanceOf( this.mapInstanceOf(conversationEntity) );

        return conversationDTO.build();
    }

    @Override
    public GroupConversationDTO toGroupDTO(GroupConversationEntity conversationEntity) {
        if ( conversationEntity == null ) {
            return null;
        }

        GroupConversationDTO.GroupConversationDTOBuilder<?, ?> groupConversationDTO = GroupConversationDTO.builder();

        groupConversationDTO.ownerId( conversationEntityOwnedById( conversationEntity ) );
        groupConversationDTO.id( conversationEntity.getId() );
        groupConversationDTO.name( conversationEntity.getName() );
        groupConversationDTO.avatarCode( conversationEntity.getAvatarCode() );

        groupConversationDTO.instanceOf( this.mapInstanceOf(conversationEntity) );

        return groupConversationDTO.build();
    }

    @Override
    public IndividualConversationDTO toIndividualDTO(IndividualConversationEntity conversationEntity) {
        if ( conversationEntity == null ) {
            return null;
        }

        IndividualConversationDTO.IndividualConversationDTOBuilder<?, ?> individualConversationDTO = IndividualConversationDTO.builder();

        individualConversationDTO.id( conversationEntity.getId() );

        individualConversationDTO.instanceOf( this.mapInstanceOf(conversationEntity) );

        return individualConversationDTO.build();
    }

    private String conversationEntityOwnedById(GroupConversationEntity groupConversationEntity) {
        if ( groupConversationEntity == null ) {
            return null;
        }
        UserEntity ownedBy = groupConversationEntity.getOwnedBy();
        if ( ownedBy == null ) {
            return null;
        }
        String id = ownedBy.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
