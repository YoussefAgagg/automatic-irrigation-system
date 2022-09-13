package com.github.youssefagagg.automaticirrigationsystem.dto.mapper;

import com.github.youssefagagg.automaticirrigationsystem.domain.Sensor;
import com.github.youssefagagg.automaticirrigationsystem.dto.SensorDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Sensor} and its DTO {@link SensorDTO}.
 */
@Mapper(componentModel = "spring")
public interface SensorMapper extends EntityMapper<SensorDTO, Sensor> {}
