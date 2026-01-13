package com.irrigation.energy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PompeDTO {
    private Long id;
    private String reference;
    private Double puissance;
    private String statut;
    private LocalDate dateMiseEnService;
}
