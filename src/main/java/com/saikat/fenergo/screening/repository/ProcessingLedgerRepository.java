package com.saikat.fenergo.screening.repository;

import com.saikat.fenergo.screening.domain.entity.ProcessingLedger;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProcessingLedgerRepository
        extends ReactiveMongoRepository<ProcessingLedger, String> {
}