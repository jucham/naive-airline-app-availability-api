package com.jucham.repository;

import com.jucham.repository.entity.FlightAvailabilityEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlightAvailabilityRepository extends CrudRepository<FlightAvailabilityEntity, Integer> {

    List<FlightAvailabilityEntity> findByOriginAirportCodeIgnoreCaseAndDestinationAirportCodeIgnoreCase(String originAirportCode, String destinationAirportCode);


}
