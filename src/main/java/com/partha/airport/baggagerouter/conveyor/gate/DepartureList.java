package com.partha.airport.baggagerouter.conveyor.gate;

import com.partha.airport.baggagerouter.conveyor.exception.DepartureException;
import com.partha.airport.baggagerouter.conveyor.exception.UnknownFlightGateException;
import com.partha.airport.baggagerouter.conveyor.layout.Node;
import com.partha.airport.baggagerouter.conveyor.layout.NodeFactory;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
   private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
   private static final String TIME_FORMAT_24H = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
   private Pattern timeFormatPattern = Pattern.compile(TIME_FORMAT_24H);
   private Matcher matcher;

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

   /**
    *
    * @param flightId flight id
    * @param flightGateName gate name
    * @param destination destination airport
    * @param departureTimeStr time (only - hh24:mi) of the departure, on the current date
    *                         TBD: not dealing with next day flights here
    * @return
    */
   public boolean addDeparture(String flightId, String flightGateName, String destination,
                               String departureTimeStr)
   {
      LOG.info("Checking departure: {}, {}, {}, {}. Total departures: {}", flightId, flightGateName, destination, departureTimeStr);
      checkFlightId(flightId);
      checkFlightGate(flightGateName);
      checkDestination(destination);
      checkDepartureTime(departureTimeStr);

      Node flightGate = NodeFactory.getNode(flightGateName, true);
      Date departureTime = constructFlightDateTime(departureTimeStr);
      Departure departure = new Departure(flightId, flightGate, destination, departureTime);
      departures.put(flightId, departure);
      LOG.info("Added departure: {}, {}, {}, {}. Total departures: {}", flightId, flightGateName, destination, departureTimeStr, departures.size());
      return true;
   }

   /**
    * A real world application, we will have business rules for flight Id's (which are not provided in the exercise).
    * So, just a check for empty here
    * @param flightId Flight Id
    */
   private void checkFlightId(String flightId)
   {
      if(StringUtils.isEmpty(flightId))
      {
         throw new DepartureException("Invalid flight Id: [" + flightId + "]");
      }
   }

   /**
    * A real world application, we will have business rules for flight gates (which are not provided in the exercise)
    * So, just a check for empty here
    * @param flightGateName Flight gate name
    */
   private void checkFlightGate(String flightGateName)
   {
      if(StringUtils.isEmpty(flightGateName))
      {
         throw new DepartureException("Invalid flight gate: [" + flightGateName + "]");
      }
   }

   /**
    * A real world application, we will have business rules for destination (which are not provided in the exercise)
    * So, just a check for empty here
    * @param destination Destination airport code
    */
   private void checkDestination(String destination)
   {
      if(StringUtils.isEmpty(destination))
      {
         throw new DepartureException("Invalid destination: [" + destination + "]");
      }
   }

   /**
    * A real world application, we will have business rules for departure times(which are not provided in the exercise)
    * So, just a check for empty & and 24h time format here
    * @param departureTimeStr Flight gate name
    */
   private void checkDepartureTime(String departureTimeStr)
   {
      if(StringUtils.isEmpty(departureTimeStr))
      {
         throw new DepartureException("Empty departure time: [" + departureTimeStr + "]");
      }
      matcher = timeFormatPattern.matcher(departureTimeStr);
      if(!matcher.matches())
      {
         throw new DepartureException("Invalid departure time: [" + departureTimeStr + "]: ");
      }
   }

   public Map<String, Departure> getAllDepartures()
   {
      return departures;
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
         throw new DepartureException("Couldn't read flight time: " + flightDateTimeStr + ": " + pe.getMessage());
      }
      return flightDateTime;
   }
}
