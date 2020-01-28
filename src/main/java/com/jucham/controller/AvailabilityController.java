package com.jucham.controller;

import com.jucham.dto.FlightAvailability;
import com.jucham.dto.FlightAvailabilityRequest;
import com.jucham.service.FlightAvailabilityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

import static com.jucham.dto.FlightAvailability.AIRPORT_CODE_REGEX;

@CrossOrigin
@RestController
@Validated
public class AvailabilityController {

    private final FlightAvailabilityService flightAvailabilityService;

    public AvailabilityController(FlightAvailabilityService flightAvailabilityService) {
        this.flightAvailabilityService = flightAvailabilityService;
    }

    /**
     * Get the flight availability
     *
     * @param originAirportCode      the airport code of origin
     * @param destinationAirportCode the airport code of destination
     * @param startDate              the date of departure
     * @param numberOfPassengers     the number of passengers
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("flights/{originAirportCode}/{destinationAirportCode}/{startDate}/{numberOfPassengers}")
    public List<FlightAvailability> getAvailableFlights(
            @PathVariable @NotNull @Pattern(regexp = AIRPORT_CODE_REGEX) String originAirportCode,
            @PathVariable @NotNull @Pattern(regexp = AIRPORT_CODE_REGEX) String destinationAirportCode,
            @PathVariable @NotNull @FutureOrPresent @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable @NotNull @Min(1) @Max(10) Integer numberOfPassengers) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return flightAvailabilityService.getFlightAvailabilities(
                new FlightAvailabilityRequest(originAirportCode, destinationAirportCode, startDate, numberOfPassengers)
        );
    }
}
