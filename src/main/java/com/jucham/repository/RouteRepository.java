package com.jucham.repository;

import com.jucham.repository.entity.RouteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "route", path = "route")
public interface RouteRepository extends CrudRepository<RouteEntity, Long> {
}
