package com.irrigation.water.repositories;

import com.irrigation.water.entities.Reservoir;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservoirRepository extends JpaRepository<Reservoir, Long> {
    List<Reservoir> findByLocalisation(String localisation);
}
