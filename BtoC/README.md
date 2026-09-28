# RideShareLite - Office/College Carpool Matching Platform

Spring Boot + MySQL backend. Drivers publish a ride (route, time, seats), riders search by route and time window and request seats, drivers approve or reject.

## Setup
1. Generate project in Spring Initializr (Maven, Java, Spring Boot 4.1.1, Java 25)
   Group: com.RideShare, Artifact: BtoC, Package: com.RideShare.BtoC
   Dependencies: Spring Web, Spring Data JPA, MySQL Driver, Spring Boot DevTools, Validation
2. Copy the entity, repository, service, controller and exception folders into src/main/java/com/RideShare/BtoC
3. In application.properties put your MySQL password
4. Run BtoCApplication, then test with the Postman collection

## Entities
- User (id, name, email, phone)
- RideOffer (id, driver -> User, origin, destination, departureTime, totalSeats, availableSeats, status)
- RideRequest (id, rideOffer -> RideOffer, rider -> User, seatsRequested, status, requestedAt)

User 1 - N RideOffer, RideOffer 1 - N RideRequest, User 1 - N RideRequest

## Business rules
- A request cannot be approved if no seats remain
- A user cannot request a seat on their own ride
- Others: origin and destination must differ, departure must be in the future,
  seats must be more than 0, only the ride's driver can approve/reject/cancel,
  no duplicate request on the same ride, approved seats reduce available seats (0 = FULL)

## APIs
| Method | URL | Use |
|---|---|---|
| POST | /api/users | add user |
| GET | /api/users, /api/users/{id} | view users |
| PUT | /api/users/{id} | update user |
| DELETE | /api/users/{id} | delete user |
| GET | /api/users/{id}/history | rides as driver and as rider |
| POST | /api/rides?driverId= | publish ride |
| GET | /api/rides, /api/rides/{id} | view rides |
| GET | /api/rides/search?origin=&destination=&from=&to= | search rides |
| PUT | /api/rides/{id}/cancel?driverId= | cancel ride |
| POST | /api/rides/{id}/requests?riderId= | request seat |
| GET | /api/rides/{id}/requests | requests for a ride |
| GET | /api/requests, /api/requests/{id} | view requests |
| PUT | /api/requests/{id}/approve?driverId= | approve |
| PUT | /api/requests/{id}/reject?driverId= | reject |

Errors come back as JSON with status and message (400 for bad input or rule broken, 404 for not found).
