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
import org.springframework.util.StringUtils;

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
   DepartureList departureList;
   @Autowired
   Network network;

   /**
    * Shortest path from source node to flight id (whichever node/gate it is located at).
    * @param sourceName
    * @param flightId
    * @return shortest path through the conveyor system
    */
   @Override
   public List<NodeDTO> findShortestPath(String sourceName, String flightId)
   {
      List<NodeDTO> shortestPathDTOs = new ArrayList<>();
      if(!StringUtils.isEmpty(sourceName) && !StringUtils.isEmpty(flightId))
      {
         Node source = NodeFactory.getNode(sourceName, false);
         Node target = departureList.findEndNodeByFlightId(flightId);
         if (source != null && target != null)
         {
            List<Node> shortestPath = new ArrayList<>();
            if (target != null)
            {
               shortestPath = network.computeShortestPath(source, target);
            }
            shortestPathDTOs = convert(shortestPath);
         }
      }
      LOG.info("ShortestPath: {}", shortestPathDTOs);
      return shortestPathDTOs;
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
