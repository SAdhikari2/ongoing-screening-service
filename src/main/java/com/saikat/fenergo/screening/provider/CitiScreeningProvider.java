package com.saikat.fenergo.screening.provider;

import com.saikat.fenergo.screening.domain.dto.BulkScreeningRequest;
import com.saikat.fenergo.screening.domain.dto.PartnerStatusResponse;
import com.saikat.fenergo.screening.domain.entity.ControlState;
import com.saikat.fenergo.screening.domain.entity.DlqRecord;
import com.saikat.fenergo.screening.domain.entity.ProcessingLedger;
import com.saikat.fenergo.screening.domain.entity.TransactionEntity;
import com.saikat.fenergo.screening.repository.ControlStateRepository;
import com.saikat.fenergo.screening.repository.DlqRepository;
import com.saikat.fenergo.screening.repository.ProcessingLedgerRepository;
import com.saikat.fenergo.screening.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CitiScreeningProvider {

    private static final int BATCH_SIZE = 100;

    private final TransactionRepository transactionRepo;
    private final ProcessingLedgerRepository ledgerRepo;
    private final ControlStateRepository controlRepo;
    private final DlqRepository dlqRepo;

    @Qualifier("screeningWebClient")
    private final WebClient webClient;

    public Mono<Void> process(BulkScreeningRequest request) {
        log.info("Processing request for {} transactions", request.getIds().size());

        return controlRepo.findById("ONGOING_SCREENING")
                .defaultIfEmpty(new ControlState("ONGOING_SCREENING", "0", LocalDate.now()))
                .flatMapMany(control -> {
                    log.debug("Control state: {}", control);
                    return transactionRepo.findByIdGreaterThan(control.getLastProcessedTxnId())
                            .filter(txn -> request.getIds().contains(txn.getId()))
                            .buffer(BATCH_SIZE);
                })
                .flatMap(this::processBatch, 10)
                .then()
                .doOnSuccess(v -> log.info("Processing completed successfully"))
                .doOnError(e -> log.error("Processing failed", e));
    }

    private Mono<Void> processBatch(List<TransactionEntity> batch) {
        if (batch.isEmpty()) {
            return Mono.empty();
        }

        String lastTxnId = batch.get(batch.size() - 1).getId();
        log.debug("Processing batch of {} transactions", batch.size());

        return Flux.fromIterable(batch)
                .flatMap(this::processSingleTransaction, 5)
                .then(updateControlState(lastTxnId));
    }

    private Mono<Void> processSingleTransaction(TransactionEntity txn) {
        return webClient.post()
                .uri("/partner/status")
                .bodyValue(txn.getId())
                .retrieve()
                .bodyToMono(PartnerStatusResponse.class)
                .flatMap(response -> ledgerRepo.save(
                        ProcessingLedger.builder()
                                .id(txn.getId())
                                .status(response.getStatus())
                                .attempts(1)
                                .providerResponse("Partner response: " + response.getStatus())
                                .updatedAt(Instant.now())
                                .build()
                ).then())
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().is4xxClientError()) {
                        log.warn("4xx error for {}: {}", txn.getId(), ex.getStatusCode());
                        return dlqRepo.save(
                                DlqRecord.builder()
                                        .id(txn.getId())
                                        .reason("4xx error: " + ex.getStatusCode())
                                        .createdAt(Instant.now())
                                        .build()
                        ).then();
                    }
                    log.error("WebClient error for {}: {}", txn.getId(), ex.getMessage());
                    return Mono.empty();
                })
                .onErrorResume(Throwable.class, ex -> {
                    log.error("Error processing {}: {}", txn.getId(), ex.getMessage());
                    return Mono.empty();
                });
    }

    private Mono<Void> updateControlState(String lastTxnId) {
        return controlRepo.findById("ONGOING_SCREENING")
                .defaultIfEmpty(new ControlState("ONGOING_SCREENING", "", LocalDate.now()))
                .flatMap(control -> {
                    control.setLastProcessedTxnId(lastTxnId);
                    control.setJobDay(LocalDate.now());
                    return controlRepo.save(control);
                })
                .then();
    }
}