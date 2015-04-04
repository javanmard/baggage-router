package com.partha.airport.baggagerouter.conveyor.layout;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by psarkar on 4/3/2015.
 */
public class NetworkTest
{
   @Test
   public void testAddConveyorSegment() throws Exception
   {
      Network network = new Network();
      String node1Name = "AAA";
      String node2Name = "BBB";
      network.addConveyorSegment(node1Name, node2Name, "15");
      Assert.assertEquals(node1Name, network.getNode(node1Name));
      Assert.assertEquals(node2Name, network.getNode(node2Name));
   }

   @Test
   public void testReset() throws Exception
   {
      Network network = new Network();
      String node1Name = "AAA";
      String node2Name = "BBB";
      String node3Name = "CCC";
      String node4Name = "DDD";
      network.addConveyorSegment(node1Name, node2Name, "15");
      Collection<Node> allNodes = network.getAllNodes();
      Assert.assertEquals(2, allNodes.size());
      network.addConveyorSegment(node1Name, node3Name, "20");
      allNodes = network.getAllNodes();
      Assert.assertEquals(3, allNodes.size());
      network.addConveyorSegment(node2Name, node3Name, "30");
      allNodes = network.getAllNodes();
      Assert.assertEquals(3, allNodes.size());
      network.addConveyorSegment(node3Name, node4Name, "10");
      allNodes = network.getAllNodes();
      Assert.assertEquals(4, allNodes.size());
      network.addConveyorSegment(node4Name, node1Name, "3");
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

   @Test
   public void testComputeShortestPath() throws Exception
   {

   }
}