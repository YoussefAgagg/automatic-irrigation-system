package com.github.youssefagagg.automaticirrigationsystem.dto.mapper;


import com.github.youssefagagg.automaticirrigationsystem.domain.Plot;
import com.github.youssefagagg.automaticirrigationsystem.domain.Slot;
import com.github.youssefagagg.automaticirrigationsystem.dto.PlotDTO;
import com.github.youssefagagg.automaticirrigationsystem.dto.SlotDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Slot} and its DTO {@link SlotDTO}.
 */
@Mapper(componentModel = "spring")
public interface SlotMapper extends EntityMapper<SlotDTO, Slot> {

}
