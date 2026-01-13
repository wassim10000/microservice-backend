package com.irrigation.water.service;

import com.irrigation.water.dto.ReservoirDTO;
import com.irrigation.water.entities.Reservoir;
import com.irrigation.water.repositories.ReservoirRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservoirService {

    private final ReservoirRepository reservoirRepository;

    public ReservoirDTO createReservoir(ReservoirDTO dto) {
        Reservoir reservoir = toEntity(dto);
        Reservoir saved = reservoirRepository.save(reservoir);
        return toDTO(saved);
    }

    public ReservoirDTO updateReservoir(Long id, ReservoirDTO dto) {
        Reservoir reservoir = reservoirRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservoir not found with id: " + id));

        reservoir.setNom(dto.getNom());
        reservoir.setCapaciteTotale(dto.getCapaciteTotale());
        reservoir.setVolumeActuel(dto.getVolumeActuel());
        reservoir.setLocalisation(dto.getLocalisation());

        Reservoir updated = reservoirRepository.save(reservoir);
        return toDTO(updated);
    }

    public ReservoirDTO getReservoirById(Long id) {
        Reservoir reservoir = reservoirRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservoir not found with id: " + id));
        return toDTO(reservoir);
    }

    public List<ReservoirDTO> getAllReservoirs() {
        return reservoirRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteReservoir(Long id) {
        reservoirRepository.deleteById(id);
    }

    public Double getNiveauRemplissage(Long id) {
        Reservoir reservoir = reservoirRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservoir not found with id: " + id));
        return (reservoir.getVolumeActuel() / reservoir.getCapaciteTotale()) * 100;
    }

    private ReservoirDTO toDTO(Reservoir reservoir) {
        return ReservoirDTO.builder()
                .id(reservoir.getId())
                .nom(reservoir.getNom())
                .capaciteTotale(reservoir.getCapaciteTotale())
                .volumeActuel(reservoir.getVolumeActuel())
                .localisation(reservoir.getLocalisation())
                .build();
    }

    private Reservoir toEntity(ReservoirDTO dto) {
        Reservoir reservoir = new Reservoir();
        reservoir.setId(dto.getId());
        reservoir.setNom(dto.getNom());
        reservoir.setCapaciteTotale(dto.getCapaciteTotale());
        reservoir.setVolumeActuel(dto.getVolumeActuel());
        reservoir.setLocalisation(dto.getLocalisation());
        return reservoir;
    }
}
