package com.search.clients;

import com.search.entity.Flights;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "Flights",  url = "http://localhost:8082/flights")
public interface FlightServiceClient {

    @GetMapping("/getAll")
    List<Flights> getAllFlights();
}
