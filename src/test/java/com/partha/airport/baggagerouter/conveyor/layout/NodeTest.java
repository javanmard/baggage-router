package com.partha.airport.baggagerouter.conveyor.layout;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Created by psarkar on 4/3/2015.
 */
public class NodeTest
{

   @Test
   public void testCompareToLess() throws Exception
   {
      Node node1 = new Node("A", "A");
      node1.setMinTravelTime(10);
      Node node2 = new Node("B", "B");
      node2.setMinTravelTime(11);
      Assert.assertTrue(node1.compareTo(node2) < 0);
   }

   @Test
   public void testCompareToEqual() throws Exception
   {
      Node node1 = new Node("A", "A");
      node1.setMinTravelTime(10);
      Node node2 = new Node("B", "B");
      node2.setMinTravelTime(10);
      Assert.assertEquals(0, node1.compareTo(node2));
   }

   @Test
   public void testCompareToMore() throws Exception
   {
      Node node1 = new Node("A", "A");
      node1.setMinTravelTime(11);
      Node node2 = new Node("B", "B");
      node2.setMinTravelTime(10);
      Assert.assertTrue(node1.compareTo(node2) > 0);
   }

   @Test
   public void testReset() throws Exception
   {
      Node node = new Node("A", "A");
      Assert.assertTrue(node.getMinTravelTime() == Integer.MAX_VALUE);
      node.setMinTravelTime(123);
      Assert.assertTrue(node.getMinTravelTime() < Integer.MAX_VALUE);
      node.setPrevious(new Node("B", "B"));
      Assert.assertNotNull(node.getPrevious());
      node.reset();
      Assert.assertTrue(node.getMinTravelTime() == Integer.MAX_VALUE);
      Assert.assertNull(node.getPrevious());
   }

   @Test
   public void testAddSegment() throws Exception
   {
      Node node = new Node("A", "A");
      Assert.assertEquals(0, node.getSegments().size());
      Node target = new Node("T1", "T1");
      Segment segment = new Segment(target, 10);
      node.addSegment(segment);
      Assert.assertEquals(1, node.getSegments().size());
      Node target2 = new Node("T2", "T2");
      Segment segment2 = new Segment(target2, 20);
      node.addSegment(segment2);
      Assert.assertEquals(2, node.getSegments().size());
      Assert.assertEquals("T1", node.getSegments().get(0).getTarget().getName());
      Assert.assertEquals("T2", node.getSegments().get(1).getTarget().getName());
      Assert.assertEquals(10, node.getSegments().get(0).getTravelTime());
      Assert.assertEquals(20, node.getSegments().get(1).getTravelTime());
   }

   @Test
   public void testGetDescription() throws Exception
   {
      String description = "A Description";
      Node node = new Node("A", description);
      Assert.assertEquals(description, node.getDescription());
   }

   @Test
   public void testEquals()
   {
      Node node1 = new Node("A", "Node A Description");
      Node node2 = new Node("B", "Node B Description");
      Node node3 = new Node("A", "Node C Description");
      Assert.assertNotEquals(node1, node2);
      Assert.assertEquals(node1, node3);
      Assert.assertNotEquals(node1, "A");

   }
}