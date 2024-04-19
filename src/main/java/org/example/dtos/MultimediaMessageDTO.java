package org.example.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class MultimediaMessageDTO extends MessageDTO {
    private String fileCode;
    private String fileName;
    private String fileType;
}
