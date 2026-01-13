package com.irrigation.energy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsommationElectriqueDTO {
    private Long id;
    private Long pompeId;
    private Double energieUtilisee;
    private Double duree;
    private LocalDateTime dateMesure;
}
