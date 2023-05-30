package com.example.archivos.CONTROLLER;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import com.example.archivos.BL.FileSystemStorageService;
import com.example.archivos.BL.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("media")
@CrossOrigin("*")
// @AllArgsConstructor
public class MediaController {

    private final FileSystemStorageService fileSystemStorageService;

    private final HttpServletRequest request;

    Logger LOGGER = LoggerFactory.getLogger(MediaController.class);

    @Autowired
    public MediaController(FileSystemStorageService fileSystemStorageService, HttpServletRequest request) {
        this.fileSystemStorageService = fileSystemStorageService;
        this.request = request;
    }

    @PostMapping("upload")
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        String path = fileSystemStorageService.store(multipartFile);
        String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String url = ServletUriComponentsBuilder
                .fromHttpUrl(host)
                .path("/media/")
                .path(path)
                .toUriString();
        LOGGER.info("El path de la imagen es {}", path);
        LOGGER.info("La url de la imagen es {}", url);
        return Map.of(
            "url",url);

    }

    @GetMapping("{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Resource file = fileSystemStorageService.loadAsResource(filename);
        String contentType = Files.probeContentType(file.getFile().toPath());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(file);
    }
    
}
