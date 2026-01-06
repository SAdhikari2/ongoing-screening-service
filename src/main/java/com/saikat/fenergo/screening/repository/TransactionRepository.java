package com.saikat.fenergo.screening.repository;

import com.saikat.fenergo.screening.domain.entity.TransactionEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepository
        extends ReactiveMongoRepository<TransactionEntity, String> {

    @Query("{ '_id': { $gt: ?0 } }")
    Flux<TransactionEntity> findByIdGreaterThan(String txnId);

    // Add this method for better handling
    default Flux<TransactionEntity> findByIdGreaterThanOrAll(String txnId) {
        if (txnId == null || txnId.isEmpty() || txnId.equals("0")) {
            return findAll();
        }
        return findByIdGreaterThan(txnId);
    }
}