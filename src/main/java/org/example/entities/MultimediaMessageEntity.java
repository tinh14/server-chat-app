package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class MultimediaMessageEntity extends MessageEntity {

    public enum MultimediaMessageType {
        VIDEO, AUDIO, IMAGE, SHEET, PRESENTATION, DOCUMENT, PDF, OTHER;
    }

    @Column(name = "message_file_code")
    private String fileCode;

    @Column(name = "message_file_name")
    private String fileName;

    @Enumerated
    @Column(name = "message_file_type")
    private MultimediaMessageType fileType;
}
