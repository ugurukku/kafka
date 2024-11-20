package com.ugurukku.productsmicroservice.service;

import com.ugurukku.productsmicroservice.dto.product.CreateProductRequest;
import java.util.concurrent.ExecutionException;

public interface ProductService {
    String create(CreateProductRequest request) throws ExecutionException, InterruptedException;
}
