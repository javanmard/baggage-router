package com.partha.airport.baggagerouter.conveyor;

import com.partha.airport.baggagerouter.conveyor.dto.DepartureDTO;
import com.partha.airport.baggagerouter.conveyor.dto.NodeDTO;
import com.partha.airport.baggagerouter.conveyor.gate.DepartureList;
import com.partha.airport.baggagerouter.conveyor.persistence.DepartureRepoService;
import com.partha.airport.baggagerouter.conveyor.service.BaggageRouterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by psarkar on 4/2/2015.
 */
@RestController
@RequestMapping("/conveyor/router")
public class RouterController
{
   private static final Logger LOG = LoggerFactory.getLogger(RouterController.class);

   private final BaggageRouterService baggageRouterService;

   private final DepartureRepoService departureRepoService;

   private final DepartureList departureList;

   @Autowired
   public RouterController(BaggageRouterService baggageRouterService, DepartureRepoService departureRepoService,
                           DepartureList departureList)
   {
      this.baggageRouterService = baggageRouterService;
      this.departureRepoService = departureRepoService;
      this.departureList = departureList;
   }

   @RequestMapping(value = "{source}/{flightId}", method = RequestMethod.GET)
   List<NodeDTO> findRoute(@PathVariable("source") String source, @PathVariable("flightId") String flightId)
   {
      LOG.info("Source: [{}], FlightId: [{}]", source, flightId);
      List<NodeDTO> route = baggageRouterService.findShortestPath(source, flightId);
      LOG.info("Returning: {}", route);
      return route;
   }

   @RequestMapping(method = RequestMethod.PUT)
   DepartureDTO createOrUpdate(@RequestBody @Valid DepartureDTO departure)
   {
      LOG.info("Updating departure: {}", departure);

      DepartureDTO updated = departureRepoService.createOrUpdate(departure);
      departureList.addDeparture(departure);
      LOG.info("Updated departure: {}", updated);

      return updated;
   }
}
