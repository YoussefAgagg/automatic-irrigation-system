package com.github.youssefagagg.automaticirrigationsystem.web.rest;


import com.github.youssefagagg.automaticirrigationsystem.aop.logging.Loggable;
import com.github.youssefagagg.automaticirrigationsystem.dto.PlotDTO;
import com.github.youssefagagg.automaticirrigationsystem.service.PlotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class AlertingController {

  private final PlotService plotService;

  /**
   * {@code GET  /alert} : show up all plots with alarm.
   *
   * @return the {@link PlotDTO} with status {@code 200 (Ok)}
   */
  @GetMapping("/alert")
  @Loggable
  public Mono<List<PlotDTO>> showUpPlotsWithAlert() {
    log.debug("REST request to show up plots with alarm");
    Flux<PlotDTO> result = plotService.getAllPlotsHasAlarm();
    return result.collectList();
  }
}
