package com.partha.airport.baggagerouter.conveyor.gate;

import com.partha.airport.baggagerouter.conveyor.exception.DepartureException;
import com.partha.airport.baggagerouter.conveyor.exception.UnknownFlightGateException;
import com.partha.airport.baggagerouter.conveyor.layout.Node;
import com.partha.airport.baggagerouter.conveyor.layout.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by psarkar on 4/2/2015.
 */
@Component
public class DepartureList
{
   private static final Logger LOG = LoggerFactory.getLogger(DepartureList.class);

   private final Map<String, Departure> departures = new HashMap<>();
   public static final String ARRIVAL = "ARRIVAL";
   public static final String BAGGAGE_CLAIM = "BaggageClaim";

   private static final String DEPARTURE_LIST_FILE = "departure.list";

   private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
   private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm");

   @PostConstruct
   public void init() throws IOException, URISyntaxException
   {
      LOG.info("Initializing......");
      Files.lines(Paths.get(this.getClass().getClassLoader().getResource(DEPARTURE_LIST_FILE).toURI()))
           .filter(line -> !line.startsWith("#"))
           .map(line -> line.split(" "))
           .forEach(tokens -> addDeparture(tokens[0], tokens[1], tokens[2], tokens[3]));
      LOG.info("..... Done");
   }

   public boolean addDeparture(@NotNull String flightId, @NotNull String flightGateStr, @NotNull String destination,
                               @NotNull String departureTimeStr)
   {
      LOG.info("flightId: {}", flightId);
      Node flightGate = NodeFactory.getNode(flightGateStr, true);
      Date departureTime = constructFlightDateTime(departureTimeStr);
      Departure departure = new Departure(flightId, flightGate, destination, departureTime);
      departures.put(flightId, departure);
      LOG.info("Added flight: {}. Total departures: {}", flightId, departures.size());
      return true;
   }

   public @NotNull Node findEndNodeByFlightId(@NotNull String flightId)
   {
      Node endNode = null;
      if(flightId.equals(ARRIVAL))
      {
         endNode = NodeFactory.getNode(BAGGAGE_CLAIM, false);
      }
      else
      {
         Departure departure = departures.get(flightId);
         if (departure != null)
         {
            endNode = departure.getFlightGate();
         }
         else
         {
            throw new UnknownFlightGateException("Flight Id named: " + flightId + ", not found");
         }
      }
      return endNode;
   }

   private Date constructFlightDateTime(String flightTime)
   {
      LOG.info("flightTime: {}", flightTime);
      Date now = new Date();
      String todayString = DATE_FORMAT.format(now);

      StringBuffer sb = new StringBuffer();
      sb.append(todayString).append(" ").append(flightTime);
      String flightDateTimeStr = sb.toString();

      Date flightDateTime;
      try
      {
         flightDateTime = DATE_TIME_FORMAT.parse(flightDateTimeStr);
      }
      catch (ParseException pe)
      {
         throw new DepartureException("Couldn't read flight time");
      }
      return flightDateTime;
   }
}
