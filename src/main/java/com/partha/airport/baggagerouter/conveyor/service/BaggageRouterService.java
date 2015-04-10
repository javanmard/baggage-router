package com.partha.airport.baggagerouter.conveyor.service;

import com.partha.airport.baggagerouter.conveyor.dto.NodeDTO;

import java.util.List;

/**
 * Created by psarkar on 4/3/2015.
 */
public interface BaggageRouterService
{
   /**
    * Find shortest path through the conveyor system from the source node to the target node.
    * @param source source node
    * @param target target node
    * @return list of nodes between the source and target traversing the shortest path between them.
    */
   List<NodeDTO> findShortestPath(String source, String target);
}
