package com.programming.techie.notificationService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@SpringBootApplication
@RequiredArgsConstructor
@Service
public class NotificationServiceApplication {
    
    private static Logger log= LoggerFactory.getLogger(NotificationServiceApplication.class);
    private final Tracer tracer;

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = {"orderPlaced"})
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        Span notificationLookUp= tracer.nextSpan().name("NotificationLookUp");
        try(Tracer.SpanInScope spanInScope= tracer.withSpan(notificationLookUp.start())) {
            System.out.println("I'm here");
//            JsonNode data = new ObjectMapper().readTree(orderPlacedEvent);
            log.info("Notification Received:- {}", orderPlacedEvent.getOrderNumber());
        }
        finally {
            notificationLookUp.end();
        }
    }
    
}
