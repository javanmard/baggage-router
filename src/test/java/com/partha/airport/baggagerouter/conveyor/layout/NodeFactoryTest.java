package com.partha.airport.baggagerouter.conveyor.layout;

import com.partha.airport.baggagerouter.conveyor.exception.ConveyorException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by psarkar on 4/3/2015.
 */
public class NodeFactoryTest
{

   @Test(expected = ConveyorException.class)
   public void testGetBadNode() throws Exception
   {
      NodeFactory nodeFactory = new NodeFactory();
      nodeFactory.getNode("AAAA", true);
   }

   @Test
   public void testGetGoodNode() throws Exception
   {
      String nodeName = "AAA";
      NodeFactory nodeFactory = new NodeFactory();
      nodeFactory.getNode(nodeName, true);//, "AAA");
      Node node = nodeFactory.getNode(nodeName, true);
      Assert.assertEquals(nodeName, node.getName());
   }

   @Test
   public void testCreateNode() throws Exception
   {
      String nodeName = "AAA";
      NodeFactory nodeFactory = new NodeFactory();
      nodeFactory.getNode(nodeName, true);//, "AAA");
      Node node = nodeFactory.getNode(nodeName, true);
      Assert.assertEquals(nodeName, node.getName());

      nodeName = "BBB";
      nodeFactory.getNode(nodeName, true);//, "BBB");
      node = nodeFactory.getNode(nodeName, true);
      Assert.assertEquals(nodeName, node.getName());
   }
}