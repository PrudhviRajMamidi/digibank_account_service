
package app.boot.digibank_account_service.controller;

import app.boot.digibank_account_service.requestDto.UserRegistrationRequest;
import app.boot.digibank_account_service.requestDto.UserUpdateRequest;
import app.boot.digibank_account_service.responseDto.UserResponse;
import app.boot.digibank_account_service.service.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/digibank/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // --------------------------------------------------
    // REGISTER USER
    // POST /digibank/user/register
    // --------------------------------------------------
    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRegistrationRequest request) {

        UserResponse response = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // --------------------------------------------------
    // GET USER BY ID
    // GET /digibank/user/{id}
    // --------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        UserResponse response = userService.getUserById(id);

        return ResponseEntity.ok(response);
    }

    // --------------------------------------------------
    // GET USER BY CUSTOMER ID
    // GET /digibank/user/customer/{customerId}
    // --------------------------------------------------
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<UserResponse> getUserByCustomerId(
            @PathVariable String customerId) {

        UserResponse response = userService.getUserByCustomerId(customerId);

        return ResponseEntity.ok(response);
    }

    // --------------------------------------------------
    // GET ALL USERS
    // GET /digibank/user
    // --------------------------------------------------
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    // --------------------------------------------------
    // UPDATE USER
    // PATCH /digibank/user/{id}
    // --------------------------------------------------
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        UserResponse response = userService.updateUser(id, request);

        return ResponseEntity.ok(response);
    }

    // --------------------------------------------------
    // DELETE USER
    // DELETE /digibank/user/{id}
    // --------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
