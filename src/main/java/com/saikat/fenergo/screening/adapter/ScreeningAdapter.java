package com.saikat.fenergo.screening.adapter;

import com.saikat.fenergo.screening.domain.dto.BulkScreeningRequest;
import com.saikat.fenergo.screening.provider.CitiScreeningProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ScreeningAdapter {

    private final CitiScreeningProvider provider;

    public Mono<Void> execute(BulkScreeningRequest request) {
        return provider.process(request);
    }
}
