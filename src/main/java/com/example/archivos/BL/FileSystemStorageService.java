package com.example.archivos.BL;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;


// import jakarta.annotation.Resource;
@Service
public class FileSystemStorageService{
    
    @Value("${media.location}")
    private String mediaLocation;

    private Path rootLocation;

    private Logger LOGGER = LoggerFactory.getLogger(FileSystemStorageService.class);
    

    
    // @PostConstruct
    // public void init() throws IOException{
    //     rootLocation = Paths.get(mediaLocation);
    //     Files.createDirectories(rootLocation);
    // }
    



    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Faile to store, empty file");
            }
            String filename = file.getOriginalFilename();
            
            String extencion = filename.substring(filename.lastIndexOf("."));
            String randomName = RandomStringUtils.randomAlphabetic(50);
            String newFileName = randomName + extencion;
            Long size = file.getSize();
            LOGGER.info("la extencion del archivo es {}", extencion);
            LOGGER.info("El nuevo nombre del archivo deberia se este {}", newFileName);
            LOGGER.info("El tamanho de la imagen es {}", size);
            LOGGER.info("El nombre del archivo es {}", filename);
            Path destinationFile = rootLocation.resolve(Paths.get(newFileName)).normalize().toAbsolutePath();
    
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return newFileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file. ", e);
        }
    }

    // @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource((file.toUri()));

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Culd not read file " + filename);
            }
            
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file " + filename);
        }
    }

    // @Override
    public void init() throws IOException {
        rootLocation = Paths.get(mediaLocation);
        Files.createDirectories(rootLocation);
    }
}
