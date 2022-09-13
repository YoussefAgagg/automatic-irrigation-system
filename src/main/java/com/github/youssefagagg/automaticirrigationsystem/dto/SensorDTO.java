package com.github.youssefagagg.automaticirrigationsystem.dto;

import com.github.youssefagagg.automaticirrigationsystem.domain.enumeration.Status;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.github.youssefagagg.automaticirrigationsystem.domain.Sensor} entity.
 */
@Data
public class SensorDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String sensorCode;

    private Status status;

}
