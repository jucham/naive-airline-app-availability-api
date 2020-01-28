package com.jucham.repository.entity;

import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "flight")
@ToString
public class FlightAvailabilityEntity {

    @Column(name = "flight_number")
    @Id
    private int flightNumber;
    private String startTime;
    private String aircraftType;
    private BigDecimal pricePerPassenger;
    private int remainingSeats;
    private String originAirportCode;
    private String destinationAirportCode;
    private int duration;

    @Id
    @Column(name = "flight_number")
    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Basic
    @Column(name = "start_time")
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    @Basic
    @Column(name = "aircraft_type")
    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    @Basic
    @Column(name = "price_per_passenger")
    public BigDecimal getPricePerPassenger() {
        return pricePerPassenger;
    }

    public void setPricePerPassenger(BigDecimal pricePerPassenger) {
        this.pricePerPassenger = pricePerPassenger;
    }

    @Basic
    @Column(name = "remaining_seats")
    public int getRemainingSeats() {
        return remainingSeats;
    }

    public void setRemainingSeats(int remainingSeats) {
        this.remainingSeats = remainingSeats;
    }

    @Basic
    @Column(name = "origin_airport_code")
    public String getOriginAirportCode() {
        return originAirportCode;
    }

    public void setOriginAirportCode(String originAirportCode) {
        this.originAirportCode = originAirportCode;
    }

    @Basic
    @Column(name = "destination_airport_code")
    public String getDestinationAirportCode() {
        return destinationAirportCode;
    }

    public void setDestinationAirportCode(String destinationAirportCode) {
        this.destinationAirportCode = destinationAirportCode;
    }

    @Basic
    @Column(name = "duration")
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


}
