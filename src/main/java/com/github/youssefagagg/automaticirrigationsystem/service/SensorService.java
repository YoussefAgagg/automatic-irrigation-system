package com.github.youssefagagg.automaticirrigationsystem.service;


import com.github.youssefagagg.automaticirrigationsystem.aop.logging.Loggable;
import com.github.youssefagagg.automaticirrigationsystem.dto.PlotDTO;
import com.github.youssefagagg.automaticirrigationsystem.dto.SensorDTO;
import com.github.youssefagagg.automaticirrigationsystem.dto.mapper.PlotMapper;
import com.github.youssefagagg.automaticirrigationsystem.dto.mapper.SensorMapper;
import com.github.youssefagagg.automaticirrigationsystem.exception.NoMoreThanOneSensorAllowed;
import com.github.youssefagagg.automaticirrigationsystem.exception.ResourceNotFoundException;
import com.github.youssefagagg.automaticirrigationsystem.repository.PlotRepository;
import com.github.youssefagagg.automaticirrigationsystem.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.github.youssefagagg.automaticirrigationsystem.domain.Sensor}.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;

    private final SensorMapper sensorMapper;

    private final PlotRepository plotRepository;

    private final MongoTemplate template;


    @Loggable
    public Mono<SensorDTO> attachSensorToPlot(String plotId, SensorDTO sensorDTO) {
        return plotRepository.findById(plotId).switchIfEmpty(Mono.error(new ResourceNotFoundException("plot doesn't exist")))
                .map(plot -> {
                    var sensor= template.save(sensorMapper.toEntity(sensorDTO));
                    plot.setSensor(sensor);
                    template.save(plot);
                    return sensor;
                }).map(sensorMapper::toDto);

    }

    /**
     * Update a sensor.
     *
     * @param sensorDTO the entity to save.
     * @return the persisted entity.
     */
    @Loggable
    public Mono<SensorDTO> update(SensorDTO sensorDTO) {
        log.debug("Request to update Sensor : {}", sensorDTO);
        return sensorRepository.save(sensorMapper.toEntity(sensorDTO)).map(sensorMapper::toDto);
    }


    /**
     * Get all the sensors.
     *
     * @return the list of entities.
     */
    @Loggable
    public Flux<SensorDTO> findAll() {
        log.debug("Request to get all Sensors");
        return sensorRepository.findAll().map(sensorMapper::toDto);
    }



    /**
     * Get one sensor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Loggable
    public Mono<SensorDTO> findOne(String id) {
        log.debug("Request to get Sensor : {}", id);
        return sensorRepository.findById(id).map(sensorMapper::toDto);
    }

    /**
     * Delete the sensor by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    @Loggable
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Sensor : {}", id);
        return sensorRepository.deleteById(id);
    }
}
