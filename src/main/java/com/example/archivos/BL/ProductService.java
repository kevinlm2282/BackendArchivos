package com.example.archivos.BL;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.archivos.DTO.ProductInputDto;
import com.example.archivos.ENTITY.Product;
import com.example.archivos.REPOSITORY.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ProductService {

    private Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private ProductRepository productRepository;
    private FileSystemStorageService fileSystemStorageService;
    private final HttpServletRequest request;

    @Autowired
    public ProductService(ProductRepository productRepository, FileSystemStorageService fileSystemStorageService,
            HttpServletRequest request) {
        this.productRepository = productRepository;
        this.fileSystemStorageService = fileSystemStorageService;
        this.request = request;
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public ProductInputDto saveProduct(String product, MultipartFile image) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductInputDto productInputDto = objectMapper.readValue(product, ProductInputDto.class);
        String host = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        if (image == null) {
            String url = ServletUriComponentsBuilder
                    .fromHttpUrl(host)
                    .path("/media/")
                    .path("noAvailableImage.svg")
                    .toUriString();
            Product product1 = new Product();
            product1.setName(productInputDto.getName());
            product1.setAmount(productInputDto.getAmount());
            product1.setPrice(productInputDto.getPrice());
            product1.setImage(url);
            productRepository.save(product1);
            return productInputDto;
        } else {
            String path = fileSystemStorageService.store(image);
            String url = ServletUriComponentsBuilder
                    .fromHttpUrl(host)
                    .path("/media/")
                    .path(path)
                    .toUriString();
            LOGGER.info("El path de la imagen es {}", path);
            LOGGER.info("La url de la imagen es {}", url);
            LOGGER.info("Los datos que entran son {}", productInputDto);
            Product product2 = new Product();
            product2.setName(productInputDto.getName());
            product2.setAmount(productInputDto.getAmount());
            product2.setPrice(productInputDto.getPrice());
            product2.setImage(url);
            productRepository.save(product2);
            return productInputDto;
        }
    }

    
    
}
