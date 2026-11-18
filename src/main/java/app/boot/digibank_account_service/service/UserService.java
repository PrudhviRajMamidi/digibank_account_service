package app.boot.digibank_account_service.service;

import app.boot.digibank_account_service.requestDto.UserRegistrationRequest;
import app.boot.digibank_account_service.requestDto.UserUpdateRequest;
import app.boot.digibank_account_service.responseDto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRegistrationRequest request);

    UserResponse getUserById(Long id);

    UserResponse getUserByCustomerId(String customerId);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);
}