package com.saikat.fenergo.screening.controller;

import com.saikat.fenergo.screening.domain.dto.BulkScreeningRequest;
import com.saikat.fenergo.screening.receptor.FenergoScreeningReceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fenergo")
@RequiredArgsConstructor
@Slf4j
public class OngoingFenergoScreenController {

    private final FenergoScreeningReceptor receptor;

    @PostMapping("/screening")
    public Mono<ResponseEntity<Void>> screening(
            @RequestBody BulkScreeningRequest request) {

        return receptor.accept(request)
                .doOnSubscribe(s -> log.info("🚀 Starting screening for {} IDs", request.getIds().size()))
                .doOnSuccess(v -> log.info("✅ Screening completed successfully"))
                .doOnError(e -> log.error("❌ Screening failed", e))
                .then(Mono.just(ResponseEntity.accepted().build()));
    }
}