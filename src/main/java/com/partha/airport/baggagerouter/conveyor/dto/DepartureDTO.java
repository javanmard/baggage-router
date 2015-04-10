package com.partha.airport.baggagerouter.conveyor.dto;

import com.partha.airport.baggagerouter.conveyor.gate.Departure;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by psarkar on 4/9/2015.
 */
public class DepartureDTO
{
   private String id;

   @NotEmpty
   @Size(max = 10)
   private String flightGate;

   @NotEmpty
   @Size(max = 3)
   private String destination;

   private Date departureTime;

   public DepartureDTO()
   {}

   public DepartureDTO(String id, String flightGate, String destination, Date departureTime)
   {
      this.id = id;
      this.flightGate = flightGate;
      this.destination = destination;
      this.departureTime = departureTime;
   }

   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public String getFlightGate()
   {
      return flightGate;
   }

   public void setFlightGate(String flightGate)
   {
      this.flightGate = flightGate;
   }

   public String getDestination()
   {
      return destination;
   }

   public void setDestination(String destination)
   {
      this.destination = destination;
   }

   public Date getDepartureTime()
   {
      return departureTime;
   }

   public void setDepartureTime(Date departureTime)
   {
      this.departureTime = departureTime;
   }

   public static DepartureDTO convertToDTO(Departure departure)
   {
      DepartureDTO departureDTO = new DepartureDTO();
      if (null != departure)
      {
         departureDTO.setId(departure.getFlightId());
         if (departure.getFlightGate() != null)
         {
            departureDTO.setFlightGate(departure.getFlightGate().getName());
         }
         departureDTO.setDestination(departure.getDestination());
         departureDTO.setDepartureTime(departure.getTime());
      }
      return departureDTO;
   }
//
//   public static List<DepartureDTO> convertToDTOs(List<Departure> departures)
//   {
//      return departures.stream()
//                       .map(this::convertToDTO)
//                       .collect(toList());
//   }

   @Override
   public String toString()
   {
      return "DepartureDTO{" +
              "flightId='" + id + '\'' +
              ", flightGate='" + flightGate + '\'' +
              ", destination='" + destination + '\'' +
              ", departureTime=" + departureTime +
              '}';
   }
}
