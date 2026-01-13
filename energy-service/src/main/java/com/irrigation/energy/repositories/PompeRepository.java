package com.irrigation.energy.repositories;

import com.irrigation.energy.entities.Pompe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PompeRepository extends JpaRepository<Pompe, Long> {
    List<Pompe> findByStatut(String statut);
}
