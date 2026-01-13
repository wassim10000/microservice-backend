package com.irrigation.water.service;

import com.irrigation.water.dto.AlerteDTO;
import com.irrigation.water.entities.Alerte;
import com.irrigation.water.repositories.AlerteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AlerteService {

    private final AlerteRepository alerteRepository;

    public List<AlerteDTO> getAllAlertes() {
        return alerteRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<AlerteDTO> getAlertesNonTraitees() {
        return alerteRepository.findByTraiteeFalse().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<AlerteDTO> getAlertesByPompe(Long pompeId) {
        return alerteRepository.findByPompeId(pompeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AlerteDTO marquerCommeTraitee(Long id) {
        Alerte alerte = alerteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerte not found with id: " + id));
        alerte.setTraitee(true);
        Alerte saved = alerteRepository.save(alerte);
        return toDTO(saved);
    }

    private AlerteDTO toDTO(Alerte alerte) {
        return AlerteDTO.builder()
                .id(alerte.getId())
                .pompeId(alerte.getPompeId())
                .type(alerte.getType())
                .message(alerte.getMessage())
                .valeur(alerte.getValeur())
                .dateAlerte(alerte.getDateAlerte())
                .traitee(alerte.isTraitee())
                .build();
    }
}
