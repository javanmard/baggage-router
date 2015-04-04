package com.partha.airport.baggagerouter.conveyor.layout;

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

   @Override
   public int compareTo(Node otherNode)
   {
      return Integer.compare(getMinTravelTime(), otherNode.getMinTravelTime());
   }

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
      StringBuffer sb = new StringBuffer();
      for(Segment segment: segments)
      {
         sb.append(segment.getTarget().getName()).append(", ");
      }
      return sb.toString();
   }
}
