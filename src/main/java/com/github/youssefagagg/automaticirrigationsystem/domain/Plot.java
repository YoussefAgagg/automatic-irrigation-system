package com.github.youssefagagg.automaticirrigationsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.github.youssefagagg.automaticirrigationsystem.domain.enumeration.CropType;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Plot.
 */
@Document(collection = "plot")
@Data
public class Plot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("plot_code")
    private String plotCode;

    @NotNull(message = "must not be null")
    @Field("plot_length")
    private Double plotLength;

    @NotNull(message = "must not be null")
    @Field("plot_width")
    private Double plotWidth;

    @Field("is_irrigated")
    private Boolean isIrrigated;

    @Field("tries_count")
    private Integer triesCount;

    @Field("has_alert")
    private Boolean hasAlert =false;

    @Field("water_amount")
    private Integer waterAmount;

    @Field("last_irrigation_time")
    private Instant lastIrrigationTime;

    @Field("start_irrigation_time")
    private Instant startIrrigationTime;

    @Field("last_sensor_call_time")
    private Instant lastSensorCallTime;

    @Field("crop_type")
    private CropType cropType;

    @Field("sensor_call_count")
    private Integer sensorCallCount = 0;

    @Field
    @DBRef
    private Sensor sensor;

    @Field
    @DBRef
    private List<Slot> slots = new ArrayList<>();


}
