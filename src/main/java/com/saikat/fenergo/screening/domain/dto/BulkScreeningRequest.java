package com.saikat.fenergo.screening.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkScreeningRequest {
    private List<String> ids;
}