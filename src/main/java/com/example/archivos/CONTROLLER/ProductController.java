package com.example.archivos.CONTROLLER;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.archivos.BL.ProductService;
import com.example.archivos.DTO.ProductInputDto;
import com.example.archivos.DTO.ProductResponse;
import com.example.archivos.ENTITY.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@RestController()
@CrossOrigin("*")
public class ProductController {

    private Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value="/products", method=RequestMethod.GET)
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    @RequestMapping(value="/pag/products", method=RequestMethod.GET)
    public ProductResponse getAllProducts(
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
        @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return productService.getAllProductsResponse(pageNo, pageSize, sortBy, sortDir);
    }


    // @RequestMapping(value="/products", method=RequestMethod.POST)
    // public ResponseEntity<Product> postProduct(@RequestBody Product product) {
    //     LOGGER.info("data de entrada {}", product);
    //     Product result = productService.saveProduct(product);
    //     return new ResponseEntity<>(result,HttpStatus.OK);
    // }

    @RequestMapping(value="/products", method=RequestMethod.POST)
    public ResponseEntity<String> postProduct(@RequestParam(required = false, name = "file") MultipartFile file, @RequestParam("data") String data) throws JsonMappingException, JsonProcessingException {
        LOGGER.info("data de entrada {}", data);
        LOGGER.info("imagen de entrada {}", file);
        productService.saveProduct(data, file);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @RequestMapping(value="/products/{id}", method=RequestMethod.PUT)
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestParam(required = false, name = "file") MultipartFile file, @RequestParam("data") String data) throws JsonMappingException, JsonProcessingException {
        Product updateProduct =  productService.updateProductDto(id, data, file);
        return new ResponseEntity<>(updateProduct,HttpStatus.OK);
    }
    
    
    

    
    
}
