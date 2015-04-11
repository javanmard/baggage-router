package com.partha.airport.baggagerouter.conveyor.gate;

import com.partha.airport.baggagerouter.conveyor.dto.DepartureDTO;
import com.partha.airport.baggagerouter.conveyor.exception.ConfigurationException;
import com.partha.airport.baggagerouter.conveyor.exception.DepartureException;
import com.partha.airport.baggagerouter.conveyor.exception.UnknownFlightIdException;
import com.partha.airport.baggagerouter.conveyor.layout.Node;
import com.partha.airport.baggagerouter.conveyor.layout.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
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
   private static Pattern timeFormatPattern = Pattern.compile(TIME_FORMAT_24H);

   private static int FLIGHT_ID_INDEX = 0;
   private static int FLIGHT_GATE_INDEX = 1;
   private static int DESTINATION_INDEX = 2;
   private static int DEPARTURE_TIME_INDEX = 3;

   /**
    * This method is meant as an initializer in the context of the exercise.
    * In a production/real world scenario, we'd likely only have the REST API to add departures, which would be invoked
    * by another service/ui, whenever a new departure is scheduled at the airport.
    *
    * @throws IOException
    * @throws URISyntaxException
    */
   @PostConstruct
   public void init() throws IOException, URISyntaxException
   {
      try
      {
         LOG.info("Initializing......");
         Files.lines(Paths.get(this.getClass().getClassLoader().getResource(DEPARTURE_LIST_FILE).toURI()))
              .filter(line -> !line.startsWith("#")).map(line -> line.split(" "))
              .forEach(tokens -> addDeparture(tokens[FLIGHT_ID_INDEX], tokens[FLIGHT_GATE_INDEX],
                      tokens[DESTINATION_INDEX], tokens[DEPARTURE_TIME_INDEX]));
         LOG.info("..... Done");

         //         LOG.info("Loading from DB");
         //         List<DepartureDTO> allDeparturesInDb = departureRepoService.findAll();
         //         allDeparturesInDb.stream()
         //                 .forEach(departure -> addDeparture(new DepartureDTO(departure.getId(), departure
         // .getFlightGate()
         //                         , departure.getDestination(), departure.getDepartureTime())));
         //         LOG.info("..... Done");
      }
      catch (Exception e)
      {
         throw new ConfigurationException("Unable to find, read or parse file: " + DEPARTURE_LIST_FILE, e);
      }
   }

   /**
    * This is meant to be called only in the context of an initialization for the exercise. It shouldn't be needed in a
    * real world scenario.
    *
    * @param flightId         flight id
    * @param flightGateName   gate name
    * @param destination      destination airport
    * @param departureTimeStr time (only - hh24:mi) of the departure, on the current date
    *                         TBD: not dealing with next day flights here
    * @return true if departure added (return only needed to be added to satisfy the usage in the lambda in init)
    */
   public boolean addDeparture(String flightId, String flightGateName, String destination, String departureTimeStr)
   {
      LOG.info("Checking departure: {}, {}, {}, {}. Total departures: {}", flightId, flightGateName, destination,
              departureTimeStr, departures.size());
      checkFlightId(flightId);
      checkFlightGate(flightGateName);
      checkDestination(destination);
      checkDepartureTime(departureTimeStr);

      Node flightGate = NodeFactory.getNode(flightGateName, true);
      Date departureTime = constructFlightDateTime(departureTimeStr);
      Departure departure = new Departure(flightId, flightGate, destination, departureTime);
      departures.put(flightId, departure);
      //      Optional<DepartureDTO> departureDTO = departureRepoService.findByFlightId(flightId);
      //      departureDTO.orElse(departureRepoService.createOrUpdate(DepartureDTO.convertToDTO(departure)));
      LOG.info("Added departure: {}, {}, {}, {}. Total departures: {}", flightId, flightGateName, destination,
              departureTimeStr, departures
                      .size());
      return true;
   }

   /**
    * This is how I'd expect departures to be added in a real world scenario, called from the REST API
    *
    * @param departureDTO DTO containing the details of the departure to be added.
    * @return true if added.
    */
   public boolean addDeparture(DepartureDTO departureDTO)
   {
      LOG.info("Checking departure: {}. Total departures: {}", departureDTO, departures.size());
      boolean isAdded = false;
      if (null != departureDTO)
      {
         checkFlightId(departureDTO.getId());
         checkFlightGate(departureDTO.getFlightGate());
         checkDestination(departureDTO.getDestination());
         checkDepartureTime(departureDTO.getDepartureTime());

         Node flightGate = NodeFactory.getNode(departureDTO.getFlightGate(), true);
         Departure departure = new Departure(departureDTO.getId(), flightGate, departureDTO
                 .getDestination(), departureDTO.getDepartureTime());
         departures.put(departureDTO.getId(), departure);
         LOG.info("Added departure: {}. Total departures: {}", departureDTO, departures.size());
         isAdded = true;
      }
      LOG.info("Returning: {}", departureDTO);
      return isAdded;
   }

   public Map<String, Departure> getAllDepartures()
   {
      return departures;
   }

   /**
    * Finds a destination node given a flight id
    *
    * @param flightId input flight id
    * @return node where the given flight is docked
    */
   public Node findEndNodeByFlightId(String flightId)
   {
      Node endNode;
      checkFlightId(flightId);

      if (flightId.equals(ARRIVAL))
      {
         endNode = NodeFactory.getNode(BAGGAGE_CLAIM, false);
      }
      else
      {
         Departure departure = departures.get(flightId);
         if (departure != null)
         {
            endNode = departure.getFlightGate();
            // TBD - in a real world application, we'd also check the time
         }
         else
         {
            throw new UnknownFlightIdException("Flight Id named: " + flightId + ", not found");
         }
      }
      return endNode;
   }

   private Date constructFlightDateTime(String flightTime)
   {
      LOG.info("flightTime: {}", flightTime);
      Date now = new Date();
      String todayString = DATE_FORMAT.format(now);

      StringBuilder sb = new StringBuilder();
      sb.append(todayString).append(" ").append(flightTime);
      String flightDateTimeStr = sb.toString();

      Date flightDateTime;
      try
      {
         flightDateTime = DATE_TIME_FORMAT.parse(flightDateTimeStr);
         // when loading from a file, we only have the time, so it's possible the time specified has already passed
         // today, so it likely means departure is tomorrow.
         if (flightDateTime.before(new Date()))
         {
            GregorianCalendar gCal = new GregorianCalendar();
            gCal.setTime(flightDateTime);
            gCal.add(GregorianCalendar.DATE, 1);
            flightDateTime = gCal.getTime();
         }
      }
      catch (ParseException pe)
      {
         throw new DepartureException("Couldn't read flight time: " + flightDateTimeStr + ": " + pe.getMessage());
      }
      return flightDateTime;
   }

   /**
    * A real world application, we will have business rules for flight Id's (which are not provided in the exercise).
    * So, just a check for empty here
    *
    * @param flightId Flight Id
    */
   public static void checkFlightId(String flightId)
   {
      if (StringUtils.isEmpty(flightId))
      {
         throw new DepartureException("Invalid flight Id: [" + flightId + "]");
      }
   }

   /**
    * A real world application, we will have business rules for flight gates (which are not provided in the exercise)
    * So, just a check for empty here
    *
    * @param flightGateName Flight gate name
    */
   public static void checkFlightGate(String flightGateName)
   {
      if (StringUtils.isEmpty(flightGateName))
      {
         throw new DepartureException("Invalid flight gate: [" + flightGateName + "]");
      }
   }

   /**
    * A real world application, we will have business rules for destination (which are not provided in the exercise)
    * So, just a check for empty here
    *
    * @param destination Destination airport code
    */
   public static void checkDestination(String destination)
   {
      if (StringUtils.isEmpty(destination))
      {
         throw new DepartureException("Invalid destination: [" + destination + "]");
      }
   }

   /**
    * A real world application, we will have business rules for departure times(which are not provided in the exercise)
    * So, just a check for empty & and 24h time format here
    *
    * @param departureTimeStr Flight Departure Time
    */
   public static void checkDepartureTime(String departureTimeStr)
   {
      if (StringUtils.isEmpty(departureTimeStr))
      {
         throw new DepartureException("Empty departure time: [" + departureTimeStr + "]");
      }
      Matcher matcher = timeFormatPattern.matcher(departureTimeStr);
      if (!matcher.matches())
      {
         throw new DepartureException("Invalid departure time: [" + departureTimeStr + "]: ");
      }
   }

   /**
    * A real world application, we will have business rules for departure times(which are not provided in the exercise)
    * So, just a check for empty & and 24h time format here
    *
    * @param departureTime Flight Departure Time
    */
   public static void checkDepartureTime(Date departureTime)
   {
      if (null == departureTime)
      {
         throw new DepartureException("Empty departure time: null]");
      }
   }
}
