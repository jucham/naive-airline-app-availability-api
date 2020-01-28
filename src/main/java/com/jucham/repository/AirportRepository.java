package com.jucham.repository;

import com.jucham.repository.entity.AirportEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "airport", path = "airport")
public interface AirportRepository extends CrudRepository<AirportEntity, Long> {

}
