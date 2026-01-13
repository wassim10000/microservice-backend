package com.irrigation.water.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurconsommationEvent {
    private Long pompeId;
    private Double energieUtilisee;
    private Double threshold;
    private LocalDateTime dateMesure;
    private String message;
}
