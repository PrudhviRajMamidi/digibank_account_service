
package app.boot.digibank_account_service.service.impl;

import app.boot.digibank_account_service.exception.ResourceNotFoundException;
import app.boot.digibank_account_service.mapper.UserMapper;
import app.boot.digibank_account_service.model.User;
import app.boot.digibank_account_service.repository.AccountRepository;
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
    private final AccountRepository accountRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    // Register a new customer
    @Override
    public UserResponse createUser(UserRegistrationRequest request) {

        validateDuplicateUserDetails(request);

        User user = userMapper.toEntity(request);

        user.setCustomerId(generateCustomerId());

        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    // Get customer by database ID
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + id));
        return userMapper.toResponse(user);
    }

    // Get customer by customer ID
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByCustomerId(String customerId) {

        User user = userRepository.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with customer ID: " + customerId));

        return userMapper.toResponse(user);
    }

    // Retrieve all customers
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    // Update customer profile
    @Override
    public UserResponse updateUser(
            Long id,
            UserUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + id));

        userMapper.updateUserFromRequest(request, user);

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    // Delete customer
    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + id));

        if (accountRepository.existsByUser_Id(id)) {

            throw new IllegalStateException(
                    "Customer cannot be deleted while bank accounts exist");
        }

        userRepository.delete(user);
    }

    // Validate duplicate registration details
    private void validateDuplicateUserDetails(
            UserRegistrationRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException(
                    "Email address is already registered");
        }

        if (userRepository.existsByMobileNumber(
                request.getMobileNumber())) {

            throw new IllegalStateException(
                    "Mobile number is already registered");
        }

        if (userRepository.existsByAadhaarNumber(
                request.getAadhaarNumber())) {

            throw new IllegalStateException(
                    "Aadhaar number is already registered");
        }

        if (userRepository.existsByPanNumber(
                request.getPanNumber())) {

            throw new IllegalStateException(
                    "PAN number is already registered");
        }
    }

    // Generate unique 11-digit customer ID starting with 1
    private String generateCustomerId() {

        String customerId;

        do {
            customerId = "1" + String.format(
                    "%010d",
                    secureRandom.nextLong(10_000_000_000L)
            );

        } while (userRepository.existsByCustomerId(customerId));

        return customerId;
    }
}
