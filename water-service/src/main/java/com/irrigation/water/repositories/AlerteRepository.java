package com.irrigation.water.repositories;

import com.irrigation.water.entities.Alerte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlerteRepository extends JpaRepository<Alerte, Long> {
    List<Alerte> findByTraiteeFalse();

    List<Alerte> findByPompeId(Long pompeId);
}
