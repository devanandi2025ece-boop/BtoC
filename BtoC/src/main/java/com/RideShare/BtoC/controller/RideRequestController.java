package com.RideShare.BtoC.controller;

import com.RideShare.BtoC.entity.RideRequest;
import com.RideShare.BtoC.service.RideRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RideRequestController {

    @Autowired
    private RideRequestService rideRequestService;

    @GetMapping
    public List<RideRequest> getAllRequests() {
        return rideRequestService.getAllRequests();
    }

    @GetMapping("/{id}")
    public RideRequest getRequest(@PathVariable Long id) {
        return rideRequestService.getRequestById(id);
    }

    // PUT /api/requests/1/approve?driverId=1
    @PutMapping("/{id}/approve")
    public RideRequest approve(@PathVariable Long id, @RequestParam Long driverId) {
        return rideRequestService.approveRequest(id, driverId);
    }

    // PUT /api/requests/1/reject?driverId=1
    @PutMapping("/{id}/reject")
    public RideRequest reject(@PathVariable Long id, @RequestParam Long driverId) {
        return rideRequestService.rejectRequest(id, driverId);
    }
}
