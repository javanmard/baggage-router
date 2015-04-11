package com.partha.airport.baggagerouter.conveyor.layout;

import com.partha.airport.baggagerouter.conveyor.exception.ConfigurationException;
import com.partha.airport.baggagerouter.conveyor.exception.UnknownNodeException;
import com.partha.airport.baggagerouter.conveyor.gate.DepartureList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by psarkar on 4/2/2015.
 */
@Component
public class Network
{
   private static final Logger LOG = LoggerFactory.getLogger(Network.class);

   private static final Map<String, Node> allNodes = new HashMap<>();

   private static final String CONVEYOR_SYSTEM_LIST_FILE = "conveyor.system.list";

   private static final int NODE_1_INDEX = 0;
   private static final int NODE_2_INDEX = 1;
   private static final int TRAVEL_TIME_INDEX = 2;

   /**
    * Read file with listing of the conveyor system. The format of the file is that each line is in the following format:
    * <node1> <node2> <flighttime>
    *
    * @throws IOException
    * @throws URISyntaxException
    */
   @PostConstruct
   public void init() throws IOException, URISyntaxException
   {
      LOG.info("Initializing.......");
      try
      {
         Files.lines(Paths.get(this.getClass().getClassLoader().getResource(CONVEYOR_SYSTEM_LIST_FILE).toURI()))
              .filter(line -> !line.startsWith("#")).map(line -> line.split(" "))
              .forEach(tokens -> addConveyorSegment(tokens[NODE_1_INDEX], tokens[NODE_2_INDEX],
                      Integer.parseInt(tokens[TRAVEL_TIME_INDEX])));
      }
      catch (Exception e)
      {
         throw new ConfigurationException("Unable to find, read or parse file: " + CONVEYOR_SYSTEM_LIST_FILE, e);
      }
      LOG.info("..... Done");
   }

   /**
    * Adds a conveyor segment. This is meant to bea used only during initialization
    * @param node1Name node name
    * @param node2Name node name
    * @param travelTime travel time between node1 & node2
    */
   public void addConveyorSegment(String node1Name, String node2Name, int travelTime)
   {
      LOG.info("Adding conveyor segment between {}, and {}", node1Name, node2Name);
      DepartureList.checkFlightGate(node1Name);
      DepartureList.checkFlightGate(node2Name);
      checkTravelTime(travelTime);

      Node node1 = NodeFactory.getNode(node1Name, true);
      Node node2 = NodeFactory.getNode(node2Name, true);
      node1.addSegment(new Segment(node2, travelTime));
      node2.addSegment(new Segment(node1, travelTime));

      allNodes.put(node1.getName(), node1);
      allNodes.put(node2.getName(), node2);
   }

   /**
    * In a real world application, maybe we'd have better validations requirements. Here, just a check for 0 or less.
    * @param travelTime travel time to validate
    */
   private static void checkTravelTime(int travelTime)
   {
      if(travelTime <= 0)
      {
         throw new ConfigurationException("Invalid travel time specified: " + travelTime);
      }
   }

   Node getNode(String name)
   {
      return allNodes.get(name);
   }

   Collection<Node> getAllNodes()
   {
      return allNodes.values();
   }

   /**
    * reset the traversal status of the network, so that it can be used to find shortest path between another pair of
    * nodes. It resets the min distance from source to all nodes, as well as the previous node in the shortest path from
    * source to target, in the previous computation
    */
   void reset()
   {
      allNodes.values().forEach(node -> node.reset());
   }

   private void checkNode(Node node) throws UnknownNodeException
   {
      if(node == null)
      {
         throw new UnknownNodeException("Node is null");
      }
      NodeFactory.getNode(node.getName(), false);
   }

   /**
    * Find shortest path between source and targetUses Djikstra's algorithm.
    * @param source source node
    * @param target target node
    * @return list of nodes between the source and target
    */
   public List<Node> computeShortestPath(Node source, Node target)
   {
      checkNode(source);
      checkNode(target);
      LOG.info("Computing shortest path from: {}, target: {}", source.getName(), target.getName());
      reset();
      source.setMinTravelTime(0);
      PriorityQueue<Node> queue = new PriorityQueue<>();

      queue.add(source);

      while (!queue.isEmpty())
      {
         Node currentNode = queue.poll();

         for (Segment currentSegment : currentNode.getSegments())
         {

            Node currentSegmentTarget = currentSegment.getTarget();
            int currentSegmentTravelTime = currentSegment.getTravelTime();

            int travelTimeSourceToCurrentTarget = currentNode.getMinTravelTime() + currentSegmentTravelTime;

            if (travelTimeSourceToCurrentTarget < currentSegmentTarget.getMinTravelTime())
            {

               queue.remove(currentSegmentTarget);
               currentSegmentTarget.setMinTravelTime(travelTimeSourceToCurrentTarget);
               currentSegmentTarget.setPrevious(currentNode);

               // short circuiting the Djitstra algorithm - no need to explore from the target, if we are at the target
               if (!currentSegmentTarget.equals(target))
               {
                  queue.add(currentSegmentTarget);
               }
            }
         }
      }
      return extractShortestPath(source, target);
   }

   private List<Node> extractShortestPath(Node source, Node target)
   {
      List<Node> shortestPath = new ArrayList<>();

      // when no path is found, the target will be the same as the source, so suppress it.
      if(null != source && null != target && !target.equals(source))
      {
         for (Node node = target; null != node; node = node.getPrevious())
         {
            shortestPath.add(node);
         }
      }
      Collections.reverse(shortestPath);
      return shortestPath;
   }
}
