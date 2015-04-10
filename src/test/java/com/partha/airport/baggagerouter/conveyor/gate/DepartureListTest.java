package com.partha.airport.baggagerouter.conveyor.gate;

import com.partha.airport.baggagerouter.conveyor.exception.DepartureException;
import com.partha.airport.baggagerouter.conveyor.exception.UnknownNodeException;
import com.partha.airport.baggagerouter.conveyor.layout.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by psarkar on 4/5/2015.
 */
public class DepartureListTest
{

   @Test
   public void testAddDeparture() throws Exception
   {
      String flightId = "UA12";
      String flightGateName = "A4";
      String destination = "LAX";
      String departureTineStr = "09:00";

      DepartureList departureList = new DepartureList();
      departureList.addDepartureFromFile(flightId, flightGateName, destination, departureTineStr);
      Map<String, Departure> allDepartures = departureList.getAllDepartures();
      Assert.assertEquals(1, allDepartures.size());
   }

   @Test(expected = DepartureException.class)
   public void testAddDepartureNullFlightId() throws Exception
   {
      String flightId = null;
      String flightGateName = "A4";
      String destination = "LAX";
      String departureTineStr = "09:00";
      DepartureList departureList = new DepartureList();
      departureList.addDepartureFromFile(flightId, flightGateName, destination, departureTineStr);
   }

   @Test(expected = DepartureException.class)
   public void testAddDepartureNullGate() throws Exception
   {
      String flightId = "UA12";
      String flightGateSName = null;
      String destination = "LAX";
      String departureTineStr = "09:00";

      DepartureList departureList = new DepartureList();
      departureList.addDepartureFromFile(flightId, flightGateSName, destination, departureTineStr);
   }

   @Test(expected = DepartureException.class)
   public void testAddDepartureNullDestination() throws Exception
   {
      String flightId = "UA12";
      String flightGateSName = "A4";
      String destination = null;
      String departureTineStr = "09:00";

      DepartureList departureList = new DepartureList();
      departureList.addDepartureFromFile(flightId, flightGateSName, destination, departureTineStr);
   }

   @Test(expected = DepartureException.class)
   public void testAddDepartureNullDepartureTime() throws Exception
   {
      String flightId = "UA12";
      String flightGateSName = "A4";
      String destination = "LAX";
      String departureTineStr = null;

      DepartureList departureList = new DepartureList();
      departureList.addDepartureFromFile(flightId, flightGateSName, destination, departureTineStr);
   }

   @Test(expected = DepartureException.class)
   public void testAddDepartureBadDepartureTime() throws Exception
   {
      String flightId = "UA12";
      String flightGateSName = "A4";
      String destination = "LAX";
      String departureTineStr = "24:00";

      DepartureList departureList = new DepartureList();
      System.out.println("Checking bad time format: " + departureTineStr);
      departureList.addDepartureFromFile(flightId, flightGateSName, destination, departureTineStr);
   }

   @Test
   public void testFindEndNodeByFlightId() throws Exception
   {
      String flightId = "UA12";
      String flightGateName = "A4";
      String destination = "LAX";
      String departureTineStr = "09:00";

      DepartureList departureList = new DepartureList();
      departureList.addDepartureFromFile(flightId, flightGateName, destination, departureTineStr);

      Node departure = departureList.findEndNodeByFlightId(flightId);
      Assert.assertEquals(flightGateName, departure.getName());
   }

   @Test(expected = UnknownNodeException.class)
   public void testFindArrivalByFlightId() throws Exception
   {
      String flightId = "ARRIVAL";
      String flightGateName = "A4";
      String destination = "LAX";
      String departureTineStr = "09:00";

      DepartureList departureList = new DepartureList();
      departureList.addDepartureFromFile(flightId, flightGateName, destination, departureTineStr);

      Node departure = departureList.findEndNodeByFlightId(flightId);
      Assert.assertEquals(flightGateName, departure.getName());
   }

   @Test
   public void testFindBaggageClaimByFlightId() throws Exception
   {
      String flightId = "ARRIVAL";
      String baggageClaim = "BaggageClaim";
      String flightGateName = "A4";
      String destination = "LAX";
      String departureTineStr = "09:00";

      DepartureList departureList = new DepartureList();
      departureList.addDepartureFromFile(flightId, flightGateName, destination, departureTineStr);
      departureList.addDepartureFromFile(baggageClaim, "BaggageClaim", destination, departureTineStr);

      Node departure = departureList.findEndNodeByFlightId(flightId);
      Assert.assertEquals(baggageClaim, departure.getName());
   }
};