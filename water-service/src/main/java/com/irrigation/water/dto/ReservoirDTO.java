package com.irrigation.water.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservoirDTO {
    private Long id;
    private String nom;
    private Double capaciteTotale;
    private Double volumeActuel;
    private String localisation;
}
