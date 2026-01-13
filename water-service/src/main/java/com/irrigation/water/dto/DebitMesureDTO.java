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
public class DebitMesureDTO {
    private Long id;
    private Long pompeId;
    private Double debit;
    private LocalDateTime dateMesure;
    private String unite;
}
