package com.irrigation.water.messaging;

import com.irrigation.water.config.RabbitMQConfig;
import com.irrigation.water.dto.SurconsommationEvent;
import com.irrigation.water.entities.Alerte;
import com.irrigation.water.repositories.AlerteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final AlerteRepository alerteRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleSurconsommationEvent(SurconsommationEvent event) {
        log.warn("⚠️ Received overconsumption alert: {}", event);

        // Save the alert to the database
        Alerte alerte = new Alerte();
        alerte.setPompeId(event.getPompeId());
        alerte.setType("SURCONSOMMATION");
        alerte.setMessage(event.getMessage());
        alerte.setValeur(event.getEnergieUtilisee());
        alerte.setDateAlerte(LocalDateTime.now());
        alerte.setTraitee(false);

        Alerte saved = alerteRepository.save(alerte);
        log.info("Alert saved with id: {}", saved.getId());
    }
}
