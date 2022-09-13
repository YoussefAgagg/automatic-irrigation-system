package com.github.youssefagagg.automaticirrigationsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.youssefagagg.automaticirrigationsystem.domain.enumeration.Status;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Sensor.
 */
@Document(collection = "sensor")
@Data
public class Sensor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("sensor_code")
    private String sensorCode;

    @Field("status")
    private Status status;
    @Field("plot")
    @ToString.Exclude
    @JsonIgnoreProperties(value = {"sensor", "slots"}, allowSetters = true)
    private Plot plot;

}
