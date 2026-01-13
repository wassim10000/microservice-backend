package com.irrigation.water.repositories;

import com.irrigation.water.entities.DebitMesure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DebitMesureRepository extends JpaRepository<DebitMesure, Long> {
    List<DebitMesure> findByPompeId(Long pompeId);

    List<DebitMesure> findByDateMesureBetween(LocalDateTime start, LocalDateTime end);
}
