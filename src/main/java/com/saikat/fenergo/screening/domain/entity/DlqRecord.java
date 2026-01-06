package com.saikat.fenergo.screening.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("dlq")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DlqRecord {

    @Id
    private String id;

    private String reason;

    private Instant createdAt;
}
