package com.RideShare.BtoC.service;

import com.RideShare.BtoC.entity.RideOffer;
import com.RideShare.BtoC.entity.User;
import com.RideShare.BtoC.exception.BusinessException;
import com.RideShare.BtoC.exception.ResourceNotFoundException;
import com.RideShare.BtoC.repository.RideOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RideOfferService {

    @Autowired
    private RideOfferRepository rideOfferRepository;

    @Autowired
    private UserService userService;

    // Feature 1: driver publishes a ride
    public RideOffer publishRide(Long driverId, RideOffer ride) {
        User driver = userService.getUserById(driverId);

        if (ride.getOrigin().trim().equalsIgnoreCase(ride.getDestination().trim())) {
            throw new BusinessException("Origin and destination cannot be the same");
        }

        ride.setId(null);
        ride.setDriver(driver);
        ride.setOrigin(ride.getOrigin().trim());
        ride.setDestination(ride.getDestination().trim());
        ride.setAvailableSeats(ride.getTotalSeats());
        ride.setStatus("OPEN");
        return rideOfferRepository.save(ride);
    }

    // Feature 2: rider searches by route and time window
    public List<RideOffer> searchRides(String origin, String destination, LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            throw new BusinessException("'from' time must be before 'to' time");
        }
        return rideOfferRepository
                .findByOriginIgnoreCaseAndDestinationIgnoreCaseAndStatusAndDepartureTimeBetweenOrderByDepartureTime(
                        origin.trim(), destination.trim(), "OPEN", from, to);
    }

    public List<RideOffer> getAllRides() {
        return rideOfferRepository.findAll();
    }

    public RideOffer getRideById(Long id) {
        return rideOfferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id " + id));
    }

    public List<RideOffer> getRidesByDriver(Long driverId) {
        return rideOfferRepository.findByDriverId(driverId);
    }

    // only the driver who published the ride can cancel it
    public RideOffer cancelRide(Long rideId, Long driverId) {
        RideOffer ride = getRideById(rideId);
        if (!ride.getDriver().getId().equals(driverId)) {
            throw new BusinessException("Only the driver of this ride can cancel it");
        }
        if (ride.getStatus().equals("CANCELLED")) {
            throw new BusinessException("Ride is already cancelled");
        }
        ride.setStatus("CANCELLED");
        return rideOfferRepository.save(ride);
    }

    public RideOffer saveRide(RideOffer ride) {
        return rideOfferRepository.save(ride);
    }
}
