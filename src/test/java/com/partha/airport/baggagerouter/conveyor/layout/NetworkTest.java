package com.partha.airport.baggagerouter.conveyor.layout;

import com.partha.airport.baggagerouter.conveyor.exception.ConfigurationException;
import com.partha.airport.baggagerouter.conveyor.exception.UnknownNodeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

/**
 * Created by psarkar on 4/3/2015.
 */
public class NetworkTest
{
   @Test
   public void testAddConveyorSegment() throws Exception
   {
      Network network = new Network();
      network.getAllNodes().clear();

      String node1Name = "AAA";
      String node2Name = "BBB";
      network.addConveyorSegment(node1Name, node2Name, 15);
      Assert.assertEquals(node1Name, network.getNode(node1Name).getName());
      Assert.assertEquals(node2Name, network.getNode(node2Name).getName());
   }

   @Test(expected = ConfigurationException.class)
   public void testAddConveyorSegmentTravelTimeZero() throws Exception
   {
      Network network = new Network();
      network.getAllNodes().clear();

      String node1Name = "AAA";
      String node2Name = "BBB";
      network.addConveyorSegment(node1Name, node2Name, 0);
   }

   @Test(expected = ConfigurationException.class)
   public void testAddConveyorSegmentTravelTimeNegative() throws Exception
   {
      Network network = new Network();
      network.getAllNodes().clear();

      String node1Name = "AAA";
      String node2Name = "BBB";
      network.addConveyorSegment(node1Name, node2Name, -1);
   }

   @Test
   public void testInit() throws Exception
   {
      Network network = new Network();
      network.getAllNodes().clear();
      network.init();
      Collection<Node> allNodes = network.getAllNodes();
      Assert.assertEquals(12, allNodes.size());
   }

   @Test
   public void testReset() throws Exception
   {
      Network network = new Network();
      network.getAllNodes().clear();

      String node1Name = "AAA";
      String node2Name = "BBB";
      String node3Name = "CCC";
      String node4Name = "DDD";
      network.addConveyorSegment(node1Name, node2Name, 15);
      Collection<Node> allNodes = network.getAllNodes();
      Assert.assertEquals(2, allNodes.size());
      network.addConveyorSegment(node1Name, node3Name, 20);
      allNodes = network.getAllNodes();
      Assert.assertEquals(3, allNodes.size());
      network.addConveyorSegment(node2Name, node3Name, 30);
      allNodes = network.getAllNodes();
      Assert.assertEquals(3, allNodes.size());
      network.addConveyorSegment(node3Name, node4Name, 10);
      allNodes = network.getAllNodes();
      Assert.assertEquals(4, allNodes.size());
      network.addConveyorSegment(node4Name, node1Name, 3);
      allNodes = network.getAllNodes();
      Assert.assertEquals(4, allNodes.size());

      List<Node> shortestPath = network.computeShortestPath(NodeFactory.getNode(node1Name, false), NodeFactory.getNode(node4Name, false));
      for(Node node: allNodes)
      {
         Assert.assertTrue(node.getMinTravelTime() < Integer.MAX_VALUE);
      }
      for(Node node: shortestPath)
      {
         Assert.assertTrue(node.getMinTravelTime() < Integer.MAX_VALUE);
      }

      network.reset();
      Assert.assertEquals(4, allNodes.size());
      for(Node node: allNodes)
      {
         Assert.assertTrue(node.getMinTravelTime() == Integer.MAX_VALUE);
         Assert.assertNull(node.getPrevious());
      }
   }

   @Test(expected = UnknownNodeException.class)
   public void testComputeShortestPathNullSource()
   {
      Network network = new Network();
      network.getAllNodes().clear();

      String cat = "Concourse_A_Ticketing";
      String bc = "BaggageClaim";
      String a1 = "A1";
      String a2 = "A2";
      String a3 = "A3";
      String a4 = "A4";
      String a5 = "A5";
      String a6 = "A6";
      String a7 = "A7";
      String a8 = "A8";
      String a9 = "A9";
      String a10 = "A10";
      network.addConveyorSegment(cat, a5, 5);
      network.addConveyorSegment(a5, bc, 5);
      network.addConveyorSegment(a5, a10, 4);
      network.addConveyorSegment(a5, a1, 6);
      network.addConveyorSegment(a1, a2, 1);
      network.addConveyorSegment(a2, a3, 1);
      network.addConveyorSegment(a3, a4, 1);
      network.addConveyorSegment(a10, a9, 1);
      network.addConveyorSegment(a9, a8, 1);
      network.addConveyorSegment(a8, a7, 1);
      network.addConveyorSegment(a7, a6, 1);

      network.computeShortestPath(null, network.getNode(a1));
   }

   @Test(expected = UnknownNodeException.class)
   public void testComputeShortestPathNullTarget()
   {
      Network network = new Network();
      network.getAllNodes().clear();

      String cat = "Concourse_A_Ticketing";
      String bc = "BaggageClaim";
      String a1 = "A1";
      String a2 = "A2";
      String a3 = "A3";
      String a4 = "A4";
      String a5 = "A5";
      String a6 = "A6";
      String a7 = "A7";
      String a8 = "A8";
      String a9 = "A9";
      String a10 = "A10";
      network.addConveyorSegment(cat, a5, 5);
      network.addConveyorSegment(a5, bc, 5);
      network.addConveyorSegment(a5, a10, 4);
      network.addConveyorSegment(a5, a1, 6);
      network.addConveyorSegment(a1, a2, 1);
      network.addConveyorSegment(a2, a3, 1);
      network.addConveyorSegment(a3, a4, 1);
      network.addConveyorSegment(a10, a9, 1);
      network.addConveyorSegment(a9, a8, 1);
      network.addConveyorSegment(a8, a7, 1);
      network.addConveyorSegment(a7, a6, 1);

      network.computeShortestPath(network.getNode(a1), null);
   }

   /**
    * perform all the shortest path tests provided in the exercise
    * @throws Exception
    */
   @Test
   public void testComputeShortestPath() throws Exception
   {
      Network network = new Network();
      network.getAllNodes().clear();

      String cat = "Concourse_A_Ticketing";
      String bc = "BaggageClaim";
      String a1 = "A1";
      String a2 = "A2";
      String a3 = "A3";
      String a4 = "A4";
      String a5 = "A5";
      String a6 = "A6";
      String a7 = "A7";
      String a8 = "A8";
      String a9 = "A9";
      String a10 = "A10";
      network.addConveyorSegment(cat, a5, 5);
      network.addConveyorSegment(a5, bc, 5);
      network.addConveyorSegment(a5, a10, 4);
      network.addConveyorSegment(a5, a1, 6);
      network.addConveyorSegment(a1, a2, 1);
      network.addConveyorSegment(a2, a3, 1);
      network.addConveyorSegment(a3, a4, 1);
      network.addConveyorSegment(a10, a9, 1);
      network.addConveyorSegment(a9, a8, 1);
      network.addConveyorSegment(a8, a7, 1);
      network.addConveyorSegment(a7, a6, 1);

      Collection<Node> allNodes = network.getAllNodes();
      Assert.assertEquals(12, allNodes.size());

      List<Node> shortestPath = network.computeShortestPath(network.getNode(cat), network.getNode(a1));
      Assert.assertEquals(3, shortestPath.size());
      Assert.assertEquals(network.getNode(cat), shortestPath.get(0));
      Assert.assertEquals(network.getNode(a5), shortestPath.get(1));
      Assert.assertEquals(network.getNode(a1), shortestPath.get(2));

      shortestPath = network.computeShortestPath(network.getNode(a5), network.getNode(a4));
      Assert.assertEquals(5, shortestPath.size());
      Assert.assertEquals(network.getNode(a5), shortestPath.get(0));
      Assert.assertEquals(network.getNode(a1), shortestPath.get(1));
      Assert.assertEquals(network.getNode(a2), shortestPath.get(2));
      Assert.assertEquals(network.getNode(a3), shortestPath.get(3));
      Assert.assertEquals(network.getNode(a4), shortestPath.get(4));

      shortestPath = network.computeShortestPath(network.getNode(a2), network.getNode(a1));
      Assert.assertEquals(2, shortestPath.size());
      Assert.assertEquals(network.getNode(a2), shortestPath.get(0));
      Assert.assertEquals(network.getNode(a1), shortestPath.get(1));

      shortestPath = network.computeShortestPath(network.getNode(a8), network.getNode(a5));
      Assert.assertEquals(4, shortestPath.size());
      Assert.assertEquals(network.getNode(a8), shortestPath.get(0));
      Assert.assertEquals(network.getNode(a9), shortestPath.get(1));
      Assert.assertEquals(network.getNode(a10), shortestPath.get(2));
      Assert.assertEquals(network.getNode(a5), shortestPath.get(3));

      shortestPath = network.computeShortestPath(network.getNode(a7), network.getNode(bc));
      Assert.assertEquals(6, shortestPath.size());
      Assert.assertEquals(network.getNode(a7), shortestPath.get(0));
      Assert.assertEquals(network.getNode(a8), shortestPath.get(1));
      Assert.assertEquals(network.getNode(a9), shortestPath.get(2));
      Assert.assertEquals(network.getNode(a10), shortestPath.get(3));
      Assert.assertEquals(network.getNode(a5), shortestPath.get(4));
      Assert.assertEquals(network.getNode(bc), shortestPath.get(5));
   }
}