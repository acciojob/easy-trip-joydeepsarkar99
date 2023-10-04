package com.driver.services;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repository.AirportRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AirportService {

    AirportRepository airportRepositoryObj = new AirportRepository();

    public void addAirport(Airport airport){
        airportRepositoryObj.addAirportToDB(airport);
    }

    public String getLargestAirportName(){
        HashMap<String,Airport> airportHashMap = airportRepositoryObj.airportDB();
        if(airportHashMap.size() == 0) return null;

        int maxNoOfTerminal = -1;
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
        HashMap<Integer,Flight> flightHashMap = airportRepositoryObj.flightDB();
        HashMap<String, Airport> airportHashMap = airportRepositoryObj.airportDB();
        HashMap<Integer,List<Integer>> passengerFlightHashMap = airportRepositoryObj.passengerFlightDB();

        int count = 0;
        for (Integer flightId : passengerFlightHashMap.keySet()) {
            if(flightHashMap.get(flightId).getFromCity().equals(airportHashMap.get(airportName).getCity()) ||
                    flightHashMap.get(flightId).getToCity().equals(airportHashMap.get(airportName).getCity()) &&
            !flightHashMap.get(flightId).getFlightDate().before(date) && !flightHashMap.get(flightId).getFlightDate().after(date)){
                count++;
            }
        }
        return count;
    }

    public int calculateFlightFare(int flightId){
        HashMap<Integer,List<Integer>> passengerFlightHashMap = airportRepositoryObj.passengerFlightDB();
        int fare = 3000;
        fare += passengerFlightHashMap.get(flightId).size() * 50;
        return fare;
    }

    public String bookATicket(Integer flightId, Integer passengerId){
        HashMap<Integer,List<Integer>> passengerFlightHashMap = airportRepositoryObj.passengerFlightDB();
        HashMap<Integer,Flight> flightHashMap = airportRepositoryObj.flightDB();
        HashMap<Integer,Passenger> passengerHashMap = airportRepositoryObj.passengerDB();

        if(!flightHashMap.containsKey(flightId) || !passengerHashMap.containsKey(passengerId)) return "FAILURE";

        for(List<Integer> passengerList : passengerFlightHashMap.values()){
            for(int i=0;i<passengerList.size();i++){
                if(passengerList.get(i) == passengerId) return "FAILURE";
            }
        }
        List<Integer> passengers = passengerFlightHashMap.getOrDefault(flightId,new ArrayList<>());
        if(flightHashMap.get(flightId).getMaxCapacity() <= passengers.size()) return "FAILURE";
        passengers.add(passengerId);
        passengerFlightHashMap.put(flightId,passengers);
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId){
        HashMap<Integer,List<Integer>> passengerFlightHashMap = airportRepositoryObj.passengerFlightDB();
        HashMap<Integer,Flight> flightHashMap = airportRepositoryObj.flightDB();

        if(flightHashMap.size() == 0 || !flightHashMap.containsKey(flightId)) return "FAILURE";
        else if(passengerFlightHashMap.size() == 0 || !passengerFlightHashMap.containsKey(flightId)) return "FAILURE";


        List<Integer> passengers = passengerFlightHashMap.get(flightId);
        if(!passengers.contains(passengerId)) return "FAILURE";

        int idx = passengers.indexOf(passengerId);
        passengers.remove(idx);
        passengerFlightHashMap.put(flightId,passengers);
        return "SUCCESS";

    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){
        HashMap<Integer,List<Integer>> passengerFlightHashMap = airportRepositoryObj.passengerFlightDB();
        if(passengerFlightHashMap.size() == 0) return 0;

        int count = 0;
        for(List<Integer> passengerList : passengerFlightHashMap.values()){
            for(int i=0;i<passengerList.size();i++){
                if(passengerList.get(i) == passengerId) count++;
            }
        }
        return count;
    }

    public String getAirportNameFromFlightId(Integer flightId){
        if(flightId == null) return null;

        HashMap<Integer,Flight> flightHashMap = airportRepositoryObj.flightDB();
        Flight obj = flightHashMap.get(flightId);
        if(obj == null) return null;

        City city = obj.getFromCity();
        HashMap<String, Airport> airportHashMap = airportRepositoryObj.airportDB();
        for(Airport airport : airportHashMap.values()){
            if(airport.getCity() == city) return airport.getAirportName();
        }
        return null;


    }

    public int calculateRevenueOfAFlight(Integer flightId){
        HashMap<Integer,List<Integer>> passengerFlightHashMap = airportRepositoryObj.passengerFlightDB();
        List<Integer> passengerList = passengerFlightHashMap.getOrDefault(flightId,new ArrayList<>());
        if(passengerList.size() == 0) return 0;

        int totalRevenue = 0;
        for(int i=0;i<passengerList.size();i++){
            totalRevenue += (3000 + i * 50);
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
