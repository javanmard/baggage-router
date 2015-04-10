package com.partha.airport.baggagerouter.conveyor.persistence;

import com.partha.airport.baggagerouter.conveyor.dto.DepartureDTO;
import com.partha.airport.baggagerouter.conveyor.exception.DepartureNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by psarkar on 4/9/2015.
 */
@Service
public class DepartureRepoServiceImpl implements DepartureRepoService
{
   private static final Logger LOG = LoggerFactory.getLogger(DepartureRepoServiceImpl.class);

   private final DepartureListRepository repository;

   @Autowired
   DepartureRepoServiceImpl(DepartureListRepository repository)
   {
      this.repository = repository;
   }

   @Override
   public List<DepartureDTO> findAll()
   {
      LOG.info("Finding all Departures");
      List<DepartureDTO> allDepartures = repository.findAll();
      LOG.info("Number of departures in list: {}", allDepartures.size());
      return allDepartures;
   }

   @Override
   public Optional<DepartureDTO> findByFlightId(final String flightId)
   {
      LOG.info("Trying to find Departure by id: {}", flightId);
      Optional<DepartureDTO> departure = repository.findOne(flightId);
      LOG.info("Responding with flight : {}", departure);
      return departure;
   }

   @Override
   public DepartureDTO createOrUpdate(final DepartureDTO departure)
   {
      LOG.info("Creating new departure: {}", departure);
      DepartureDTO savedDeparture = null;
      if (departure != null)
      {
         savedDeparture = repository.save(departure);
         LOG.info("Created new departure", departure.getId());
      }
      else
      {
         new DepartureNotFoundException("Couldn't find departure with flight id: null");
      }
      return savedDeparture;
   }
}
