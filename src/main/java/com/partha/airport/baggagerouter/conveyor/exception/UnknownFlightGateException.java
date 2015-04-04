package com.partha.airport.baggagerouter.conveyor.exception;

/**
 * Created by psarkar on 4/4/2015.
 */
public class UnknownFlightGateException extends RuntimeException
{
   public UnknownFlightGateException(String message)
   {
      super(message);
   }
}
