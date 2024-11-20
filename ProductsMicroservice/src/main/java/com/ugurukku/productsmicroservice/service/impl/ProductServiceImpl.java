package com.ugurukku.productsmicroservice.service.impl;

import com.ugurukku.core.ProductCreatedEvent;
import com.ugurukku.productsmicroservice.dto.product.CreateProductRequest;
import com.ugurukku.productsmicroservice.service.ProductService;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String create(CreateProductRequest request) throws ExecutionException, InterruptedException {
        String productId = UUID.randomUUID().toString();

        // TODO: Persist Product Details into DB before publishing event

        ProductCreatedEvent productCreatedEvent =
                new ProductCreatedEvent(request.getProductId(), request.getTitle(),
                        request.getPrice(), request.getQuantity());

        SendResult<String, ProductCreatedEvent> result =
                kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent).get();

        return productId;
    }

}
