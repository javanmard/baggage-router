package com.partha.airport.baggagerouter.conveyor.layout;

import com.partha.airport.baggagerouter.conveyor.exception.UnknownNodeException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by psarkar on 4/3/2015.
 */
public class NodeFactoryTest
{

   @Test(expected = UnknownNodeException.class)
   public void testGetBadNode() throws Exception
   {
      NodeFactory.getNode("AAAA", false);
   }

   @Test
   public void testGetGoodNode() throws Exception
   {
      String nodeName = "AAA";
      Node node = NodeFactory.getNode(nodeName, true);
      Assert.assertEquals(nodeName, node.getName());

      nodeName = "BBB";
      node = NodeFactory.getNode(nodeName, true);
      Assert.assertEquals(nodeName, node.getName());
   }
}