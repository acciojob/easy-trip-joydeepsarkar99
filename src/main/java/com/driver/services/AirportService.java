package com.driver.services;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AirportService {

    AirportRepository airportRepositoryObj;

    public void addAirport(Airport airport){
        airportRepositoryObj.addAirportToDB(airport);
    }

    public String getLargestAirportName(){
        int maxNoOfTerminal = Integer.MIN_VALUE;
        HashMap<String,Airport> airportHashMap = airportRepositoryObj.airportDB();
        for(Airport airport : airportHashMap.values()){
            maxNoOfTerminal = Math.max(maxNoOfTerminal,airport.getNoOfTerminals());
        }
        List<String> airtportNamesList = new ArrayList<>();
        for(String airtportName : airportHashMap.keySet()){
            Airport obj = airportHashMap.get(airtportName);
            if(obj.getNoOfTerminals() == maxNoOfTerminal){
                airtportNamesList.add(airtportName);
            }
        }
        if(airtportNamesList.size() == 1){
            return airtportNamesList.get(0);
        }
        Collections.sort(airtportNamesList);
        return airtportNamesList.get(0);
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity){
        HashMap<Integer,Flight> flightHashMap = airportRepositoryObj.flightDB();
        double minDuration = Integer.MAX_VALUE;
        for(Flight obj : flightHashMap.values()){
            if(obj.getFromCity().equals(fromCity) && obj.getToCity().equals(toCity)){
                minDuration = Math.min(minDuration,obj.getDuration());
            }
        }
        return minDuration == Integer.MAX_VALUE ? -1 : minDuration;
    }

    public int getNumberOfPeopleOn(Date date, String airportName){
        Integer count = null;
        HashMap<Integer,Flight> flightHashMap = airportRepositoryObj.flightDB();
        for(Flight obj : flightHashMap.values()){
            //converting enum to string and then comparing
            if((obj.getToCity().toString().equals(airportName) || obj.getFromCity().toString().equals(airportName)) && obj.getFlightDate().equals(date)){
                count++;
            }
        }
        return count;
    }

    public int calculateFlightFare(int flightId){
        HashMap<Integer,List<Integer>> passengerFlightHashMap = airportRepositoryObj.passengerFlightDB();
        List<Integer> passengers = passengerFlightHashMap.get(flightId);
        return 3000 + (passengers.size() * 50);
    }

    public String bookATicket(Integer flightId, Integer passengerId){
        HashMap<Integer,List<Integer>> passengerFlightHashMap = airportRepositoryObj.passengerFlightDB();
        HashMap<Integer,Flight> flightHashMap = airportRepositoryObj.flightDB();

        List<Integer> passengers = passengerFlightHashMap.get(flightId);
        Flight obj = flightHashMap.get(flightId);
        if(obj.getMaxCapacity() >= passengers.size()){
            return "FAILURE";
        }
        else if(passengers.contains(passengerId)){
            return "FAILURE";
        }
        passengers.add(passengerId);
        passengerFlightHashMap.put(flightId,passengers);
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId){
        HashMap<Integer,List<Integer>> passengerFlightHashMap = airportRepositoryObj.passengerFlightDB();
        HashMap<Integer,Flight> flightHashMap = airportRepositoryObj.flightDB();

        if(!flightHashMap.containsKey(flightId)) return "FAILURE";
        else if(!passengerFlightHashMap.containsKey(flightId)) return "FAILURE";

        List<Integer> passengers = passengerFlightHashMap.get(flightId);
        if(!passengers.contains(passengerId)) return "FAILURE";

        int idx = passengers.indexOf(passengerId);
        passengers.remove(idx);
        passengerFlightHashMap.put(flightId,passengers);
        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){
        Integer count = null;
        HashMap<Integer,List<Integer>> passengerFlightHashMap = airportRepositoryObj.passengerFlightDB();
        for(List<Integer> passengerList : passengerFlightHashMap.values()){
            if(passengerList.contains(passengerId)) count++;
        }
        return count;
    }

    public String getAirportNameFromFlightId(Integer flightId){
        HashMap<Integer,Flight> flightHashMap = airportRepositoryObj.flightDB();
        if(!flightHashMap.containsKey(flightId)) return null;
        Flight obj = flightHashMap.get(flightId);

        for(City city : City.values()){
            if(city == obj.getFromCity()) return obj.getFromCity().toString();
        }
        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId){
        Integer totalRevenue = null;
        HashMap<Integer,List<Integer>> passengerFlightHashMap = airportRepositoryObj.passengerFlightDB();
        if(passengerFlightHashMap.containsKey(flightId)){
            List<Integer> passengerList = passengerFlightHashMap.get(flightId);
            int idx = 0;
            for(int passenger : passengerList){
                totalRevenue += 3000 + (idx * 50);
                idx++;
            }
        }
        return totalRevenue;
    }


    public void addFlight(Flight flight){
        airportRepositoryObj.addFlightToDB(flight);
    }

    public void addPassenger(Passenger passenger){
        airportRepositoryObj.addPassengerToDB(passenger);
    }
}
