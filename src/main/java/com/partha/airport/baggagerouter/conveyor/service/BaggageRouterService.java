package com.partha.airport.baggagerouter.conveyor.service;

import com.partha.airport.baggagerouter.conveyor.dto.NodeDTO;

import java.util.List;

/**
 * Created by psarkar on 4/3/2015.
 */
public interface BaggageRouterService
{
   List<NodeDTO> findShortestPath(String source, String target);
}
