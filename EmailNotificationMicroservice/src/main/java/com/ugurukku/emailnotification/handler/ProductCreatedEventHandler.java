package com.ugurukku.emailnotification.handler;

import static org.springframework.http.HttpMethod.GET;

import com.ugurukku.core.ProductCreatedEvent;
import com.ugurukku.emailnotification.exception.NotRetryableException;
import com.ugurukku.emailnotification.exception.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;

    public ProductCreatedEventHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaHandler
    public void handle(ProductCreatedEvent productCreatedEvent) {
        LOGGER.info("Received event : {}", productCreatedEvent.getTitle());

        String requestUrl = "http://localhost:8082/response/200";
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, GET, null, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("Successfully processed event : {}", responseEntity.getBody());
            }
        } catch (ResourceAccessException e) {
            LOGGER.error("ResourceAccessException : {}", e.getMessage());
             throw new RetryableException(e);
        } catch (HttpServerErrorException e) {
            LOGGER.error("HttpServerErrorException : {}", e.getMessage());
            throw new NotRetryableException(e);
        } catch (Exception e) {
            LOGGER.error("Exception : {}", e.getMessage());
            throw new NotRetryableException(e);
        }
    }

}
