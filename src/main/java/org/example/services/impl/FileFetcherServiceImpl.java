package org.example.services.impl;

import org.example.dtos.ResponseDTO;
import org.example.services.FileFetcherService;
import org.example.utils.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class FileFetcherServiceImpl implements FileFetcherService {

    @Override
    public ResponseDTO<byte[]> fetchFile(String avatarCode) throws Exception {
        try {
            return ResponseDTO.<byte[]>builder()
                    .data(FileUtils.getFile(avatarCode))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("File not found");
        }
    }
}
