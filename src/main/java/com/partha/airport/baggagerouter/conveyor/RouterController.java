package com.partha.airport.baggagerouter.conveyor;

import com.partha.airport.baggagerouter.conveyor.dto.NodeDTO;
import com.partha.airport.baggagerouter.conveyor.gate.DepartureList;
import com.partha.airport.baggagerouter.conveyor.layout.Node;
import com.partha.airport.baggagerouter.conveyor.service.BaggageRouterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by psarkar on 4/2/2015.
 */
@RestController
@RequestMapping("/conveyor/router")
public class RouterController
{
   private static final Logger LOG = LoggerFactory.getLogger(RouterController.class);

   private final BaggageRouterService service;

   @Autowired
   DepartureList departureList;

   @Autowired
   public RouterController(BaggageRouterService service)
   {
      System.out.println("@@@@@@@@@@@ Creating RouterController");
      this.service = service;
   }

   @RequestMapping(value = "{source}/{flightId}", method = RequestMethod.GET)
   List<NodeDTO> findRoute(@NotNull @PathVariable("source") String source,
                           @NotNull @PathVariable("flightId") String flightId)
   {
      LOG.info("Source: {}, FlightId: [{}]", source, flightId);
      List<NodeDTO> route = service.findShortestPath(source, flightId);

      LOG.info("Returning: {}", route);
      return route;
   }
}
