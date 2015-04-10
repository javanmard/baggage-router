package com.partha.airport.baggagerouter.conveyor.exception;

import com.partha.airport.baggagerouter.conveyor.gate.Departure;

/**
 * Created by psarkar on 4/9/2015.
 */
public class DepartureNotFoundException extends RuntimeException
{
   public DepartureNotFoundException(String message)
   {
      super(message);
   }
}
