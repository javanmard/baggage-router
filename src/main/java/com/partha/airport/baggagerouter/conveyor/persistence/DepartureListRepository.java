package com.partha.airport.baggagerouter.conveyor.persistence;

import com.partha.airport.baggagerouter.conveyor.dto.DepartureDTO;
import com.partha.airport.baggagerouter.conveyor.gate.Departure;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by psarkar on 4/9/2015.
 */
interface DepartureListRepository extends Repository<DepartureDTO, String>
{
   List<DepartureDTO> findAll();

   Optional<DepartureDTO> findOne(String flightId);

   DepartureDTO save(DepartureDTO departure);
}
