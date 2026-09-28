package com.RideShare.BtoC.controller;

import com.RideShare.BtoC.entity.RideOffer;
import com.RideShare.BtoC.entity.RideRequest;
import com.RideShare.BtoC.service.RideOfferService;
import com.RideShare.BtoC.service.RideRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideOfferController {

    @Autowired
    private RideOfferService rideOfferService;

    @Autowired
    private RideRequestService rideRequestService;

    // POST /api/rides?driverId=1
    @PostMapping
    public ResponseEntity<RideOffer> publishRide(@RequestParam Long driverId, @Valid @RequestBody RideOffer ride) {
        return new ResponseEntity<>(rideOfferService.publishRide(driverId, ride), HttpStatus.CREATED);
    }

    @GetMapping
    public List<RideOffer> getAllRides() {
        return rideOfferService.getAllRides();
    }

    @GetMapping("/{id}")
    public RideOffer getRide(@PathVariable Long id) {
        return rideOfferService.getRideById(id);
    }

    // GET /api/rides/search?origin=Gandhipuram&destination=College&from=2026-10-01T07:30:00&to=2026-10-01T09:30:00
    @GetMapping("/search")
    public List<RideOffer> searchRides(@RequestParam String origin,
                                       @RequestParam String destination,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return rideOfferService.searchRides(origin, destination, from, to);
    }

    @PutMapping("/{id}/cancel")
    public RideOffer cancelRide(@PathVariable Long id, @RequestParam Long driverId) {
        return rideOfferService.cancelRide(id, driverId);
    }


    @PostMapping("/{id}/requests")
    public ResponseEntity<RideRequest> requestSeat(@PathVariable Long id, @RequestParam Long riderId,
                                                   @Valid @RequestBody RideRequest request) {
        return new ResponseEntity<>(rideRequestService.requestSeat(id, riderId, request), HttpStatus.CREATED);
    }

    // all requests for one ride (driver uses this to see who asked)
    @GetMapping("/{id}/requests")
    public List<RideRequest> getRequestsForRide(@PathVariable Long id) {
        return rideRequestService.getRequestsForRide(id);
    }
}
