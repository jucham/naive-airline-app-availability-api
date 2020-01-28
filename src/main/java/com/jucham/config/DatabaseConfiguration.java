package com.jucham.config;

import com.jucham.repository.entity.FlightAvailabilityEntity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@Configuration
@EntityScan(basePackageClasses = {FlightAvailabilityEntity.class, Jsr310JpaConverters.class})
public class DatabaseConfiguration {
}
