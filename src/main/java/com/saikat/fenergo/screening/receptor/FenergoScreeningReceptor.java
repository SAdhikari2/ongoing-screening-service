package com.saikat.fenergo.screening.receptor;

import com.saikat.fenergo.screening.adapter.ScreeningAdapter;
import com.saikat.fenergo.screening.domain.dto.BulkScreeningRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FenergoScreeningReceptor {

    private final ScreeningAdapter adapter;

    public Mono<Void> accept(BulkScreeningRequest request) {
        return adapter.execute(request);
    }
}