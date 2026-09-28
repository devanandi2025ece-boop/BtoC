package com.RideShare.BtoC.controller;

import com.RideShare.BtoC.entity.User;
import com.RideShare.BtoC.service.RideOfferService;
import com.RideShare.BtoC.service.RideRequestService;
import com.RideShare.BtoC.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RideOfferService rideOfferService;

    @Autowired
    private RideRequestService rideRequestService;

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }

    // Feature 5: ride history as both driver and rider
    @GetMapping("/{id}/history")
    public Map<String, Object> getHistory(@PathVariable Long id) {
        User user = userService.getUserById(id);
        Map<String, Object> history = new HashMap<>();
        history.put("user", user.getName());
        history.put("ridesAsDriver", rideOfferService.getRidesByDriver(id));
        history.put("ridesAsRider", rideRequestService.getRequestsByRider(id));
        return history;
    }
}
