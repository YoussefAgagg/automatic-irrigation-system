package com.github.youssefagagg.automaticirrigationsystem.service;

import com.github.youssefagagg.automaticirrigationsystem.aop.logging.Loggable;
import com.github.youssefagagg.automaticirrigationsystem.domain.Plot;
import com.github.youssefagagg.automaticirrigationsystem.domain.enumeration.Status;
import com.github.youssefagagg.automaticirrigationsystem.dto.PlotDTO;
import com.github.youssefagagg.automaticirrigationsystem.dto.mapper.PlotMapper;
import com.github.youssefagagg.automaticirrigationsystem.exception.BadRequestAlertException;
import com.github.youssefagagg.automaticirrigationsystem.exception.PlotHasAlreadyStartedToBeIrrigated;
import com.github.youssefagagg.automaticirrigationsystem.exception.ResourceNotFoundException;
import com.github.youssefagagg.automaticirrigationsystem.exception.SensorCantBeReachedException;
import com.github.youssefagagg.automaticirrigationsystem.repository.PlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Service implementation for manipulating plot irrigation {@link com.github.youssefagagg.automaticirrigationsystem.domain.Plot}.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class IrrigationService {

  private final PlotRepository plotRepository;
  private final PlotMapper plotMapper;
  private final SensorCallingScheduler sensorCallingScheduler;

  private final MongoTemplate template;

  @Value("${tries.count}")
  private int triesCount = 10;

  /**
   * start irrigate a plot.
   *
   * @param plotId the plot id to start irrigate.
   * @return the persisted plot.
   */
  @Loggable
  public Mono<PlotDTO> startIrrigate(String plotId) {
    log.debug("Request to start irrigate a Plot : {}", plotId);
    Mono<Plot> existPlot = plotRepository.findById(plotId);
   return existPlot.map(plot -> {
              if (plot.getSensorCallCount() > 0 && !plot.getHasAlert()) {
                throw new SensorCantBeReachedException(
                        "Be patient!"
                                + " sensor is scheduled to be called " + plot.getSensorCallCount() + "/"
                                + triesCount + " " + plot.getLastSensorCallTime());
              } else {
                if (plot.getSensor().getStatus() == Status.UP) {
                  if (plot.getIsIrrigated()) {
                    throw new PlotHasAlreadyStartedToBeIrrigated(
                            "Irrigation has already  started by: " + plot.getStartIrrigationTime());
                  }
                  updatePlotIrrigationSuccess(plot);
                } else {
                  if (plot.getHasAlert()) {
                    throw new SensorCantBeReachedException(
                            "Sensor is DOWN, the Plot has alert ON, please try to fix  the sensor first");
                  }
                  sensorCallingScheduler.tryToConnectToSensor(plot);
                  throw new SensorCantBeReachedException(
                          "Sensor is DOWN, a time schedule is arranged to re-call the sensor");
                }
              }

              return plot;
            }
            ).flatMap(plotRepository::save).map(plotMapper::toDto);

  }

  private void updatePlotIrrigationSuccess(Plot plot) {
    plot.setHasAlert(false);
    plot.setSensorCallCount(0);
    plot.setLastSensorCallTime(null);
    plot.setLastIrrigationTime(Instant.now());
    plot.setStartIrrigationTime(Instant.now());
    plot.setIsIrrigated(true);
    plot.setHasAlert(false);
    plot.getSlots().forEach(plotSlot -> {
      plotSlot.setStatus(Status.UP);
      template.save(plotSlot);
    });
  }


  @Loggable
  public Mono<PlotDTO> endIrrigate(String plotId) {
    log.debug("Request to end irrigate a Plot : {}", plotId);
    Mono<Plot> existPlot = plotRepository.findById(plotId);

    return existPlot.map(plot -> {
      if (Boolean.FALSE.equals(plot.getIsIrrigated())) {
        throw new BadRequestAlertException("Please start irrigate first!");
      }
      plot.setIsIrrigated(false);
      plot.setLastIrrigationTime(Instant.now());
      plot.getSlots().forEach(plotSlot -> {
        plotSlot.setStatus(Status.DOWN);
        template.save(plotSlot);
      });

      return plot;
    }).flatMap(plotRepository::save).map(plotMapper::toDto);
  }
}
