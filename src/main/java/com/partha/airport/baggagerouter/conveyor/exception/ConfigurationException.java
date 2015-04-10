package com.partha.airport.baggagerouter.conveyor.exception;

/**
 * Created by psarkar on 4/5/2015.
 */
public class ConfigurationException extends RuntimeException
{
   public ConfigurationException(String message, Throwable t)
   {
      super(message, t);
   }

   public ConfigurationException(String message)
   {
      super(message);
   }
}
