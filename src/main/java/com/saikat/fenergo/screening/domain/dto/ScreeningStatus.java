package com.saikat.fenergo.screening.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreeningStatus {
    private String jobId;
    private String status; // IN_PROGRESS, COMPLETED, FAILED
    private int processedCount;
    private int totalCount;
    private Instant startTime;
    private Instant lastUpdated;

    // Convenience constructor
    public ScreeningStatus(String status) {
        this.status = status;
        this.startTime = Instant.now();
        this.lastUpdated = Instant.now();
    }
}