package com.rantomah.drift.api.mapper;

import com.rantomah.drift.api.dto.HealthDto;
import com.rantomah.drift.api.model.Health;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HealthMapper {

    HealthMapper INSTANCE = Mappers.getMapper(HealthMapper.class);

    HealthDto toDto(Health model);
}
