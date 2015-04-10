package com.partha.airport.baggagerouter.conveyor;

import com.partha.airport.baggagerouter.conveyor.dto.NodeDTO;
import com.partha.airport.baggagerouter.conveyor.service.BaggageRouterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

   @Autowired
   public RouterController(BaggageRouterService baggageRouterService)
   {
      this.baggageRouterService = baggageRouterService;
   }

   @RequestMapping(value = "{source}/{flightId}", method = RequestMethod.GET)
   List<NodeDTO> findRoute(@PathVariable("source") String source, @PathVariable("flightId") String flightId)
   {
      LOG.info("Source: [{}], FlightId: [{}]", source, flightId);
      List<NodeDTO> route = baggageRouterService.findShortestPath(source, flightId);
      LOG.info("Returning: {}", route);
      return route;
   }
}
