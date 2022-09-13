package com.github.youssefagagg.automaticirrigationsystem.dto.mapper;


import com.github.youssefagagg.automaticirrigationsystem.domain.Plot;
import com.github.youssefagagg.automaticirrigationsystem.domain.Sensor;
import com.github.youssefagagg.automaticirrigationsystem.dto.PlotDTO;
import com.github.youssefagagg.automaticirrigationsystem.dto.SensorDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Plot} and its DTO {@link PlotDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlotMapper extends EntityMapper<PlotDTO, Plot> {

}
