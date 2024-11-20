package com.ugurukku.productsmicroservice.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.ugurukku.productsmicroservice.dto.product.CreateProductRequest;
import com.ugurukku.productsmicroservice.service.ProductService;
import java.util.concurrent.ExecutionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody CreateProductRequest request)
            throws ExecutionException, InterruptedException {
        return ResponseEntity
                .status(CREATED)
                .body(productService.create(request));
    }

}
