package com.irrigation.water.service;

import com.irrigation.water.dto.DebitMesureDTO;
import com.irrigation.water.entities.DebitMesure;
import com.irrigation.water.repositories.DebitMesureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DebitService {

    private final DebitMesureRepository debitMesureRepository;

    public DebitMesureDTO createDebitMesure(DebitMesureDTO dto) {
        DebitMesure debit = toEntity(dto);
        DebitMesure saved = debitMesureRepository.save(debit);
        return toDTO(saved);
    }

    public DebitMesureDTO getDebitById(Long id) {
        DebitMesure debit = debitMesureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DebitMesure not found with id: " + id));
        return toDTO(debit);
    }

    public List<DebitMesureDTO> getAllDebits() {
        return debitMesureRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<DebitMesureDTO> getDebitsByPompeId(Long pompeId) {
        return debitMesureRepository.findByPompeId(pompeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Double getAverageDebitByPompe(Long pompeId) {
        return debitMesureRepository.findByPompeId(pompeId).stream()
                .mapToDouble(DebitMesure::getDebit)
                .average()
                .orElse(0.0);
    }

    private DebitMesureDTO toDTO(DebitMesure debit) {
        return DebitMesureDTO.builder()
                .id(debit.getId())
                .pompeId(debit.getPompeId())
                .debit(debit.getDebit())
                .dateMesure(debit.getDateMesure())
                .unite(debit.getUnite())
                .build();
    }

    private DebitMesure toEntity(DebitMesureDTO dto) {
        DebitMesure debit = new DebitMesure();
        debit.setId(dto.getId());
        debit.setPompeId(dto.getPompeId());
        debit.setDebit(dto.getDebit());
        debit.setDateMesure(dto.getDateMesure() != null ? dto.getDateMesure() : LocalDateTime.now());
        debit.setUnite(dto.getUnite() != null ? dto.getUnite() : "L/min");
        return debit;
    }
}
