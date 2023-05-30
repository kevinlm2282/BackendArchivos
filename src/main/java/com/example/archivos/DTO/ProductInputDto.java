package com.example.archivos.DTO;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ProductInputDto {
    private String name;
    private Integer amount;
    private Double price;
}
