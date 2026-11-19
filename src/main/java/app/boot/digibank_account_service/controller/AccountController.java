
package app.boot.digibank_account_service.controller;

import app.boot.digibank_account_service.requestDto.AccountOpenRequest;
import app.boot.digibank_account_service.requestDto.AccountUpdateRequest;
import app.boot.digibank_account_service.requestDto.AccountStatusUpdateRequest;

import app.boot.digibank_account_service.responseDto.AccountResponse;
import app.boot.digibank_account_service.responseDto.AccountBalanceResponse;
import app.boot.digibank_account_service.responseDto.AccountVerificationResponse;

import app.boot.digibank_account_service.service.AccountService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/digibank/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // Open a new bank account
    @PostMapping("/open")
    public ResponseEntity<AccountResponse> openAccount(
            @Valid @RequestBody AccountOpenRequest request) {

        AccountResponse response = accountService.openAccount(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get account by account number
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountByNumber(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                accountService.getAccountByNumber(accountNumber));
    }

    // Get all accounts belonging to a customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByCustomerId(
            @PathVariable String customerId) {

        return ResponseEntity.ok(
                accountService.getAccountsByCustomerId(customerId));
    }

    // Update account information
    @PatchMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable String accountNumber,
            @Valid @RequestBody AccountUpdateRequest request) {

        return ResponseEntity.ok(
                accountService.updateAccount(accountNumber, request));
    }

    // Update account status
    @PatchMapping("/{accountNumber}/status")
    public ResponseEntity<AccountResponse> updateAccountStatus(
            @PathVariable String accountNumber,
            @Valid @RequestBody AccountStatusUpdateRequest request) {

        return ResponseEntity.ok(
                accountService.updateAccountStatus(
                        accountNumber, request));
    }

    // Get account balance
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<AccountBalanceResponse> getAccountBalance(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                accountService.getAccountBalance(accountNumber));
    }

    // Get balances of all customer accounts
    @GetMapping("/customer/{customerId}/balance")
    public ResponseEntity<List<AccountBalanceResponse>>
    getCustomerAccountBalances(
            @PathVariable String customerId) {

        return ResponseEntity.ok(
                accountService.getCustomerAccountBalances(customerId));
    }

    // Verify account
    @GetMapping("/{accountNumber}/verify")
    public ResponseEntity<AccountVerificationResponse> verifyAccount(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                accountService.verifyAccount(accountNumber));
    }
}
