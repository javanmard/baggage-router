package com.partha.airport.baggagerouter.conveyor.layout;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node>
{

   private final String name;
   private final String description;

   private List<Segment> segments = new ArrayList<>();

   private int minTravelTime = Integer.MAX_VALUE;

   private Node previous;

   Node(String name, String description)
   {
      this.name = name;
      this.description = description;
   }

   public String getDescription()
   {
      return description;
   }

   /**
    * Comparison between two nodes involves the travel time between them, from the source.
    * @param otherNode the node to compare the travel time to.
    * @return whether this node is shorter than the other one, when travelling from the source node.
    */
   @Override
   public int compareTo(@NotNull Node otherNode)
   {
      return Integer.compare(minTravelTime, otherNode.minTravelTime);
   }

   /**
    * reset this node to its state before it was used for any shortest path computation.
    */
   public void reset()
   {
      minTravelTime = Integer.MAX_VALUE;
      previous = null;
   }

   public Node getPrevious()
   {
      return previous;
   }

   public void setPrevious(Node previous)
   {
      this.previous = previous;
   }

   public List<Segment> getSegments()
   {
      return segments;
   }

   public void addSegment(Segment segment)
   {
      this.segments.add(segment);
   }

   public int getMinTravelTime()
   {
      return minTravelTime;
   }

   public void setMinTravelTime(int minTravelTime)
   {
      this.minTravelTime = minTravelTime;
   }

   public String getName()
   {
      return name;
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (o == null || getClass() != o.getClass())
      {
         return false;
      }

      Node node = (Node) o;

      return name.equals(node.name);

   }

   @Override
   public int hashCode()
   {
      return name.hashCode();
   }

   @Override
   public String toString()
   {
      return "Node{" +
              "name='" + name + '\'' +
              ", segments=[" + getAdjacentSegmentTargets() + "]" +
              ", minTravelTime=" + minTravelTime +
              ", previous=" + previous +
              '}';
   }

   private String getAdjacentSegmentTargets()
   {
      StringBuilder sb = new StringBuilder();
      for(Segment segment: segments)
      {
         sb.append(segment.getTarget().getName()).append(", ");
      }
      return sb.toString();
   }
}

