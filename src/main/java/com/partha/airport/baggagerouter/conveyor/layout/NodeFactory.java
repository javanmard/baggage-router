package com.partha.airport.baggagerouter.conveyor.layout;

import com.partha.airport.baggagerouter.conveyor.exception.ConveyorException;
import com.partha.airport.baggagerouter.conveyor.exception.UnknownNodeException;
import com.partha.airport.baggagerouter.conveyor.layout.Node;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by psarkar on 4/2/2015.
 */
@Component
public class NodeFactory
{
   private static final Map<String, Node> nodeRepo = new HashMap<>();

   public static Node getNode(String name, boolean createIfNotFound)
   {
      Node node = nodeRepo.get(name);
      if(node == null)
      {
         if(createIfNotFound)
         {
            node = new Node(name, name);
            nodeRepo.put(name, node);
         }
         else
         {
            throw new UnknownNodeException("Node named: " + name + ", not found");
         }
      }

      return node;
   }
}
