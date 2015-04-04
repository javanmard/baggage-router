package com.partha.airport.baggagerouter.conveyor.layout;

/**
 * Created by psarkar on 4/2/2015.
 */
class Segment
{

   private final Node target;
   private final int travelTime;

   public Segment(Node target, int travelTime)
   {
      this.target = target;
      this.travelTime = travelTime;
   }

   public Node getTarget()
   {
      return target;
   }

   public int getTravelTime()
   {
      return travelTime;
   }

   @Override
   public String toString()
   {
      return "ConveyorSegment{target=" + target +
              ", travelTime=" + travelTime + '}';
   }
}

