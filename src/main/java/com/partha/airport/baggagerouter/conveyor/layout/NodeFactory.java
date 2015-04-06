package com.partha.airport.baggagerouter.conveyor.layout;

import com.partha.airport.baggagerouter.conveyor.exception.UnknownNodeException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by psarkar on 4/2/2015.
 */
@Component
public class NodeFactory
{
   private static final Map<String, Node> NODE_REPO = new HashMap<>();

   private NodeFactory()
   {
   }

   public static Node getNode(String name, boolean createIfNotFound)
   {
      Node node = NODE_REPO.get(name);
      if (node == null)
      {
         if (createIfNotFound)
         {
            node = new Node(name, name);
            NODE_REPO.put(name, node);
         }
         else
         {
            throw new UnknownNodeException("Node named: " + name + ", not found");
         }
      }

      return node;
   }
}
