package com.RideShare.BtoC.service;

import com.RideShare.BtoC.entity.User;
import com.RideShare.BtoC.exception.BusinessException;
import com.RideShare.BtoC.exception.ResourceNotFoundException;
import com.RideShare.BtoC.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("Email already registered");
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    public User updateUser(Long id, User newData) {
        User user = getUserById(id);
        if (!user.getEmail().equals(newData.getEmail()) && userRepository.existsByEmail(newData.getEmail())) {
            throw new BusinessException("Email already registered");
        }
        user.setName(newData.getName());
        user.setEmail(newData.getEmail());
        user.setPhone(newData.getPhone());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
