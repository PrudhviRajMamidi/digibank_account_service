
package app.boot.digibank_account_service.service;

import app.boot.digibank_account_service.requestDto.AccountOpenRequest;
import app.boot.digibank_account_service.requestDto.AccountUpdateRequest;
import app.boot.digibank_account_service.requestDto.AccountStatusUpdateRequest;

import app.boot.digibank_account_service.responseDto.AccountResponse;
import app.boot.digibank_account_service.responseDto.AccountBalanceResponse;
import app.boot.digibank_account_service.responseDto.AccountVerificationResponse;

import java.util.List;

public interface AccountService {

    AccountResponse openAccount(AccountOpenRequest request);

    AccountResponse getAccountByNumber(String accountNumber);

    List<AccountResponse> getAccountsByCustomerId(String customerId);

    AccountResponse updateAccount(
            String accountNumber,
            AccountUpdateRequest request
    );

    AccountResponse updateAccountStatus(
            String accountNumber,
            AccountStatusUpdateRequest request
    );

    AccountBalanceResponse getAccountBalance(String accountNumber);

    List<AccountBalanceResponse> getCustomerAccountBalances(String customerId);

    AccountVerificationResponse verifyAccount(String accountNumber);
}
