package com.saikat.fenergo.screening.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("control_state")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlState {

    @Id
    private String jobName;

    private String lastProcessedTxnId;

    private LocalDate jobDay;
}
