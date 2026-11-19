
package app.boot.digibank_account_service.mapper;

import app.boot.digibank_account_service.model.Account;
import app.boot.digibank_account_service.model.User;
import app.boot.digibank_account_service.requestDto.AccountOpenRequest;
import app.boot.digibank_account_service.requestDto.AccountUpdateRequest;
import app.boot.digibank_account_service.requestDto.AccountStatusUpdateRequest;
import app.boot.digibank_account_service.responseDto.AccountResponse;
import app.boot.digibank_account_service.responseDto.AccountBalanceResponse;
import app.boot.digibank_account_service.responseDto.AccountVerificationResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class AccountMapper {

    // Convert AccountOpenRequest to Account entity
    public Account toEntity(AccountOpenRequest request, User user) {

        if (request == null || user == null) {
            return null;
        }

        BigDecimal initialDeposit = request.getInitialDeposit() != null
                ? request.getInitialDeposit()
                : BigDecimal.ZERO;

        String accountHolderName =
                user.getFirstName()
                        + (user.getMiddleName() != null
                        && !user.getMiddleName().isBlank()
                        ? " " + user.getMiddleName() : "")
                        + " " + user.getLastName();

        return Account.builder()
                .accountType(request.getAccountType())
                .accountHolderName(accountHolderName)
                .balance(initialDeposit)
                .availableBalance(initialDeposit)
                .accountCurrency(request.getAccountCurrency())
                .branchCode(request.getBranchCode())
                .branchName(request.getBranchName())
                .ifscCode(request.getIfscCode())
                .accountDescription(request.getAccountDescription())
                .openingDate(LocalDate.now())
                .user(user)
                .build();
    }

    // Convert Account entity to AccountResponse
    public AccountResponse toResponse(Account account) {

        if (account == null) {
            return null;
        }

        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .accountHolderName(account.getAccountHolderName())
                .balance(account.getBalance())
                .availableBalance(account.getAvailableBalance())
                .overdraftLimit(account.getOverdraftLimit())
                .accountCurrency(account.getAccountCurrency())
                .interestRate(account.getInterestRate())
                .dailyWithdrawalLimit(account.getDailyWithdrawalLimit())
                .dailyTransferLimit(account.getDailyTransferLimit())
                .minimumBalance(account.getMinimumBalance())
                .branchCode(account.getBranchCode())
                .branchName(account.getBranchName())
                .ifscCode(account.getIfscCode())
                .accountStatus(account.getAccountStatus())
                .accountDescription(account.getAccountDescription())
                .openingDate(account.getOpeningDate())
                .closingDate(account.getClosingDate())
                .closedAt(account.getClosedAt())
                .closureReason(account.getClosureReason())
                .lastTransactionAt(account.getLastTransactionAt())
                .lastStatementGeneratedAt(
                        account.getLastStatementGeneratedAt())
                .userId(account.getUser() != null
                        ? account.getUser().getId() : null)
                .customerId(account.getUser() != null
                        ? account.getUser().getCustomerId() : null)
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    // Convert Account entity to AccountBalanceResponse
    public AccountBalanceResponse toBalanceResponse(Account account) {

        if (account == null) {
            return null;
        }

        return AccountBalanceResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountHolderName(account.getAccountHolderName())
                .accountType(account.getAccountType())
                .accountStatus(account.getAccountStatus())
                .accountCurrency(account.getAccountCurrency())
                .balance(account.getBalance())
                .availableBalance(account.getAvailableBalance())
                .overdraftLimit(account.getOverdraftLimit())
                .minimumBalance(account.getMinimumBalance())
                .lastTransactionAt(account.getLastTransactionAt())
                .build();
    }

    // Convert account verification details to response
    public AccountVerificationResponse toVerificationResponse(
            Account account,
            boolean eligibleForTransactions,
            String message) {

        if (account == null) {
            return AccountVerificationResponse.builder()
                    .accountExists(false)
                    .eligibleForTransactions(false)
                    .message(message)
                    .build();
        }

        return AccountVerificationResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountExists(true)
                .accountStatus(account.getAccountStatus())
                .eligibleForTransactions(eligibleForTransactions)
                .message(message)
                .build();
    }

    // Update permitted account information
    public void updateAccountFromRequest(
            AccountUpdateRequest request,
            Account account) {

        if (request == null || account == null) {
            return;
        }

        if (request.getBranchName() != null) {
            account.setBranchName(request.getBranchName());
        }

        if (request.getAccountDescription() != null) {
            account.setAccountDescription(
                    request.getAccountDescription());
        }
    }

    // Update account status and closure reason
    public void updateAccountStatusFromRequest(
            AccountStatusUpdateRequest request,
            Account account) {

        if (request == null || account == null) {
            return;
        }

        if (request.getAccountStatus() != null) {
            account.setAccountStatus(request.getAccountStatus());
        }

        if (request.getClosureReason() != null) {
            account.setClosureReason(request.getClosureReason());
        }
    }
}
