package com.saikat.fenergo.screening.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerStatusResponse {
    private String id;
    private String status;
}