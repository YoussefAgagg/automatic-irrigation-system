package com.github.youssefagagg.automaticirrigationsystem.web.rest;


import com.github.youssefagagg.automaticirrigationsystem.aop.logging.Loggable;
import com.github.youssefagagg.automaticirrigationsystem.dto.PlotConfigDTO;
import com.github.youssefagagg.automaticirrigationsystem.dto.PlotDTO;
import com.github.youssefagagg.automaticirrigationsystem.exception.BadRequestAlertException;
import com.github.youssefagagg.automaticirrigationsystem.exception.ResourceNotFoundException;
import com.github.youssefagagg.automaticirrigationsystem.repository.PlotRepository;
import com.github.youssefagagg.automaticirrigationsystem.service.PlotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.github.youssefagagg.automaticirrigationsystem.domain.Plot}.
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class PlotResource {
    private final PlotService plotService;

    private final PlotRepository plotRepository;


    /**
     * {@code POST  /plots} : Create a new plot.
     *
     * @param plotDTO the plotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plotDTO, or with status {@code 400 (Bad Request)} if the plot has already an ID.
     */
    @PostMapping("/plots")
    @Loggable
    public Mono<ResponseEntity<PlotDTO>> createPlot(@Valid @RequestBody PlotDTO plotDTO) {
        log.debug("REST request to save Plot : {}", plotDTO);
        if (plotDTO.getId() != null) {
            throw new BadRequestAlertException("A new plot cannot already have an ID exists");
        }
        return plotService
                .save(plotDTO)
                .map(result -> {
                    try {
                        return ResponseEntity
                                .created(new URI("/api/plots/" + result.getId()))
                                .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * {@code PUT  /plots/:id} : Updates an existing plot.
     *
     * @param id      the id of the plotDTO to save.
     * @param plotDTO the plotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plotDTO,
     * or with status {@code 400 (Bad Request)} if the plotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plotDTO couldn't be updated.
     */
    @PutMapping("/plots/{id}")
    @Loggable
    public Mono<ResponseEntity<PlotDTO>> updatePlot(
            @PathVariable(value = "id") final String id,
            @Valid @RequestBody PlotDTO plotDTO
    ) {
        log.debug("REST request to update Plot : {}, {}", id, plotDTO);
        plotDTO.setId(id);
        return plotRepository
                .existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new ResourceNotFoundException("plot doesn't exist"));
                    }

                    Mono<PlotDTO> result = plotService.partialUpdate(plotDTO);

                    return result
                            .switchIfEmpty(Mono.error(new ResourceNotFoundException("plot doesn't exist")))
                            .map(res -> ResponseEntity.ok().body(res));
                });
    }

    @PutMapping("/plots/config/{id}")
    @Loggable
    public Mono<ResponseEntity<PlotDTO>> configPlot(
            @PathVariable final String id,
            @Valid @RequestBody PlotConfigDTO plotConfigDTO) {
        log.debug("REST request to configure Plot : {}, {}", id, plotConfigDTO);
        Mono<PlotDTO> result = plotService.configurePlot(plotConfigDTO, id);
        return result.switchIfEmpty(Mono.error(new ResourceNotFoundException("plot doesn't exist")))
                .map(res -> ResponseEntity.ok().body(res));

    }

    /**
     * {@code GET  /plots} : get all the plots.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plots in body.
     */
    @GetMapping("/plots")
    @Loggable
    public Mono<List<PlotDTO>> getAllPlots() {
        log.debug("REST request to get  all  Plots");
        return plotService.findAll().collectList();
    }

    /**
     * {@code GET  /plots/:id} : get the "id" plot.
     *
     * @param id the id of the plotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plots/{id}")
    @Loggable
    public Mono<ResponseEntity<PlotDTO>> getPlot(@PathVariable String id) {
        log.debug("REST request to get Plot : {}", id);
        Mono<PlotDTO> plotDTO = plotService.findOne(id);
        return plotDTO.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(res -> ResponseEntity.ok().body(res));

    }

    /**
     * {@code DELETE  /plots/:id} : delete the "id" plot.
     *
     * @param id the id of the plotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plots/{id}")
    @Loggable
    public Mono<ResponseEntity<Void>> deletePlot(@PathVariable String id) {
        log.debug("REST request to delete Plot : {}", id);
        return plotService
                .delete(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
