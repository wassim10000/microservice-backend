package com.irrigation.energy.service;

import com.irrigation.energy.dto.ConsommationElectriqueDTO;
import com.irrigation.energy.dto.SurconsommationEvent;
import com.irrigation.energy.entities.ConsommationElectrique;
import com.irrigation.energy.messaging.MessagePublisher;
import com.irrigation.energy.repositories.ConsommationElectriqueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ConsommationService {

    private final ConsommationElectriqueRepository consommationRepository;
    private final MessagePublisher messagePublisher;

    @Value("${energy.alert.threshold}")
    private Double alertThreshold;

    public ConsommationElectriqueDTO createConsommation(ConsommationElectriqueDTO dto) {
        ConsommationElectrique consommation = toEntity(dto);
        ConsommationElectrique saved = consommationRepository.save(consommation);

        // Check for overconsumption and publish event if needed
        if (saved.getEnergieUtilisee() > alertThreshold) {
            publishSurconsommationAlert(saved);
        }

        return toDTO(saved);
    }

    public ConsommationElectriqueDTO getConsommationById(Long id) {
        ConsommationElectrique consommation = consommationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consommation not found with id: " + id));
        return toDTO(consommation);
    }

    public List<ConsommationElectriqueDTO> getAllConsommations() {
        return consommationRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ConsommationElectriqueDTO> getConsommationsByPompeId(Long pompeId) {
        return consommationRepository.findByPompeId(pompeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Double getTotalConsommationByPompe(Long pompeId) {
        return consommationRepository.findByPompeId(pompeId).stream()
                .mapToDouble(ConsommationElectrique::getEnergieUtilisee)
                .sum();
    }

    public List<ConsommationElectriqueDTO> getConsommationsByDateRange(LocalDateTime start, LocalDateTime end) {
        return consommationRepository.findByDateMesureBetween(start, end).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private void publishSurconsommationAlert(ConsommationElectrique consommation) {
        log.warn("Overconsumption detected! PompeId: {}, Energy: {} kWh, Threshold: {} kWh",
                consommation.getPompeId(), consommation.getEnergieUtilisee(), alertThreshold);

        SurconsommationEvent event = SurconsommationEvent.builder()
                .pompeId(consommation.getPompeId())
                .energieUtilisee(consommation.getEnergieUtilisee())
                .threshold(alertThreshold)
                .dateMesure(consommation.getDateMesure())
                .message(String.format("⚠️ Pompe %d a consommé %.2f kWh (seuil: %.2f kWh)",
                        consommation.getPompeId(), consommation.getEnergieUtilisee(), alertThreshold))
                .build();

        messagePublisher.publishSurconsommationEvent(event);
    }

    private ConsommationElectriqueDTO toDTO(ConsommationElectrique consommation) {
        return ConsommationElectriqueDTO.builder()
                .id(consommation.getId())
                .pompeId(consommation.getPompeId())
                .energieUtilisee(consommation.getEnergieUtilisee())
                .duree(consommation.getDuree())
                .dateMesure(consommation.getDateMesure())
                .build();
    }

    private ConsommationElectrique toEntity(ConsommationElectriqueDTO dto) {
        ConsommationElectrique consommation = new ConsommationElectrique();
        consommation.setId(dto.getId());
        consommation.setPompeId(dto.getPompeId());
        consommation.setEnergieUtilisee(dto.getEnergieUtilisee());
        consommation.setDuree(dto.getDuree());
        consommation.setDateMesure(dto.getDateMesure() != null ? dto.getDateMesure() : LocalDateTime.now());
        return consommation;
    }
}
