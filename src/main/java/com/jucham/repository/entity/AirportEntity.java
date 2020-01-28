package com.jucham.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "airport")
public class AirportEntity {

    @Id
    @Column(name = "airport_id")
    Long airportId;

    @Column(name = "airport_name")
    String name;

    @Column(name = "city")
    String city;

    @Column(name = "country")
    String country;

    @Column(name = "iata")
    String iata;

    @Column(name = "icao")
    String icao;

    @Column(name = "latitude")
    Double latitude;

    @Column(name = "longitude")
    Double longitude;

    @Column(name = "altitude")
    Integer altitude;

    @Column(name = "airport_timezone")
    Integer timezone;

    @Column(name = "dst")
    String dst;

    @Column(name = "tz_database_time_zone")
    String tzDatabaseTimeZone;

    @Column(name = "airport_type")
    String type;

    @Column(name = "airport_source")
    String source;
}
