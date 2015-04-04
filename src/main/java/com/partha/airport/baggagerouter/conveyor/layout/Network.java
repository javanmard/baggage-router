package com.partha.airport.baggagerouter.conveyor.layout;

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
      LOG.info("Initializing......");
      Files.lines(Paths.get(this.getClass().getClassLoader().getResource(CONVEYOR_SYSTEM_LIST_FILE).toURI()))
           .filter(line -> !line.startsWith("#"))
           .map(line -> line.split(" "))
           .forEach(tokens -> addConveyorSegment(tokens[0], tokens[1], tokens[2]));
      LOG.info("..... Done");
   }

   /**
    * Adds a conveyor segment. This is meant to bea used only during initializatio
    * @param node1Name
    * @param node2Name
    * @param travelTimeStr
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

//   public static void main(String[] args)
//   {
//      Node v1 = NodeFactory.getFlightGate("A1");
//      Node v2 = NodeFactory.getFlightGate("A2");
//      Node v3 = NodeFactory.getFlightGate("A3");
//      Node v4 = NodeFactory.getFlightGate("A4");
//      Node v5 = NodeFactory.getFlightGate("A5");
//      Node v6 = NodeFactory.getFlightGate("A6");
//      Node v7 = NodeFactory.getFlightGate("A7");
//      Node v8 = NodeFactory.getFlightGate("A8");
//      Node v9 = NodeFactory.getFlightGate("A9");
//      Node v10 = NodeFactory.getFlightGate("A10");
//      Node vTicketing = NodeFactory.getFlightGate("Ticketing");
////      Node vBaggageClaim = NodeFactory.getFlightGate("BaggageClaim");
//
//      DepartureList departureList = new DepartureList();
//      departureList.addDeparture("UA10", v1, "MIA", new GregorianCalendar(2015, 2, 23, 8, 0).getTime());
//      departureList.addDeparture("UA11", v1, "LAX", new GregorianCalendar(2015, 2, 23, 9, 0).getTime());
//      departureList.addDeparture("UA12", v1, "JFK", new GregorianCalendar(2015, 2, 23, 9, 45).getTime());
//      departureList.addDeparture("UA13", v2, "JFK", new GregorianCalendar(2015, 2, 23, 8, 30).getTime());
//      departureList.addDeparture("UA14", v2, "JFK", new GregorianCalendar(2015, 2, 23, 9, 45).getTime());
//      departureList.addDeparture("UA15", v2, "JFK", new GregorianCalendar(2015, 2, 23, 10, 0).getTime());
//      departureList.addDeparture("UA16", v3, "JFK", new GregorianCalendar(2015, 2, 23, 9, 0).getTime());
//      departureList.addDeparture("UA17", v4, "MHT", new GregorianCalendar(2015, 2, 23, 9, 15).getTime());
//      departureList.addDeparture("UA18", v5, "LAX", new GregorianCalendar(2015, 2, 23, 10, 15).getTime());
//
//      Network network = new Network();
//      network.addConveyorSegment(vTicketing, v5, 5);
//      network.addConveyorSegment(v5, departureList.findDepartureByFlightId(DepartureList.ARRIVAL)
//                                                  .getFlightGate(), 5);
//      network.addConveyorSegment(v5, v10, 4);
//      network.addConveyorSegment(v5, v1, 6);
//      network.addConveyorSegment(v1, v2, 1);
//      network.addConveyorSegment(v2, v3, 1);
//      network.addConveyorSegment(v3, v4, 1);
//      network.addConveyorSegment(v10, v9, 1);
//      network.addConveyorSegment(v9, v8, 1);
//      network.addConveyorSegment(v8, v7, 1);
//      network.addConveyorSegment(v7, v6, 1);
//
//      printShortestPath(network.computeShortestPath(vTicketing, departureList.findDepartureByFlightId("UA12")
//                                                                             .getFlightGate()));
//      printShortestPath(network.computeShortestPath(v5, departureList.findDepartureByFlightId("UA17")
//                                                                     .getFlightGate()));
//      printShortestPath(network.computeShortestPath(v2, departureList.findDepartureByFlightId("UA10")
//                                                                     .getFlightGate()));
//      printShortestPath(network.computeShortestPath(v8, departureList.findDepartureByFlightId("UA18")
//                                                                     .getFlightGate()));
//      printShortestPath(network.computeShortestPath(v7, departureList
//              .findDepartureByFlightId(DepartureList.ARRIVAL).getFlightGate()));
//   }
//
//   private static void printShortestPath(List<Node> shortestPath)
//   {
//      Node source = shortestPath.get(0);
//      Node target = shortestPath.get(shortestPath.size() - 1);
//
//      System.out.println("Distance from: " + source.getName() + " to " + target.getName() + ": " + target
//              .getMinTravelTime() + ", length: " + shortestPath.size());
//
//      for (Node interimNode : shortestPath)
//      {
//         System.out.print(interimNode.getName() + " -> ");
//      }
//      System.out.println();
//      System.out.println();
//   }
}
