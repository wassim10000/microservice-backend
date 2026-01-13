package com.irrigation.water.web;

import com.irrigation.water.clients.EnergyClient;
import com.irrigation.water.dto.AlerteDTO;
import com.irrigation.water.dto.DebitMesureDTO;
import com.irrigation.water.dto.ReservoirDTO;
import com.irrigation.water.service.AlerteService;
import com.irrigation.water.service.DebitService;
import com.irrigation.water.service.ReservoirService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class WaterController {

    private final ReservoirService reservoirService;
    private final DebitService debitService;
    private final AlerteService alerteService;
    private final EnergyClient energyClient;

    // ============ Reservoir Endpoints ============

    @PostMapping("/reservoirs")
    public ResponseEntity<ReservoirDTO> createReservoir(@RequestBody ReservoirDTO dto) {
        ReservoirDTO created = reservoirService.createReservoir(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/reservoirs")
    public ResponseEntity<List<ReservoirDTO>> getAllReservoirs() {
        return ResponseEntity.ok(reservoirService.getAllReservoirs());
    }

    @GetMapping("/reservoirs/{id}")
    public ResponseEntity<ReservoirDTO> getReservoirById(@PathVariable Long id) {
        return ResponseEntity.ok(reservoirService.getReservoirById(id));
    }

    @PutMapping("/reservoirs/{id}")
    public ResponseEntity<ReservoirDTO> updateReservoir(@PathVariable Long id, @RequestBody ReservoirDTO dto) {
        return ResponseEntity.ok(reservoirService.updateReservoir(id, dto));
    }

    @DeleteMapping("/reservoirs/{id}")
    public ResponseEntity<Void> deleteReservoir(@PathVariable Long id) {
        reservoirService.deleteReservoir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservoirs/{id}/niveau")
    public ResponseEntity<Double> getNiveauRemplissage(@PathVariable Long id) {
        return ResponseEntity.ok(reservoirService.getNiveauRemplissage(id));
    }

    // ============ Debit Endpoints ============

    @PostMapping("/debits")
    public ResponseEntity<DebitMesureDTO> createDebit(@RequestBody DebitMesureDTO dto) {
        DebitMesureDTO created = debitService.createDebitMesure(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/debits")
    public ResponseEntity<List<DebitMesureDTO>> getAllDebits() {
        return ResponseEntity.ok(debitService.getAllDebits());
    }

    @GetMapping("/debits/{id}")
    public ResponseEntity<DebitMesureDTO> getDebitById(@PathVariable Long id) {
        return ResponseEntity.ok(debitService.getDebitById(id));
    }

    @GetMapping("/debits/pompe/{pompeId}")
    public ResponseEntity<List<DebitMesureDTO>> getDebitsByPompe(@PathVariable Long pompeId) {
        return ResponseEntity.ok(debitService.getDebitsByPompeId(pompeId));
    }

    @GetMapping("/debits/pompe/{pompeId}/moyenne")
    public ResponseEntity<Double> getAverageDebit(@PathVariable Long pompeId) {
        return ResponseEntity.ok(debitService.getAverageDebitByPompe(pompeId));
    }

    // ============ Alertes Endpoints ============

    @GetMapping("/alertes")
    public ResponseEntity<List<AlerteDTO>> getAllAlertes() {
        return ResponseEntity.ok(alerteService.getAllAlertes());
    }

    @GetMapping("/alertes/non-traitees")
    public ResponseEntity<List<AlerteDTO>> getAlertesNonTraitees() {
        return ResponseEntity.ok(alerteService.getAlertesNonTraitees());
    }

    @GetMapping("/alertes/pompe/{pompeId}")
    public ResponseEntity<List<AlerteDTO>> getAlertesByPompe(@PathVariable Long pompeId) {
        return ResponseEntity.ok(alerteService.getAlertesByPompe(pompeId));
    }

    @PutMapping("/alertes/{id}/traiter")
    public ResponseEntity<AlerteDTO> marquerAlerteTraitee(@PathVariable Long id) {
        return ResponseEntity.ok(alerteService.marquerCommeTraitee(id));
    }

    // ============ Water-Energy Integration Endpoints ============

    @PostMapping("/water/demarrer-pompe/{pompeId}")
    public ResponseEntity<Map<String, Object>> demarrerPompe(@PathVariable Long pompeId) {
        log.info("Checking energy availability for pump {}", pompeId);

        try {
            Boolean disponible = energyClient.checkDisponibilite(pompeId);

            if (Boolean.TRUE.equals(disponible)) {
                log.info("Pump {} is available, starting irrigation", pompeId);
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "✅ Démarrage de l'irrigation pour la pompe " + pompeId,
                        "pompeId", pompeId));
            } else {
                log.warn("Pump {} is not available", pompeId);
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "❌ Échec : La pompe " + pompeId + " n'est pas disponible",
                        "pompeId", pompeId));
            }
        } catch (Exception e) {
            log.error("Error checking pump availability: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                    "success", false,
                    "message", "⚠️ Erreur de communication avec le service énergie: " + e.getMessage(),
                    "pompeId", pompeId));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"UP\",\"service\":\"water-service\"}");
    }
}
