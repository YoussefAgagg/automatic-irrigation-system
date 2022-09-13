package com.github.youssefagagg.automaticirrigationsystem.web.rest;

import com.github.youssefagagg.automaticirrigationsystem.dto.SensorDTO;
import com.github.youssefagagg.automaticirrigationsystem.exception.BadRequestAlertException;
import com.github.youssefagagg.automaticirrigationsystem.exception.ResourceNotFoundException;
import com.github.youssefagagg.automaticirrigationsystem.repository.SensorRepository;
import com.github.youssefagagg.automaticirrigationsystem.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

/**
 * REST controller for managing {@link com.github.youssefagagg.automaticirrigationsystem.domain.Sensor}.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class SensorResource {
    private final SensorService sensorService;

    private final SensorRepository sensorRepository;


    /**
     * {@code POST  /sensors} : Create a new sensor.
     *
     * @param sensorDTO the sensorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sensorDTO, or with status {@code 400 (Bad Request)} if the sensor has already an ID.
     */
    @PostMapping("/sensors/{plotId}")
    public Mono<ResponseEntity<SensorDTO>> createSensor(@Valid @RequestBody SensorDTO sensorDTO,
    @PathVariable String plotId) {
        log.debug("REST request to save Sensor : {}", sensorDTO);
        if (sensorDTO.getId() != null) {
            throw new BadRequestAlertException("A new sensor cannot already have an ID exists");
        }
        return sensorService
            .attachSensorToPlot(plotId,sensorDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/sensors/" + result.getId()))
                            .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /sensors/:id} : Updates an existing sensor.
     *
     * @param id the id of the sensorDTO to save.
     * @param sensorDTO the sensorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensorDTO,
     * or with status {@code 400 (Bad Request)} if the sensorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sensorDTO couldn't be updated.
     */
    @PutMapping("/sensors/{id}")
    public Mono<ResponseEntity<SensorDTO>> updateSensor(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody SensorDTO sensorDTO
    ) {
        log.debug("REST request to update Sensor : {}, {}", id, sensorDTO);
        if (sensorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id null");
        }
        if (!Objects.equals(id, sensorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID invalid");
        }

        return sensorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found"));
                }

                return sensorService
                    .update(sensorDTO)
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("sensor doesn't exist")))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .body(result)
                    );
            });
    }


    /**
     * {@code GET  /sensors} : get all the sensors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sensors in body.
     */
    @GetMapping("/sensors")
    public Mono<List<SensorDTO>> getAllSensors() {
        log.debug("REST request to get a page of Sensors");
        return sensorService.findAll().collectList();
    }

    /**
     * {@code GET  /sensors/:id} : get the "id" sensor.
     *
     * @param id the id of the sensorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sensorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sensors/{id}")
    public Mono<ResponseEntity<SensorDTO>> getSensor(@PathVariable String id) {
        log.debug("REST request to get Sensor : {}", id);
        Mono<SensorDTO> sensorDTO = sensorService.findOne(id);
        return sensorDTO.switchIfEmpty(Mono.error(new ResourceNotFoundException("sensor doesn't exist")))
                .map(res -> ResponseEntity.ok().body(res));
    }

    /**
     * {@code DELETE  /sensors/:id} : delete the "id" sensor.
     *
     * @param id the id of the sensorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sensors/{id}")
    public Mono<ResponseEntity<Void>> deleteSensor(@PathVariable String id) {
        log.debug("REST request to delete Sensor : {}", id);
        return sensorService
            .delete(id)
            .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
