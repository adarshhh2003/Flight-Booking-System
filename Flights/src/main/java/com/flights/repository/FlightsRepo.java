package com.flights.repository;

import com.flights.entity.Flights;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightsRepo extends JpaRepository<Flights, Integer> {

    Optional<Flights> findByFlightNumber(int flightNumber);

    @Transactional
    int deleteByFlightNumber(int flightNumber);
}
