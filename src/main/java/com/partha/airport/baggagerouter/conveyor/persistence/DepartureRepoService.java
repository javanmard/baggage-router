package com.partha.airport.baggagerouter.conveyor.persistence;

import com.partha.airport.baggagerouter.conveyor.dto.DepartureDTO;
import com.partha.airport.baggagerouter.conveyor.gate.Departure;

import java.util.List;
import java.util.Optional;

/**
 * Created by psarkar on 4/9/2015.
 */
public interface DepartureRepoService
{
   List<DepartureDTO> findAll();

   Optional<DepartureDTO> findByFlightId(String id);

   DepartureDTO createOrUpdate(DepartureDTO departure);
}
