package com.partha.airport.baggagerouter.conveyor.layout;

import com.partha.airport.baggagerouter.conveyor.exception.UnknownNodeException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

   /**
    * Ensures that there is only one node of a given name.
    * @param name get a node of the given name. Create if not existing in the repository of nodes, if instructed so
    *             by the argument "createIfNotFound".
    * @param createIfNotFound if required node is not found, then createOrUpdate it, if this argument is true, else throw an
    *                         exception
    * @return the node asked for, or an exception.
    */
   public static Node getNode(String name, boolean createIfNotFound)
   {
      Node node = null;
      if(!StringUtils.isEmpty(name))
      {
         node = NODE_REPO.get(name);
         if (node == null)
         {
            if (createIfNotFound)
            {
               node = new Node(name, name);
               NODE_REPO.put(name, node);
            }
         }
      }
      if(node == null)
      {
         throw new UnknownNodeException("Node named: " + name + ", not found");
      }
      return node;
   }
}
