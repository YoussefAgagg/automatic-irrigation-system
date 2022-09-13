package com.github.youssefagagg.automaticirrigationsystem.service;

import com.github.youssefagagg.automaticirrigationsystem.aop.logging.Loggable;
import com.github.youssefagagg.automaticirrigationsystem.domain.Plot;
import com.github.youssefagagg.automaticirrigationsystem.domain.enumeration.Status;
import com.github.youssefagagg.automaticirrigationsystem.repository.PlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@Slf4j
@RequiredArgsConstructor
public class SensorCallingScheduler {

  private static final int TIME_SLEEP = 1000 * 30;
  private final PlotRepository plotRepository;

  private final MongoTemplate template;
  @Value("${tries.count}")
  private int triesCount = 10;

  @Async
  @Loggable
  public void tryToConnectToSensor(Plot plot) {
    log.debug("try to connect to sensor  '{}'", plot);

    while (triesCount > 0) {
      log.debug("try to connect sensor tries remain: {}", triesCount);
      triesCount--;
      plot = plotRepository.findById(plot.getId()).block();

      log.debug("current plot status {}", plot);

      if (plot.getSensor().getStatus() == Status.UP) {
        if (plot.getSensorCallCount() != 0) {
          log.debug("sensor is up now after {} tries", triesCount);
        }
        UpdatePlotIrrigationSuccess(plot);
        break;
      }

      plot.setSensorCallCount(plot.getSensorCallCount() + 1);
      plot.setLastSensorCallTime(Instant.now());
      plot =plotRepository.save(plot).block();
      if (triesCount != 0) {
        sleepTillNextTry();
      }
    }

    if (triesCount == 0) {
      plot.setHasAlert(true);
    }

    plotRepository.save(plot).block();
  }

  private void UpdatePlotIrrigationSuccess(Plot plot) {
    plot.setHasAlert(false);
    plot.setSensorCallCount(0);
    plot.setLastSensorCallTime(null);
    plot.setLastIrrigationTime(Instant.now());
    plot.setStartIrrigationTime(Instant.now());
    plot.setIsIrrigated(true);
    plot.getSlots().forEach(plotSlot ->{
      plotSlot.setStatus(Status.UP);
      template.save(plotSlot);
    }
    );
  }


  private void sleepTillNextTry() {
    try {
      Thread.sleep(TIME_SLEEP);
    } catch (InterruptedException e) {
      log.error("interrupted '{}'", e.getMessage());
    }
  }
}
