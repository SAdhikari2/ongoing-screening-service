package com.saikat.fenergo.screening.controller;

import com.saikat.fenergo.screening.domain.dto.PartnerStatusResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Random;

@RestController
@RequestMapping("/partner")
public class MockPartnerController {

    private final Random random = new Random();

    @PostMapping("/status")
    public Mono<PartnerStatusResponse> status(@RequestBody String txnId) {
        // Simulate random response like in your original PartnerStatusController
        String status = switch (random.nextInt(3)) {
            case 0 -> "PENDING";
            case 1 -> "FAILED";
            default -> "COMPLETED";
        };

        return Mono.just(new PartnerStatusResponse(txnId, status));
    }
}