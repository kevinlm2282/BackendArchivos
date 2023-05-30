package com.example.archivos.REPOSITORY;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.archivos.ENTITY.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{
    
}
