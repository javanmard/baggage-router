package com.partha.airport.baggagerouter.conveyor.layout;

import com.partha.airport.baggagerouter.conveyor.exception.ConfigurationException;
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

   @PostConstruct
   public void init() throws IOException, URISyntaxException
   {
      LOG.info("Initializing.......");
      try
      {
         Files.lines(Paths.get(this.getClass().getClassLoader().getResource(CONVEYOR_SYSTEM_LIST_FILE).toURI()))
              .filter(line -> !line.startsWith("#")).map(line -> line.split(" "))
              .forEach(tokens -> addConveyorSegment(tokens[0], tokens[1], tokens[2]));
      }
      catch (Exception e)
      {
         throw new ConfigurationException("Unable to find or parse file: " + CONVEYOR_SYSTEM_LIST_FILE, e);
      }
         LOG.info("..... Done");
   }

   /**
    * Adds a conveyor segment. This is meant to bea used only during initializatio
    * @param node1Name node name
    * @param node2Name node name
    * @param travelTimeStr travel time between node1 & node2
    */
   public void addConveyorSegment(String node1Name, String node2Name, String travelTimeStr)
   {
      LOG.info("Adding conveyor segment between {}, and {}", node1Name, node2Name);
      Node node1 = NodeFactory.getNode(node1Name, true);
      Node node2 = NodeFactory.getNode(node2Name, true);
      Integer travelTime = Integer.parseInt(travelTimeStr);
      node1.addSegment(new Segment(node2, travelTime));
      node2.addSegment(new Segment(node1, travelTime));

      allNodes.put(node1.getName(), node1);
      allNodes.put(node2.getName(), node2);
   }

   Node getNode(String name)
   {
      return allNodes.get(name);
   }

   Collection<Node> getAllNodes()
   {
      return allNodes.values();
   }

   void reset()
   {
      allNodes.values().forEach(node -> node.reset());
   }

   public List<Node> computeShortestPath(Node source, Node target)
   {
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

      List<Node> shortestPath = new ArrayList<>();

      for (Node Node = target; Node != null; Node = Node.getPrevious())
      {
         shortestPath.add(Node);
      }
      Collections.reverse(shortestPath);
      return shortestPath;
   }
}
