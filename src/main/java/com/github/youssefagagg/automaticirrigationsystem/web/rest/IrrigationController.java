package com.github.youssefagagg.automaticirrigationsystem.web.rest;


import com.github.youssefagagg.automaticirrigationsystem.aop.logging.Loggable;
import com.github.youssefagagg.automaticirrigationsystem.dto.PlotDTO;
import com.github.youssefagagg.automaticirrigationsystem.exception.ResourceNotFoundException;
import com.github.youssefagagg.automaticirrigationsystem.service.IrrigationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


/**
 * REST controller for manipulating irrigation business.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class IrrigationController {

    private final IrrigationService irrigationService;

    /**
     * {@code GET  /irrigate/start/{id}} : start irrigate a plot.
     *
     * @param plotId plot id to start irrigation.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the new
     * PlotDto, or with status {@code 400 (Not Found)} if the plot does not exist.
     */
    @GetMapping("/irrigate/start/{id}")
    @Loggable
    public Mono<ResponseEntity<PlotDTO>> startPlotIrrigation(
            @PathVariable(value = "id") final String plotId) {
        log.debug("REST request to start irrigation : {}", plotId);
        Mono<PlotDTO> result = irrigationService.startIrrigate(plotId);

        return result.switchIfEmpty(Mono.error(new ResourceNotFoundException("plot doesn't exist")))
                .map(res -> ResponseEntity.ok().body(res));
    }

    /**
     * {@code GET  /irrigate/end/{id}} : end irrigate a plot.
     *
     * @param plotId plot id to start irrigation.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
     * plotDto, or with status {@code 400 (Not Found)} if the plot does not exist.
     */
    @GetMapping("/irrigate/end/{id}")
    @Loggable
    public Mono<ResponseEntity<PlotDTO>> endPlotIrrigation(
            @PathVariable(value = "id") final String plotId) {
        log.debug("REST request to start irrigation : {}", plotId);
        var result = irrigationService.endIrrigate(plotId);
        return result.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(res -> ResponseEntity.ok().body(res));
    }


}
