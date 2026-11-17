
package app.boot.digibank_account_service.service.impl;

import app.boot.digibank_account_service.model.User;
import app.boot.digibank_account_service.repository.UserRepository;
import app.boot.digibank_account_service.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {

        if (userRepository.existsByCustomerId(user.getCustomerId())) {
            throw new RuntimeException("Customer ID already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByMobileNumber(user.getMobileNumber())) {
            throw new RuntimeException("Mobile number already exists");
        }

        if (userRepository.existsByAadhaarNumber(user.getAadhaarNumber())) {
            throw new RuntimeException("Aadhaar number already exists");
        }

        if (userRepository.existsByPanNumber(user.getPanNumber())) {
            throw new RuntimeException("PAN number already exists");
        }

        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByCustomerId(String customerId) {

        return userRepository.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with Customer ID: " + customerId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, User updatedUser) {

        User existingUser = getUserById(id);

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setMiddleName(updatedUser.getMiddleName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setMobileNumber(updatedUser.getMobileNumber());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setAadhaarNumber(updatedUser.getAadhaarNumber());
        existingUser.setPanNumber(updatedUser.getPanNumber());

        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setCity(updatedUser.getCity());
        existingUser.setState(updatedUser.getState());
        existingUser.setPincode(updatedUser.getPincode());
        existingUser.setCountry(updatedUser.getCountry());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {

        User existingUser = getUserById(id);

        userRepository.delete(existingUser);
    }
}
