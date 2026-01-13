package com.irrigation.energy.repositories;

import com.irrigation.energy.entities.ConsommationElectrique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsommationElectriqueRepository extends JpaRepository<ConsommationElectrique, Long> {
    List<ConsommationElectrique> findByPompeId(Long pompeId);

    List<ConsommationElectrique> findByDateMesureBetween(LocalDateTime start, LocalDateTime end);
}
