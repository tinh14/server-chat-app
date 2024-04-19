package org.example.services;

import org.example.dtos.ResponseDTO;

public interface FileFetcherService {

    ResponseDTO<byte[]> fetchFile(String fileCode) throws Exception;

}
