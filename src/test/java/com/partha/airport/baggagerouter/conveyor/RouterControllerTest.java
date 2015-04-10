package com.partha.airport.baggagerouter.conveyor;

import com.partha.airport.baggagerouter.conveyor.service.BaggageRouterService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by psarkar on 4/10/2015.
 */
@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
public class RouterControllerTest
{
   private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
           MediaType.APPLICATION_JSON.getSubtype(),
           Charset.forName("utf8")
   );

   @Autowired
   private BaggageRouterService service;

   private MockMvc mockMvc;

   @Before
   public void setUp() {
      MockitoAnnotations.initMocks(this);
      final ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();

      //here we need to setup a dummy application context that only registers the GlobalControllerExceptionHandler
      final StaticApplicationContext applicationContext = new StaticApplicationContext();
      applicationContext.registerBeanDefinition("advice", new RootBeanDefinition(RouterControllerTest.class, null, null));

      //set the application context of the resolver to the dummy application context we just created
      exceptionHandlerExceptionResolver.setApplicationContext(applicationContext);

      //needed in order to force the exception resolver to update it's internal caches
      exceptionHandlerExceptionResolver.afterPropertiesSet();

      mockMvc = MockMvcBuilders.standaloneSetup(new RouterController(service)).setHandlerExceptionResolvers(exceptionHandlerExceptionResolver).build();
   }

   @Test
   public void testFindRoute() throws Exception
   {
      String source = "A10";
      String flightId = "UA123";
      mockMvc.perform(get("/conveyor/router/{source}/{flightId}", source, flightId))
             .andExpect(status().isOk())
             .andExpect(content().contentType(APPLICATION_JSON_UTF8))
             .andExpect(jsonPath("$", hasSize(3)))
             .andExpect(jsonPath("$[0].name", is("A10")))
             .andExpect(jsonPath("$[1].name", is("A5")))
             .andExpect(jsonPath("$[2].name", is("A1")));

   }
}
