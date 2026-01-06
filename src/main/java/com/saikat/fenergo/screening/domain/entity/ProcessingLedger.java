package com.saikat.fenergo.screening.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("processing_ledger")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingLedger
{
    @Id
    private String id;

    private String status; // PENDING | FAILED | COMPLETED

    private int attempts;

    private String providerResponse;

    private Instant updatedAt;
}
