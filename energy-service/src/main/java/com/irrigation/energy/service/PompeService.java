package com.irrigation.energy.service;

import com.irrigation.energy.dto.PompeDTO;
import com.irrigation.energy.entities.Pompe;
import com.irrigation.energy.repositories.PompeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PompeService {

    private final PompeRepository pompeRepository;

    public PompeDTO createPompe(PompeDTO pompeDTO) {
        Pompe pompe = toEntity(pompeDTO);
        Pompe saved = pompeRepository.save(pompe);
        return toDTO(saved);
    }

    public PompeDTO updatePompe(Long id, PompeDTO pompeDTO) {
        Pompe pompe = pompeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pompe not found with id: " + id));

        pompe.setReference(pompeDTO.getReference());
        pompe.setPuissance(pompeDTO.getPuissance());
        pompe.setStatut(pompeDTO.getStatut());
        pompe.setDateMiseEnService(pompeDTO.getDateMiseEnService());

        Pompe updated = pompeRepository.save(pompe);
        return toDTO(updated);
    }

    public PompeDTO getPompeById(Long id) {
        Pompe pompe = pompeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pompe not found with id: " + id));
        return toDTO(pompe);
    }

    public List<PompeDTO> getAllPompes() {
        return pompeRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void deletePompe(Long id) {
        pompeRepository.deleteById(id);
    }

    public List<PompeDTO> getPompesByStatut(String statut) {
        return pompeRepository.findByStatut(statut).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public boolean isPompeDisponible(Long id) {
        Pompe pompe = pompeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pompe not found with id: " + id));
        return "ACTIVE".equalsIgnoreCase(pompe.getStatut());
    }

    private PompeDTO toDTO(Pompe pompe) {
        return PompeDTO.builder()
                .id(pompe.getId())
                .reference(pompe.getReference())
                .puissance(pompe.getPuissance())
                .statut(pompe.getStatut())
                .dateMiseEnService(pompe.getDateMiseEnService())
                .build();
    }

    private Pompe toEntity(PompeDTO dto) {
        Pompe pompe = new Pompe();
        pompe.setId(dto.getId());
        pompe.setReference(dto.getReference());
        pompe.setPuissance(dto.getPuissance());
        pompe.setStatut(dto.getStatut());
        pompe.setDateMiseEnService(dto.getDateMiseEnService());
        return pompe;
    }
}
