package com.saikat.fenergo.screening.repository;

import com.saikat.fenergo.screening.domain.entity.ControlState;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ControlStateRepository
        extends ReactiveMongoRepository<ControlState, String> {
}