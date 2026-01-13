package com.irrigation.energy.messaging;

import com.irrigation.energy.dto.SurconsommationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${energy.alert.exchange}")
    private String exchange;

    @Value("${energy.alert.routing-key}")
    private String routingKey;

    public void publishSurconsommationEvent(SurconsommationEvent event) {
        log.info("Publishing overconsumption event: {}", event);
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        log.info("Event published successfully");
    }
}
