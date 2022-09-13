package com.github.youssefagagg.automaticirrigationsystem.dto;

import com.github.youssefagagg.automaticirrigationsystem.domain.enumeration.Status;
import lombok.Data;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.github.youssefagagg.automaticirrigationsystem.domain.Slot} entity.
 */
@Data
public class SlotDTO implements Serializable {

    private String id;

    private Status status;

   // private PlotDTO plot;

}
