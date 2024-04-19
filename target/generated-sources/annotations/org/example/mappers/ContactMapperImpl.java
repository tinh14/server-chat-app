package org.example.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.dtos.ContactDTO;
import org.example.entities.ContactEntity;
import org.example.entities.IndividualConversationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-20T00:04:28+0700",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class ContactMapperImpl implements ContactMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<ContactDTO> toDTO(List<ContactEntity> contacts) {
        if ( contacts == null ) {
            return null;
        }

        List<ContactDTO> list = new ArrayList<ContactDTO>( contacts.size() );
        for ( ContactEntity contactEntity : contacts ) {
            list.add( toDTO( contactEntity ) );
        }

        return list;
    }

    @Override
    public ContactDTO toDTO(ContactEntity contact) {
        if ( contact == null ) {
            return null;
        }

        ContactDTO.ContactDTOBuilder contactDTO = ContactDTO.builder();

        contactDTO.conversationId( contactConversationId( contact ) );
        contactDTO.id( contact.getId() );
        contactDTO.sender( userMapper.toDTO( contact.getSender() ) );
        contactDTO.receiver( userMapper.toDTO( contact.getReceiver() ) );

        return contactDTO.build();
    }

    private String contactConversationId(ContactEntity contactEntity) {
        if ( contactEntity == null ) {
            return null;
        }
        IndividualConversationEntity conversation = contactEntity.getConversation();
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
