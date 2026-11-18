
package app.boot.digibank_account_service.service.impl;

import app.boot.digibank_account_service.mapper.UserMapper;
import app.boot.digibank_account_service.model.User;
import app.boot.digibank_account_service.repository.UserRepository;
import app.boot.digibank_account_service.requestDto.UserRegistrationRequest;
import app.boot.digibank_account_service.requestDto.UserUpdateRequest;
import app.boot.digibank_account_service.responseDto.UserResponse;
import app.boot.digibank_account_service.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    // --------------------------------------------------
    // CREATE USER
    // --------------------------------------------------
    @Override
    public UserResponse createUser(UserRegistrationRequest request) {

        // Validate duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Validate duplicate mobile number
        if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new IllegalArgumentException("Mobile number already exists");
        }

        // Validate duplicate Aadhaar number
        if (userRepository.existsByAadhaarNumber(request.getAadhaarNumber())) {
            throw new IllegalArgumentException("Aadhaar number already exists");
        }

        // Validate duplicate PAN number
        if (userRepository.existsByPanNumber(request.getPanNumber())) {
            throw new IllegalArgumentException("PAN number already exists");
        }

        // Convert registration DTO to entity
        User user = userMapper.toEntity(request);

        // Generate unique customer ID
        user.setCustomerId(generateCustomerId());

        // Encrypt password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save user to database
        User savedUser = userRepository.save(user);

        // Convert entity to response DTO
        return userMapper.toResponse(savedUser);
    }

    // --------------------------------------------------
    // GET USER BY ID
    // --------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with ID: " + id));

        return userMapper.toResponse(user);
    }

    // --------------------------------------------------
    // GET USER BY CUSTOMER ID
    // --------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByCustomerId(String customerId) {

        User user = userRepository.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with Customer ID: " + customerId));

        return userMapper.toResponse(user);
    }

    // --------------------------------------------------
    // GET ALL USERS
    // --------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    // --------------------------------------------------
    // UPDATE USER
    // --------------------------------------------------
    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest request) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with ID: " + id));

        // Update only fields provided in request
        userMapper.updateUserFromRequest(request, existingUser);

        // Save updated user
        User updatedUser = userRepository.save(existingUser);

        // Convert entity to response DTO
        return userMapper.toResponse(updatedUser);
    }

    // --------------------------------------------------
    // DELETE USER
    // --------------------------------------------------
    @Override
    public void deleteUser(Long id) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with ID: " + id));

        userRepository.delete(existingUser);
    }

    // --------------------------------------------------
    // GENERATE CUSTOMER ID
    // --------------------------------------------------
    private String generateCustomerId() {

        String customerId;

        do {
            // Generate 11-digit customer ID starting with 1
            StringBuilder id = new StringBuilder("1");

            for (int i = 0; i < 10; i++) {
                id.append(SECURE_RANDOM.nextInt(10));
            }

            customerId = id.toString();

        } while (userRepository.existsByCustomerId(customerId));

        return customerId;
    }
}
