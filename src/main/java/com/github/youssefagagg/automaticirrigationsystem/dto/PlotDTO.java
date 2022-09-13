package com.github.youssefagagg.automaticirrigationsystem.dto;

import com.github.youssefagagg.automaticirrigationsystem.domain.Slot;
import com.github.youssefagagg.automaticirrigationsystem.domain.enumeration.CropType;
import lombok.Data;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.github.youssefagagg.automaticirrigationsystem.domain.Plot} entity.
 */
@Data
public class PlotDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String plotCode;

    @NotNull(message = "must not be null")
    private Double plotLength;

    @NotNull(message = "must not be null")
    private Double plotWidth;

    private Boolean isIrrigated;

    private Integer triesCount;

    private Boolean hasAlert;

    private Integer waterAmount;

    private Integer sensorCallCount;

    private Instant lastIrrigationTime;

    private Instant startIrrigationTime;

    private CropType cropType;

    private SensorDTO sensor;

    private List<Slot> slots;

}
