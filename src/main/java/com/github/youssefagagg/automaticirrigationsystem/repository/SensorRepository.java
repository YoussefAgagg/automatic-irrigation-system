package com.github.youssefagagg.automaticirrigationsystem.repository;

import com.github.youssefagagg.automaticirrigationsystem.domain.Sensor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Sensor entity.
 */

@Repository
public interface SensorRepository extends ReactiveMongoRepository<Sensor, String> {
    Flux<Sensor> findAllBy(Pageable pageable);
}
