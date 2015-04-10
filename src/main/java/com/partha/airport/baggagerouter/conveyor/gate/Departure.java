package com.partha.airport.baggagerouter.conveyor.gate;

import com.partha.airport.baggagerouter.conveyor.layout.Node;

import java.util.Date;

/**
 * A pojo that represents a departing flight.
 */
public class Departure
{
   private String flightId;

   private Node flightGate;

   private String destination;

   private Date time;

   public Departure()
   {}

   public Departure(String flightId, Node flightGate, String destination, Date tTime)
   {
      this.flightId = flightId;
      this.flightGate = flightGate;
      this.destination = destination;
      this.time = (tTime != null) ? new Date(tTime.getTime()) : null;
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
      return new Date(time.getTime());
   }

   public void setTime(Date tTime)
   {
      this.time = (tTime != null) ? new Date(tTime.getTime()) : null;
   }
}
