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
public class AlerteDTO {
    private Long id;
    private Long pompeId;
    private String type;
    private String message;
    private Double valeur;
    private LocalDateTime dateAlerte;
    private boolean traitee;
}
