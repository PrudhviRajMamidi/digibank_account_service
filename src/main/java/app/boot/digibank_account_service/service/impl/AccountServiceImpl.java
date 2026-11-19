
package app.boot.digibank_account_service.service.impl;

import app.boot.digibank_account_service.enums.AccountStatus;
import app.boot.digibank_account_service.exception.ResourceNotFoundException;
import app.boot.digibank_account_service.mapper.AccountMapper;
import app.boot.digibank_account_service.model.Account;
import app.boot.digibank_account_service.model.User;
import app.boot.digibank_account_service.repository.AccountRepository;
import app.boot.digibank_account_service.repository.UserRepository;

import app.boot.digibank_account_service.requestDto.AccountOpenRequest;
import app.boot.digibank_account_service.requestDto.AccountUpdateRequest;
import app.boot.digibank_account_service.requestDto.AccountStatusUpdateRequest;

import app.boot.digibank_account_service.responseDto.AccountResponse;
import app.boot.digibank_account_service.responseDto.AccountBalanceResponse;
import app.boot.digibank_account_service.responseDto.AccountVerificationResponse;

import app.boot.digibank_account_service.service.AccountService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    private final SecureRandom secureRandom = new SecureRandom();

    // Open a new bank account
    @Override
    public AccountResponse openAccount(AccountOpenRequest request) {

        User user = userRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with customer ID: " + request.getCustomerId()));

        Account account = accountMapper.toEntity(request, user);

        account.setAccountNumber(generateAccountNumber());

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toResponse(savedAccount);
    }

    // Retrieve account using account number
    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountByNumber(String accountNumber) {

        Account account = getAccountEntity(accountNumber);

        return accountMapper.toResponse(account);
    }

    // Retrieve all accounts belonging to a customer
    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByCustomerId(
            String customerId) {

        if (!userRepository.existsByCustomerId(customerId)) {
            throw new ResourceNotFoundException(
                    "User not found with customer ID: " + customerId);
        }

        return accountRepository.findByUser_CustomerId(customerId)
                .stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    // Update permitted account information
    @Override
    public AccountResponse updateAccount(
            String accountNumber,
            AccountUpdateRequest request) {

        Account account = getAccountEntity(accountNumber);

        if (account.getAccountStatus() == AccountStatus.CLOSED) {
            throw new IllegalStateException(
                    "Closed account cannot be updated");
        }

        accountMapper.updateAccountFromRequest(request, account);

        Account updatedAccount = accountRepository.save(account);

        return accountMapper.toResponse(updatedAccount);
    }

    // Update account status
    @Override
    public AccountResponse updateAccountStatus(
            String accountNumber,
            AccountStatusUpdateRequest request) {

        Account account = getAccountEntity(accountNumber);

        AccountStatus currentStatus = account.getAccountStatus();
        AccountStatus requestedStatus = request.getAccountStatus();

        validateStatusTransition(currentStatus, requestedStatus);

        if (requestedStatus == AccountStatus.CLOSED) {

            if (request.getClosureReason() == null
                    || request.getClosureReason().isBlank()) {

                throw new IllegalArgumentException(
                        "Closure reason is required to close an account");
            }

            account.setClosingDate(LocalDate.now());
            account.setClosedAt(LocalDateTime.now());
        }

        accountMapper.updateAccountStatusFromRequest(request, account);

        Account updatedAccount = accountRepository.save(account);

        return accountMapper.toResponse(updatedAccount);
    }

    // Retrieve account balance
    @Override
    @Transactional(readOnly = true)
    public AccountBalanceResponse getAccountBalance(
            String accountNumber) {

        Account account = getAccountEntity(accountNumber);

        return accountMapper.toBalanceResponse(account);
    }

    // Retrieve balances of all customer accounts
    @Override
    @Transactional(readOnly = true)
    public List<AccountBalanceResponse> getCustomerAccountBalances(
            String customerId) {

        if (!userRepository.existsByCustomerId(customerId)) {
            throw new ResourceNotFoundException(
                    "User not found with customer ID: " + customerId);
        }

        return accountRepository.findByUser_CustomerId(customerId)
                .stream()
                .map(accountMapper::toBalanceResponse)
                .toList();
    }

    // Verify whether an account can be used for transactions
    @Override
    @Transactional(readOnly = true)
    public AccountVerificationResponse verifyAccount(
            String accountNumber) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElse(null);

        if (account == null) {
            return accountMapper.toVerificationResponse(
                    null,
                    false,
                    "Account does not exist"
            );
        }

        boolean eligible =
                account.getAccountStatus() == AccountStatus.ACTIVE;

        String message = eligible
                ? "Account is active and eligible for transactions"
                : "Account is not eligible for transactions";

        return accountMapper.toVerificationResponse(
                account,
                eligible,
                message
        );
    }

    // Retrieve account entity or throw exception
    private Account getAccountEntity(String accountNumber) {

        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with account number: " + accountNumber));
    }

    // Generate unique 12-digit account number
    private String generateAccountNumber() {

        String accountNumber;

        do {
            accountNumber = String.format(
                    "%012d",
                    secureRandom.nextLong(1_000_000_000_000L)
            );

        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    // Validate account status transitions
    private void validateStatusTransition(
            AccountStatus currentStatus,
            AccountStatus requestedStatus) {

        if (currentStatus == AccountStatus.CLOSED) {
            throw new IllegalStateException(
                    "Closed account status cannot be changed");
        }

        if (currentStatus == requestedStatus) {
            throw new IllegalArgumentException(
                    "Account already has the requested status");
        }

        if (requestedStatus == AccountStatus.CLOSED) {
            return;
        }

        if (currentStatus == AccountStatus.ACTIVE
                && (requestedStatus == AccountStatus.FROZEN
                || requestedStatus == AccountStatus.DORMANT)) {
            return;
        }

        if ((currentStatus == AccountStatus.FROZEN
                || currentStatus == AccountStatus.DORMANT)
                && requestedStatus == AccountStatus.ACTIVE) {
            return;
        }

        throw new IllegalArgumentException(
                "Invalid account status transition from "
                        + currentStatus + " to " + requestedStatus);
    }
}
