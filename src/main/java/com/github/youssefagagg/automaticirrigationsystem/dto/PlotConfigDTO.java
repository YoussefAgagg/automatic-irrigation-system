package com.github.youssefagagg.automaticirrigationsystem.dto;


import com.github.youssefagagg.automaticirrigationsystem.domain.enumeration.CropType;
import lombok.Data;


import javax.validation.constraints.NotNull;

@Data
public class PlotConfigDTO {

  private String id;

  @NotNull
  private CropType cropType;

  @NotNull
  private int waterAmount;

  @NotNull
  private int slotsCount;
}
