package com.irrigation.water.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "energy-service")
public interface EnergyClient {

    @GetMapping("/api/energy/disponibilite/{pompeId}")
    Boolean checkDisponibilite(@PathVariable("pompeId") Long pompeId);

    @GetMapping("/api/pompes/{id}")
    Object getPompeById(@PathVariable("id") Long id);
}
