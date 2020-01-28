package com.jucham.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FlightAvailability {

    public static final String AIRPORT_CODE_REGEX = "[A-Za-z]{3}";

    /**
     * the number of flight
     */
    @NotNull
    @NotBlank
    @Min(1)
    @Max(9999)
    private Integer flightNumber;

    /**
     * the airport code of origin
     */
    @NotNull
    @NotBlank
    @Pattern(regexp = AIRPORT_CODE_REGEX)
    private String originAirportCode;

    /**
     * the airport code of destination
     */
    @NotNull
    @NotBlank
    @Pattern(regexp = AIRPORT_CODE_REGEX)
    private String destinationAirportCode;

    /**
     * the date of departure
     */
    @NotNull
    @FutureOrPresent
    private LocalDateTime startDate;

    /**
     * duration of the flight
     */
    @NotNull
    private Integer duration;

    /**
     * The type of aircraft
     */
    private String aircraftType;

    /**
     * The price per passenger
     */
    @NotNull
    @DecimalMin("0")
    private BigDecimal pricePerPassenger;

    /**
     * The number of remaining seats
     */
    @NotNull
    private Integer remainingSeats;

}
