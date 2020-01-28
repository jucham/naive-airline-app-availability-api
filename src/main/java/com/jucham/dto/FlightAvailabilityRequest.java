package com.jucham.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FlightAvailabilityRequest {

    private String originAirportCode;
    private String destinationAirportCode;
    private LocalDate startDate;
    private int numberOfPassengers;

    public FlightAvailabilityRequest() {

    }
}
