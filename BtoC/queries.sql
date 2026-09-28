USE rideshare_db;
SHOW TABLES;

DESCRIBE users;
DESCRIBE ride_offers;
DESCRIBE ride_requests;

SELECT * FROM users;
SELECT * FROM ride_offers;
SELECT * FROM ride_requests;

-- rides with driver name
SELECT r.id, u.name AS driver, r.origin, r.destination, r.departure_time,
       r.total_seats, r.available_seats, r.status
FROM ride_offers r
JOIN users u ON u.id = r.driver_id;

-- who requested which ride
SELECT q.id, rider.name AS rider, driver.name AS driver, r.origin, r.destination,
       q.seats_requested, q.status
FROM ride_requests q
JOIN ride_offers r   ON r.id = q.ride_offer_id
JOIN users rider     ON rider.id = q.rider_id
JOIN users driver    ON driver.id = r.driver_id;

-- search in SQL: same route, open, between 7:30 and 9:30
SELECT * FROM ride_offers
WHERE LOWER(origin) = 'gandhipuram'
  AND LOWER(destination) = 'sri eshwar college'
  AND status = 'OPEN'
  AND departure_time BETWEEN '2026-10-01 07:30:00' AND '2026-10-01 09:30:00';

-- seats approved per ride
SELECT r.id, r.origin, r.destination, SUM(q.seats_requested) AS seats_approved
FROM ride_offers r
JOIN ride_requests q ON q.ride_offer_id = r.id
WHERE q.status = 'APPROVED'
GROUP BY r.id, r.origin, r.destination;
