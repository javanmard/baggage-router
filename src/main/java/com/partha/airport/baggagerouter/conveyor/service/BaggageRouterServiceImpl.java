package com.partha.airport.baggagerouter.conveyor.service;

import com.partha.airport.baggagerouter.conveyor.dto.NodeDTO;
import com.partha.airport.baggagerouter.conveyor.gate.DepartureList;
import com.partha.airport.baggagerouter.conveyor.layout.Network;
import com.partha.airport.baggagerouter.conveyor.layout.Node;
import com.partha.airport.baggagerouter.conveyor.layout.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by psarkar on 4/3/2015.
 */
@Service
public class BaggageRouterServiceImpl implements BaggageRouterService
{
   private static final Logger LOG = LoggerFactory.getLogger(BaggageRouterServiceImpl.class);

   @Autowired
   NodeFactory nodeFactory;
   @Autowired
   DepartureList departureList;
   @Autowired
   Network network;

   /**
    * Shortest path from source to flight id (flight id is located at some target node).
    * @param sourceName
    * @param flightId
    * @return
    */
   @Override
   public List<NodeDTO> findShortestPath(String sourceName, String flightId)
   {
      List<NodeDTO> shortestPathDTO = new ArrayList<>();
      Node source = nodeFactory.getNode(sourceName, false);
      Node target = departureList.findEndNodeByFlightId(flightId);
      if (source != null && target != null)
      {
         List<Node> shortestPath = new ArrayList<>();
         if (target != null)
         {
            shortestPath = network.computeShortestPath(source, target);
         }
         shortestPathDTO = convert(shortestPath);
      }
      LOG.info("ShortestPath: {}", shortestPathDTO);
      return shortestPathDTO;
   }

   private List<NodeDTO> convert(List<Node> nodes)
   {
      return nodes.stream().map(this::convert).collect(toList());
   }

   private NodeDTO convert(Node node)
   {
      NodeDTO nodeDTO = new NodeDTO();
      nodeDTO.setName(node.getName());

      return nodeDTO;
   }
}
