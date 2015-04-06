package com.partha.airport.baggagerouter.conveyor.dto;

/**
 * Created by psarkar on 4/3/2015.
 */
public class NodeDTO
{
   private String name;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }


   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (o == null || getClass() != o.getClass())
      {
         return false;
      }

      NodeDTO nodeDTO = (NodeDTO) o;

      return name.equals(nodeDTO.name);
   }

   @Override
   public int hashCode()
   {
      return name.hashCode();
   }

   @Override
   public String toString()
   {
      return "NodeDTO{" +
              "name='" + name + '\'' +
              '}';
   }
}
