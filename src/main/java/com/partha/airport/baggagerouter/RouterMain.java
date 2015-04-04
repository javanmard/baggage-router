package com.partha.airport.baggagerouter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by psarkar on 4/2/2015.
 */
@SpringBootApplication
public class RouterMain
{
   private static final Logger LOG = LoggerFactory.getLogger(RouterMain.class);

   public static void main(String[] args)
   {
      LOG.info("Baggage Router Starting up.....");
      SpringApplication.run(RouterMain.class, args);
      LOG.info("... Initialization Complete. Ready to start computing...");
   }
}