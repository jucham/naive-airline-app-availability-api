package com.jucham.service.impl;

import com.jucham.dto.FlightAvailability;
import com.jucham.dto.FlightAvailabilityRequest;
import com.jucham.repository.FlightAvailabilityRepository;
import com.jucham.repository.entity.FlightAvailabilityEntity;
import com.jucham.service.FlightAvailabilityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightAvailabilityServiceImpl implements FlightAvailabilityService {

    private final FlightAvailabilityRepository flightAvailabilityRepository;

    public FlightAvailabilityServiceImpl(FlightAvailabilityRepository flightAvailabilityRepository) {
        this.flightAvailabilityRepository = flightAvailabilityRepository;
    }

    @Override
    public List<FlightAvailability> getFlightAvailabilities(FlightAvailabilityRequest far) {

        List<FlightAvailabilityEntity> flightEntities = flightAvailabilityRepository.findByOriginAirportCodeIgnoreCaseAndDestinationAirportCodeIgnoreCase(
                far.getOriginAirportCode(),
                far.getDestinationAirportCode());

        return flightEntities.stream().map(fe -> {
            FlightAvailability fa = new FlightAvailability();
            fa.setFlightNumber(fe.getFlightNumber());
            fa.setOriginAirportCode(fe.getOriginAirportCode());
            fa.setDestinationAirportCode(fe.getDestinationAirportCode());
            int hour = Integer.parseInt(fe.getStartTime().substring(0, 2));
            int minutes = Integer.parseInt(fe.getStartTime().substring(3));
            fa.setStartDate(far.getStartDate().atTime(hour, minutes));
            fa.setDuration(fe.getDuration());
            fa.setAircraftType(fe.getAircraftType());
            fa.setPricePerPassenger(fe.getPricePerPassenger());
            fa.setRemainingSeats(fe.getRemainingSeats());
            return fa;
        }).collect(Collectors.toList());
    }

}
