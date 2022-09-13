package com.github.youssefagagg.automaticirrigationsystem.repository;


import com.github.youssefagagg.automaticirrigationsystem.domain.Plot;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Arrays;

/**
 * Spring Data MongoDB reactive repository for the Plot entity.
 */

@Repository
public interface PlotRepository extends ReactiveMongoRepository<Plot, String> {
    Flux<Plot> findAllBy(Pageable pageable);

     Flux<Plot> findAllByHasAlertIsTrue();
}
