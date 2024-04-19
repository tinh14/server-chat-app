package org.example.controllers;

import org.example.dtos.ResponseDTO;
import org.example.services.FileFetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v2")
public class FileFetcherController {

    @Value("${default.group.avatar.code}")
    private String defaultGroupAvatarCode;

    @Autowired
    private FileFetcherService fileFetcherService;

    @GetMapping("/files/inline/{fileCode}")
    public ResponseEntity<byte[]> fetchFile(@PathVariable String fileCode) {
        try {

            byte[] bytes = this.fileFetcherService.fetchFile(fileCode).getData();

            String mimeType = URLConnection.guessContentTypeFromName(fileCode);

            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType));
            headers.setContentDispositionFormData("inline", fileCode);
            headers.setContentLength(bytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(bytes);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/files/attachment/{fileCode}")
    public ResponseEntity<byte[]> fetchAsAttachment(@PathVariable String fileCode) {
        try {
            byte[] bytes = this.fileFetcherService. fetchFile(fileCode).getData();

            String mimeType = URLConnection.guessContentTypeFromName(fileCode);

            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(mimeType));
            headers.setContentDispositionFormData("attachment", fileCode);
            headers.setContentLength(bytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(bytes);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
