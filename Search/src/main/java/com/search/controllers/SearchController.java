package com.search.controllers;

import com.search.entity.Flights;
import com.search.services.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/search")
@Tag(name = "Flight Search", description = "Search for flights by source destination and date")
public class SearchController {

    private SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("test")
    public String test() {
        return "This is Test";
    }

    @GetMapping("/filter")
    @Operation(summary = "Get Flights", description = "Search available flights by source, destination and date")
    public ResponseEntity<List<Flights>> searchFlights(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate flightDate) {

        List<Flights> flightsList = searchService.getAllFlights(source, destination, flightDate);
        return ResponseEntity.ok(flightsList);
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get All Flights", description = "Get all the flight")
    public ResponseEntity<List<Flights>> getAllFlights() {
        List<Flights> flightsList = searchService.getAllAvailableFlights();
        return ResponseEntity.ok(flightsList);
    }
}
