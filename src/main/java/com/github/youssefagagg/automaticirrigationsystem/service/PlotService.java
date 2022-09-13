package com.github.youssefagagg.automaticirrigationsystem.service;

import com.github.youssefagagg.automaticirrigationsystem.aop.logging.Loggable;
import com.github.youssefagagg.automaticirrigationsystem.domain.Plot;
import com.github.youssefagagg.automaticirrigationsystem.domain.Slot;
import com.github.youssefagagg.automaticirrigationsystem.domain.enumeration.Status;
import com.github.youssefagagg.automaticirrigationsystem.dto.PlotConfigDTO;
import com.github.youssefagagg.automaticirrigationsystem.dto.PlotDTO;
import com.github.youssefagagg.automaticirrigationsystem.dto.mapper.PlotMapper;
import com.github.youssefagagg.automaticirrigationsystem.exception.NoMoreThanOneSensorAllowed;
import com.github.youssefagagg.automaticirrigationsystem.exception.ResourceNotFoundException;
import com.github.youssefagagg.automaticirrigationsystem.repository.PlotRepository;
import com.github.youssefagagg.automaticirrigationsystem.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Service Implementation for managing {@link com.github.youssefagagg.automaticirrigationsystem.domain.Plot}.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PlotService {

    private final PlotRepository plotRepository;

    private final PlotMapper plotMapper;

    private final MongoTemplate template;

    /**
     * Save a plot.
     *
     * @param plotDTO the entity to save.
     * @return the persisted entity.
     */
    @Loggable
    public Mono<PlotDTO> save(PlotDTO plotDTO) {
        log.debug("Request to save Plot : {}", plotDTO);
        setPlotToDefault(plotDTO);
        return plotRepository.save(plotMapper.toEntity(plotDTO)).map(plotMapper::toDto);
    }
    @Loggable
    public Mono<PlotDTO> configurePlot(PlotConfigDTO plotConfigDTO, String id) {
        log.debug("Request to configure Plot : {}", plotConfigDTO);
        Mono<Plot> existPlot = plotRepository.findById(id);

        return existPlot.map(plot -> {
                    plot.setId(id);
                    plot.setCropType(plotConfigDTO.getCropType());
                    plot.setWaterAmount(plotConfigDTO.getWaterAmount());
                    plot.setSlots(configurePlotAddingSlots(plot, plotConfigDTO.getSlotsCount()));
                    log.debug("it's working here");
                    return plot;
                })
                .flatMap(plotRepository::save)
                .log("is still working")
                .map(plotMapper::toDto);
    }


    /**
     * Partially update a plot.
     *
     * @param plotDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Loggable
    public Mono<PlotDTO> partialUpdate(PlotDTO plotDTO) {
        log.debug("Request to partially update Plot : {}", plotDTO);

        return plotRepository
            .findById(plotDTO.getId())
            .map(existingPlot -> {
                plotMapper.partialUpdate(existingPlot, plotDTO);

                return existingPlot;
            })
            .flatMap(plotRepository::save)
            .map(plotMapper::toDto);
    }

    /**
     * Get all the plots.
     *
     * @return the list of entities.
     */
    @Loggable
    public Flux<PlotDTO> findAll() {
        log.debug("Request to get all Plots");
        return plotRepository.findAll().map(plotMapper::toDto);
    }


    /**
     * Get one plot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Loggable
    public Mono<PlotDTO> findOne(String id) {
        log.debug("Request to get Plot : {}", id);
        return plotRepository.findById(id).map(plotMapper::toDto);
    }

    /**
     * Delete the plot by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    @Loggable
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Plot : {}", id);
       return plotRepository.findById(id).map(plot -> {
            if (plot.getSensor()!=null)
                template.remove(plot.getSensor());
            Query q = new Query(where("_id").in(plot.getSlots().stream().map(slot -> slot.getId()).collect(Collectors.toList())));
            template.findAllAndRemove(q,Slot.class);
            return plot;
        }).flatMap(plotRepository::delete);

    }
    private List<Slot> configurePlotAddingSlots(
            Plot plot, int slotsCount) {
       return IntStream
                .rangeClosed(0,slotsCount)
                .mapToObj(i -> {
                    var slot =new Slot();

                    slot.setStatus(Status.DOWN);
                    return template.save(slot);
                }).collect(Collectors.toList());
    }
    private PlotDTO setPlotToDefault(PlotDTO plot) {
        plot.setIsIrrigated(false);
        plot.setTriesCount(0);
        plot.setHasAlert(false);
        plot.setStartIrrigationTime(null);
        plot.setLastIrrigationTime(null);
        plot.setWaterAmount(0);
        return plot;
    }

    public Flux<PlotDTO> getAllPlotsHasAlarm() {

        return plotRepository.findAllByHasAlertIsTrue()
                .map(plotMapper::toDto);
    }


}
