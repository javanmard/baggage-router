package com.partha.airport.baggagerouter.conveyor.service;

import com.partha.airport.baggagerouter.conveyor.dto.NodeDTO;
import com.partha.airport.baggagerouter.conveyor.exception.UnknownFlightIdException;
import com.partha.airport.baggagerouter.conveyor.exception.UnknownNodeException;
import com.partha.airport.baggagerouter.conveyor.gate.DepartureList;
import com.partha.airport.baggagerouter.conveyor.layout.Network;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;

/**
 * Created by psarkar on 4/10/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class BaggageServiceImplTest
{
   @Configuration
   static class ContextConfiguration
   {

      // this bean will be injected into the OrderServiceTest class
      @Bean
      public BaggageRouterService baggageRouterService()
      {
         BaggageRouterService baggageRouterService = new BaggageRouterServiceImpl();
         // set properties, etc.
         return baggageRouterService;
      }

      @Bean
      public DepartureList departureList()
      {
         DepartureList departureList = new DepartureList();
         // set properties, etc.
         return departureList;
      }

      @Bean
      public Network network()
      {
         Network network = new Network();
         // set properties, etc.
         return network;
      }
   }

   @Autowired
   private BaggageRouterService baggageRouterService;

   @Autowired
   private DepartureList departureList;

   @Autowired
   private Network network;

   @Before
   public void setup()
   {
      MockitoAnnotations.initMocks(this);
   }

   @Test
   public void testFindShortestPath()
   {
      String sourceName = "A10";
      String flightId = "UA10";
      List<NodeDTO> shortestPathDTO = baggageRouterService.findShortestPath(sourceName, flightId);
      Assert.assertEquals(3, shortestPathDTO.size());
      Assert.assertEquals("A10", shortestPathDTO.get(0).getName());
      Assert.assertEquals("A5", shortestPathDTO.get(1).getName());
      Assert.assertEquals("A1", shortestPathDTO.get(2).getName());
   }

   @Test
   public void testFindShortestPathNullSource()
   {
      String sourceName = null;
      String flightId = "UA10";
      List<NodeDTO> shortestPathDTO = baggageRouterService.findShortestPath(sourceName, flightId);
      Assert.assertEquals(0, shortestPathDTO.size());
   }

   @Test(expected = UnknownNodeException.class)
   public void testFindShortestPathInvalidSource()
   {
      String sourceName = "A111";
      String flightId = "UA10";
      List<NodeDTO> shortestPathDTO = baggageRouterService.findShortestPath(sourceName, flightId);
   }

   @Test
   public void testFindShortestPathNullFlightId()
   {
      String sourceName = "A10";
      String flightId = null;
      List<NodeDTO> shortestPathDTO = baggageRouterService.findShortestPath(sourceName, flightId);
      Assert.assertEquals(0, shortestPathDTO.size());
   }

   @Test(expected = UnknownFlightIdException.class)
   public void testFindShortestPathInvalidFlightId()
   {
      String sourceName = "A10";
      String flightId = "UA111";
      List<NodeDTO> shortestPathDTO = baggageRouterService.findShortestPath(sourceName, flightId);
   }
}
