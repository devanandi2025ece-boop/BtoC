package com.RideShare.BtoC.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
@Table(name = "ride_requests")
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // which ride the seat is asked for
    @ManyToOne
    @JoinColumn(name = "ride_offer_id")
    private RideOffer rideOffer;

    // the user who wants the seat
    @ManyToOne
    @JoinColumn(name = "rider_id")
    private User rider;

    @NotNull(message = "Seats requested is required")
    @Positive(message = "Seats requested must be more than 0")
    private Integer seatsRequested;

    // PENDING, APPROVED or REJECTED
    private String status;

    private LocalDateTime requestedAt;

    public RideRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RideOffer getRideOffer() {
        return rideOffer;
    }

    public void setRideOffer(RideOffer rideOffer) {
        this.rideOffer = rideOffer;
    }

    public User getRider() {
        return rider;
    }

    public void setRider(User rider) {
        this.rider = rider;
    }

    public Integer getSeatsRequested() {
        return seatsRequested;
    }

    public void setSeatsRequested(Integer seatsRequested) {
        this.seatsRequested = seatsRequested;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }
}
