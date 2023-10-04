package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class AirportRepository {

    HashMap<String, Airport> airportHashMap = new HashMap<>();
    HashMap<Integer,Flight> flightHashMap = new HashMap<>();
    HashMap<Integer, Passenger> passengerHashMap = new HashMap<>();
    HashMap<Integer, List<Integer>> passengerFlightHashMap = new HashMap<>(); //flightId,List<Passenger>
    HashMap<Integer,List<Flight>> passengerBookedFlightMap = new HashMap<>();


    public void addAirportToDB(Airport airport){
        String key = airport.getAirportName();
        airportHashMap.put(key,airport);
    }

    public HashMap<String,Airport> airportDB(){
        return airportHashMap;
    }

    public void addFlightToDB(Flight flight){
        int key = flight.getFlightId();
        flightHashMap.put(key,flight);
    }

    public HashMap<Integer,Flight> flightDB(){
        return flightHashMap;
    }

    public void addPassengerToDB(Passenger passenger){
        int key = passenger.getPassengerId();
        passengerHashMap.put(key,passenger);
    }

    public HashMap<Integer,Passenger> passengerDB(){
        return passengerHashMap;
    }

    public HashMap<Integer,List<Integer>> passengerFlightDB(){
        return passengerFlightHashMap;
    }

    public HashMap<Integer,List<Flight>> getPassengerBookedFlightDB(){
        return passengerBookedFlightMap;
    }
}
