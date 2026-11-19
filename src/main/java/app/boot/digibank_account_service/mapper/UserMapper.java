
package app.boot.digibank_account_service.mapper;

import app.boot.digibank_account_service.model.User;
import app.boot.digibank_account_service.requestDto.UserRegistrationRequest;
import app.boot.digibank_account_service.requestDto.UserUpdateRequest;
import app.boot.digibank_account_service.responseDto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Convert UserRegistrationRequest to User entity
    public User toEntity(UserRegistrationRequest request) {

        if (request == null) {
            return null;
        }

        return User.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobileNumber(request.getMobileNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .password(request.getPassword())
                .aadhaarNumber(request.getAadhaarNumber())
                .panNumber(request.getPanNumber())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .country(request.getCountry())
                .preferredLanguage(request.getPreferredLanguage())
                .profileImageUrl(request.getProfileImageUrl())
                .alternateMobileNumber(request.getAlternateMobileNumber())
                .occupation(request.getOccupation())
                .maritalStatus(request.getMaritalStatus())
                .nationality(request.getNationality())
                .build();
    }

    // Convert User entity to UserResponse
    public UserResponse toResponse(User user) {

        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .customerId(user.getCustomerId())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .pincode(user.getPincode())
                .country(user.getCountry())
                .preferredLanguage(user.getPreferredLanguage())
                .profileImageUrl(user.getProfileImageUrl())
                .alternateMobileNumber(user.getAlternateMobileNumber())
                .occupation(user.getOccupation())
                .maritalStatus(user.getMaritalStatus())
                .nationality(user.getNationality())
                .role(user.getRole())
                .status(user.getStatus())
                .emailVerified(user.isEmailVerified())
                .phoneNumberVerified(user.isPhoneNumberVerified())
                .lastLoginAt(user.getLastLoginAt())
                .passwordChangedAt(user.getPasswordChangedAt())
                .lastPasswordResetAt(user.getLastPasswordResetAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // Update existing User entity from UserUpdateRequest
    public void updateUserFromRequest(
            UserUpdateRequest request,
            User user) {

        if (request == null || user == null) {
            return;
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getMiddleName() != null) {
            user.setMiddleName(request.getMiddleName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }

        if (request.getCity() != null) {
            user.setCity(request.getCity());
        }

        if (request.getState() != null) {
            user.setState(request.getState());
        }

        if (request.getPincode() != null) {
            user.setPincode(request.getPincode());
        }

        if (request.getCountry() != null) {
            user.setCountry(request.getCountry());
        }

        if (request.getPreferredLanguage() != null) {
            user.setPreferredLanguage(request.getPreferredLanguage());
        }

        if (request.getProfileImageUrl() != null) {
            user.setProfileImageUrl(request.getProfileImageUrl());
        }

        if (request.getAlternateMobileNumber() != null) {
            user.setAlternateMobileNumber(
                    request.getAlternateMobileNumber());
        }

        if (request.getOccupation() != null) {
            user.setOccupation(request.getOccupation());
        }

        if (request.getMaritalStatus() != null) {
            user.setMaritalStatus(request.getMaritalStatus());
        }

        if (request.getNationality() != null) {
            user.setNationality(request.getNationality());
        }
    }
}
