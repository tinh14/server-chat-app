package org.example.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ResponseDTO<T> {
    @Builder.Default
    boolean success = true;
    T data;
    String message;
}

