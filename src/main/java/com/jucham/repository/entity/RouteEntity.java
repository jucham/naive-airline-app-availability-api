package com.jucham.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "route")
public class RouteEntity {

    @Id
    @Column(name = "id")
    Long id;

    @Column(name = "airline_id")
    int airlineId;

    @Column(name = "airline")
    String airline;

    @Column(name = "iata_src")
    String iataSrc;

    @Column(name = "id_src")
    int idSrc;

    @Column(name = "iata_dst")
    String iataDst;

    @Column(name = "id_dst")
    int idDst;

    @Column(name = "codeshare")
    String codeshare;

    @Column(name = "stops")
    int stops;

    @Column(name = "equipment")
    String equipment;

}
