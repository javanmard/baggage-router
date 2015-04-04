package com.partha.airport.baggagerouter.conveyor.gate;

import com.partha.airport.baggagerouter.conveyor.layout.Node;

import java.util.Date;

/**
 * Created by psarkar on 4/2/2015.
 */
public class Departure
{
   private String flightId;

   private Node flightGate;

   private String destination;

   private Date time;

   public Departure(String flightId, Node flightGate, String destination, Date time)
   {
      this.flightId = flightId;
      this.flightGate = flightGate;
      this.destination = destination;
      this.time = time;
   }

   public String getFlightId()
   {
      return flightId;
   }

   public void setFlightId(String flightId)
   {
      this.flightId = flightId;
   }

   public Node getFlightGate()
   {
      return flightGate;
   }

   public void setFlightGate(Node flightGate)
   {
      this.flightGate = flightGate;
   }

   public String getDestination()
   {
      return destination;
   }

   public void setDestination(String destination)
   {
      this.destination = destination;
   }

   public Date getTime()
   {
      return time;
   }

   public void setTime(Date time)
   {
      this.time = time;
   }
}
