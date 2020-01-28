package com.jucham.service;

import com.jucham.dto.FlightAvailability;
import com.jucham.dto.FlightAvailabilityRequest;

import java.util.List;

public interface FlightAvailabilityService {

    List<FlightAvailability> getFlightAvailabilities(FlightAvailabilityRequest flightAvailabilityRequest);


}
