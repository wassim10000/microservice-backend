package com.irrigation.energy.web;

import com.irrigation.energy.dto.ConsommationElectriqueDTO;
import com.irrigation.energy.dto.PompeDTO;
import com.irrigation.energy.service.ConsommationService;
import com.irrigation.energy.service.PompeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EnergyController {

    private final PompeService pompeService;
    private final ConsommationService consommationService;

    // ============ Pompe Endpoints ============

    @PostMapping("/pompes")
    public ResponseEntity<PompeDTO> createPompe(@RequestBody PompeDTO pompeDTO) {
        PompeDTO created = pompeService.createPompe(pompeDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/pompes")
    public ResponseEntity<List<PompeDTO>> getAllPompes() {
        return ResponseEntity.ok(pompeService.getAllPompes());
    }

    @GetMapping("/pompes/{id}")
    public ResponseEntity<PompeDTO> getPompeById(@PathVariable Long id) {
        return ResponseEntity.ok(pompeService.getPompeById(id));
    }

    @PutMapping("/pompes/{id}")
    public ResponseEntity<PompeDTO> updatePompe(@PathVariable Long id, @RequestBody PompeDTO pompeDTO) {
        return ResponseEntity.ok(pompeService.updatePompe(id, pompeDTO));
    }

    @DeleteMapping("/pompes/{id}")
    public ResponseEntity<Void> deletePompe(@PathVariable Long id) {
        pompeService.deletePompe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pompes/statut/{statut}")
    public ResponseEntity<List<PompeDTO>> getPompesByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(pompeService.getPompesByStatut(statut));
    }

    // ============ Consommation Endpoints ============

    @PostMapping("/consommations")
    public ResponseEntity<ConsommationElectriqueDTO> createConsommation(@RequestBody ConsommationElectriqueDTO dto) {
        ConsommationElectriqueDTO created = consommationService.createConsommation(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/consommations")
    public ResponseEntity<List<ConsommationElectriqueDTO>> getAllConsommations() {
        return ResponseEntity.ok(consommationService.getAllConsommations());
    }

    @GetMapping("/consommations/{id}")
    public ResponseEntity<ConsommationElectriqueDTO> getConsommationById(@PathVariable Long id) {
        return ResponseEntity.ok(consommationService.getConsommationById(id));
    }

    @GetMapping("/consommations/pompe/{pompeId}")
    public ResponseEntity<List<ConsommationElectriqueDTO>> getConsommationsByPompe(@PathVariable Long pompeId) {
        return ResponseEntity.ok(consommationService.getConsommationsByPompeId(pompeId));
    }

    @GetMapping("/consommations/pompe/{pompeId}/total")
    public ResponseEntity<Double> getTotalConsommation(@PathVariable Long pompeId) {
        return ResponseEntity.ok(consommationService.getTotalConsommationByPompe(pompeId));
    }

    // ============ Energy Management Endpoints ============

    @GetMapping("/energy/disponibilite/{pompeId}")
    public ResponseEntity<Boolean> checkDisponibilite(@PathVariable Long pompeId) {
        boolean disponible = pompeService.isPompeDisponible(pompeId);
        return ResponseEntity.ok(disponible);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"UP\",\"service\":\"energy-service\"}");
    }
}
