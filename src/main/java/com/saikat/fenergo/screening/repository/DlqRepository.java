package com.saikat.fenergo.screening.repository;

import com.saikat.fenergo.screening.domain.entity.DlqRecord;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DlqRepository
        extends ReactiveMongoRepository<DlqRecord, String> {
}