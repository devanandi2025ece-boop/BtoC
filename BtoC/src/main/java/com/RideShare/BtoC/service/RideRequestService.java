package com.RideShare.BtoC.service;

import com.RideShare.BtoC.entity.RideOffer;
import com.RideShare.BtoC.entity.RideRequest;
import com.RideShare.BtoC.entity.User;
import com.RideShare.BtoC.exception.BusinessException;
import com.RideShare.BtoC.exception.ResourceNotFoundException;
import com.RideShare.BtoC.repository.RideRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RideRequestService {

    @Autowired
    private RideRequestRepository rideRequestRepository;

    @Autowired
    private RideOfferService rideOfferService;

    @Autowired
    private UserService userService;

    // Feature 3: rider asks for a seat
    public RideRequest requestSeat(Long rideId, Long riderId, RideRequest request) {
        RideOffer ride = rideOfferService.getRideById(rideId);
        User rider = userService.getUserById(riderId);

        // Rule: a user cannot request a seat on their own ride
        if (ride.getDriver().getId().equals(rider.getId())) {
            throw new BusinessException("You cannot request a seat on your own ride");
        }
        if (!ride.getStatus().equals("OPEN")) {
            throw new BusinessException("This ride is " + ride.getStatus() + ", you cannot request a seat");
        }
        if (ride.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("This ride has already left");
        }
        if (request.getSeatsRequested() > ride.getAvailableSeats()) {
            throw new BusinessException("Only " + ride.getAvailableSeats() + " seat(s) available on this ride");
        }
        if (rideRequestRepository.existsByRideOfferIdAndRiderIdAndStatusNot(rideId, riderId, "REJECTED")) {
            throw new BusinessException("You have already requested a seat on this ride");
        }

        request.setId(null);
        request.setRideOffer(ride);
        request.setRider(rider);
        request.setStatus("PENDING");
        request.setRequestedAt(LocalDateTime.now());
        return rideRequestRepository.save(request);
    }

    // Feature 4: driver approves, seats go down
    @Transactional
    public RideRequest approveRequest(Long requestId, Long driverId) {
        RideRequest request = getPendingRequestForDriver(requestId, driverId);
        RideOffer ride = request.getRideOffer();

        // Rule: cannot approve if no seats remain (checked before saving)
        if (ride.getAvailableSeats() == 0 || request.getSeatsRequested() > ride.getAvailableSeats()) {
            throw new BusinessException("Cannot approve, only " + ride.getAvailableSeats() + " seat(s) left on this ride");
        }

        ride.setAvailableSeats(ride.getAvailableSeats() - request.getSeatsRequested());
        if (ride.getAvailableSeats() == 0) {
            ride.setStatus("FULL");
        }
        rideOfferService.saveRide(ride);

        request.setStatus("APPROVED");
        return rideRequestRepository.save(request);
    }

    public RideRequest rejectRequest(Long requestId, Long driverId) {
        RideRequest request = getPendingRequestForDriver(requestId, driverId);
        request.setStatus("REJECTED");
        return rideRequestRepository.save(request);
    }

    public List<RideRequest> getAllRequests() {
        return rideRequestRepository.findAll();
    }

    public List<RideRequest> getRequestsForRide(Long rideId) {
        rideOfferService.getRideById(rideId);
        return rideRequestRepository.findByRideOfferId(rideId);
    }

    public List<RideRequest> getRequestsByRider(Long riderId) {
        return rideRequestRepository.findByRiderId(riderId);
    }

    public RideRequest getRequestById(Long id) {
        return rideRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ride request not found with id " + id));
    }

    // common checks for approve and reject
    private RideRequest getPendingRequestForDriver(Long requestId, Long driverId) {
        RideRequest request = getRequestById(requestId);
        if (!request.getRideOffer().getDriver().getId().equals(driverId)) {
            throw new BusinessException("Only the driver of this ride can approve or reject requests");
        }
        if (!request.getStatus().equals("PENDING")) {
            throw new BusinessException("This request is already " + request.getStatus());
        }
        return request;
    }
}
