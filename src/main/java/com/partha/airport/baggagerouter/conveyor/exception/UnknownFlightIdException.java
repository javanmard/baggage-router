package com.partha.airport.baggagerouter.conveyor.exception;

/**
 * Created by psarkar on 4/4/2015.
 */
public class UnknownFlightIdException extends RuntimeException
{
   public UnknownFlightIdException(String message)
   {
      super(message);
   }
}
