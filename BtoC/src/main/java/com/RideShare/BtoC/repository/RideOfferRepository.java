package com.RideShare.BtoC.repository;

import com.RideShare.BtoC.entity.RideOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RideOfferRepository extends JpaRepository<RideOffer, Long> {

    // used for search: same route, open, and leaving inside the time window
    List<RideOffer> findByOriginIgnoreCaseAndDestinationIgnoreCaseAndStatusAndDepartureTimeBetweenOrderByDepartureTime(
            String origin, String destination, String status, LocalDateTime from, LocalDateTime to);

    // rides a user has published (history as driver)
    List<RideOffer> findByDriverId(Long driverId);
}
