package com.RideShare.BtoC.repository;

import com.RideShare.BtoC.entity.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {

    // requests a user has made (history as rider)
    List<RideRequest> findByRiderId(Long riderId);

    List<RideRequest> findByRideOfferId(Long rideOfferId);

    boolean existsByRideOfferIdAndRiderIdAndStatusNot(Long rideOfferId, Long riderId, String status);
}
