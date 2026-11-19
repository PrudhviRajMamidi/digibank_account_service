
package app.boot.digibank_account_service.service;

import app.boot.digibank_account_service.enums.AccountStatus;
import app.boot.digibank_account_service.enums.AccountType;
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
import app.boot.digibank_account_service.service.impl.AccountServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User user;
    private Account account;

    private AccountOpenRequest openRequest;
    private AccountUpdateRequest updateRequest;
    private AccountStatusUpdateRequest statusUpdateRequest;

    private AccountResponse accountResponse;
    private AccountBalanceResponse balanceResponse;
    private AccountVerificationResponse verificationResponse;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .customerId("12345678901")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .mobileNumber("9876543210")
                .build();

        account = Account.builder()
                .id(1L)
                .accountNumber("123456789012")
                .accountType(AccountType.SAVINGS)
                .accountHolderName("John Doe")
                .balance(new BigDecimal("10000.00"))
                .availableBalance(new BigDecimal("10000.00"))
                .accountCurrency("INR")
                .accountStatus(AccountStatus.ACTIVE)
                .user(user)
                .build();

        openRequest = AccountOpenRequest.builder()
                .customerId("12345678901")
                .accountType(AccountType.SAVINGS)
                .accountCurrency("INR")
                .build();

        updateRequest = AccountUpdateRequest.builder()
                .branchName("Hyderabad Main Branch")
                .accountDescription("Primary savings account")
                .build();

        statusUpdateRequest = AccountStatusUpdateRequest.builder()
                .accountStatus(AccountStatus.FROZEN)
                .build();

        accountResponse = AccountResponse.builder()
                .id(1L)
                .accountNumber("123456789012")
                .accountHolderName("John Doe")
                .accountType(AccountType.SAVINGS)
                .accountStatus(AccountStatus.ACTIVE)
                .build();

        balanceResponse = AccountBalanceResponse.builder()
                .accountNumber("123456789012")
                .accountHolderName("John Doe")
                .accountType(AccountType.SAVINGS)
                .accountStatus(AccountStatus.ACTIVE)
                .balance(new BigDecimal("10000.00"))
                .availableBalance(new BigDecimal("10000.00"))
                .build();

        verificationResponse = AccountVerificationResponse.builder()
                .accountNumber("123456789012")
                .accountExists(true)
                .accountStatus(AccountStatus.ACTIVE)
                .eligibleForTransactions(true)
                .message("Account is active and eligible for transactions")
                .build();
    }

    // OPEN ACCOUNT TESTS

    @Test
    void openAccount_ShouldReturnAccountResponse_WhenValidRequestIsProvided() {

        when(userRepository.findByCustomerId(openRequest.getCustomerId()))
                .thenReturn(Optional.of(user));

        when(accountMapper.toEntity(openRequest, user))
                .thenReturn(account);

        when(accountRepository.existsByAccountNumber(anyString()))
                .thenReturn(false);

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(accountMapper.toResponse(any(Account.class)))
                .thenReturn(accountResponse);

        AccountResponse result = accountService.openAccount(openRequest);

        assertNotNull(result);
        assertEquals("123456789012", result.getAccountNumber());

        assertNotNull(account.getAccountNumber());
        assertTrue(account.getAccountNumber().matches("\\d{12}"));

        verify(userRepository).findByCustomerId(openRequest.getCustomerId());
        verify(accountMapper).toEntity(openRequest, user);
        verify(accountRepository).save(any(Account.class));
        verify(accountMapper).toResponse(any(Account.class));
    }

    @Test
    void openAccount_ShouldThrowException_WhenCustomerDoesNotExist() {

        when(userRepository.findByCustomerId(openRequest.getCustomerId()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> accountService.openAccount(openRequest));

        verify(accountRepository, never()).save(any(Account.class));
        verify(accountMapper, never()).toEntity(any(), any());
    }

    // GET ACCOUNT BY NUMBER TESTS

    @Test
    void getAccountByNumber_ShouldReturnAccountResponse_WhenAccountExists() {

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));

        when(accountMapper.toResponse(account))
                .thenReturn(accountResponse);

        AccountResponse result = accountService.getAccountByNumber("123456789012");

        assertNotNull(result);
        assertEquals("123456789012", result.getAccountNumber());

        verify(accountRepository).findByAccountNumber("123456789012");
        verify(accountMapper).toResponse(account);
    }

    @Test
    void getAccountByNumber_ShouldThrowException_WhenAccountDoesNotExist() {

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> accountService.getAccountByNumber("123456789012"));

        verify(accountMapper, never()).toResponse(any(Account.class));
    }

    // GET ACCOUNTS BY CUSTOMER ID TESTS

    @Test
    void getAccountsByCustomerId_ShouldReturnAccounts_WhenCustomerExists() {

        when(userRepository.existsByCustomerId("12345678901"))
                .thenReturn(true);

        when(accountRepository.findByUser_CustomerId("12345678901"))
                .thenReturn(List.of(account));

        when(accountMapper.toResponse(account))
                .thenReturn(accountResponse);

        List<AccountResponse> result =
                accountService.getAccountsByCustomerId("12345678901");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("123456789012", result.get(0).getAccountNumber());

        verify(userRepository).existsByCustomerId("12345678901");
        verify(accountRepository).findByUser_CustomerId("12345678901");
        verify(accountMapper).toResponse(account);
    }

    @Test
    void getAccountsByCustomerId_ShouldReturnEmptyList_WhenCustomerHasNoAccounts() {

        when(userRepository.existsByCustomerId("12345678901"))
                .thenReturn(true);

        when(accountRepository.findByUser_CustomerId("12345678901"))
                .thenReturn(List.of());

        List<AccountResponse> result =
                accountService.getAccountsByCustomerId("12345678901");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(accountRepository).findByUser_CustomerId("12345678901");
        verify(accountMapper, never()).toResponse(any(Account.class));
    }

    @Test
    void getAccountsByCustomerId_ShouldThrowException_WhenCustomerDoesNotExist() {

        when(userRepository.existsByCustomerId("12345678901"))
                .thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> accountService.getAccountsByCustomerId("12345678901"));

        verify(accountRepository, never())
                .findByUser_CustomerId(anyString());
    }

    // UPDATE ACCOUNT TESTS

    @Test
    void updateAccount_ShouldReturnUpdatedResponse_WhenAccountExists() {

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(account))
                .thenReturn(account);

        when(accountMapper.toResponse(account))
                .thenReturn(accountResponse);

        AccountResponse result =
                accountService.updateAccount("123456789012", updateRequest);

        assertNotNull(result);

        verify(accountRepository).findByAccountNumber("123456789012");
        verify(accountMapper).updateAccountFromRequest(updateRequest, account);
        verify(accountRepository).save(account);
        verify(accountMapper).toResponse(account);
    }

    @Test
    void updateAccount_ShouldThrowException_WhenAccountDoesNotExist() {

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> accountService.updateAccount("123456789012", updateRequest));

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void updateAccount_ShouldThrowException_WhenAccountIsClosed() {

        account.setAccountStatus(AccountStatus.CLOSED);

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));

        assertThrows(IllegalStateException.class,
                () -> accountService.updateAccount("123456789012", updateRequest));

        verify(accountMapper, never())
                .updateAccountFromRequest(any(AccountUpdateRequest.class), any(Account.class));

        verify(accountRepository, never()).save(any(Account.class));
    }

    // UPDATE ACCOUNT STATUS TESTS

    @Test
    void updateAccountStatus_ShouldUpdateStatus_WhenValidTransitionIsProvided() {

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(account))
                .thenReturn(account);

        when(accountMapper.toResponse(account))
                .thenReturn(accountResponse);

        AccountResponse result =
                accountService.updateAccountStatus("123456789012", statusUpdateRequest);

        assertNotNull(result);

        verify(accountMapper)
                .updateAccountStatusFromRequest(statusUpdateRequest, account);

        verify(accountRepository).save(account);
    }

    @Test
    void updateAccountStatus_ShouldCloseAccount_WhenValidClosureRequestIsProvided() {

        AccountStatusUpdateRequest closeRequest =
                AccountStatusUpdateRequest.builder()
                        .accountStatus(AccountStatus.CLOSED)
                        .closureReason("Customer requested account closure")
                        .build();

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(account))
                .thenReturn(account);

        when(accountMapper.toResponse(account))
                .thenReturn(accountResponse);

        AccountResponse result =
                accountService.updateAccountStatus("123456789012", closeRequest);

        assertNotNull(result);
        assertNotNull(account.getClosingDate());
        assertNotNull(account.getClosedAt());

        verify(accountMapper)
                .updateAccountStatusFromRequest(closeRequest, account);

        verify(accountRepository).save(account);
    }

    @Test
    void updateAccountStatus_ShouldThrowException_WhenClosureReasonIsMissing() {

        AccountStatusUpdateRequest closeRequest =
                AccountStatusUpdateRequest.builder()
                        .accountStatus(AccountStatus.CLOSED)
                        .closureReason(" ")
                        .build();

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class,
                () -> accountService.updateAccountStatus("123456789012", closeRequest));

        verify(accountMapper, never())
                .updateAccountStatusFromRequest(any(AccountStatusUpdateRequest.class), any(Account.class));

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void updateAccountStatus_ShouldThrowException_WhenAccountDoesNotExist() {

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> accountService.updateAccountStatus("123456789012", statusUpdateRequest));

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void updateAccountStatus_ShouldThrowException_WhenAccountIsAlreadyClosed() {

        account.setAccountStatus(AccountStatus.CLOSED);

        AccountStatusUpdateRequest activateRequest =
                AccountStatusUpdateRequest.builder()
                        .accountStatus(AccountStatus.ACTIVE)
                        .build();

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));

        assertThrows(IllegalStateException.class,
                () -> accountService.updateAccountStatus("123456789012", activateRequest));

        verify(accountMapper, never())
                .updateAccountStatusFromRequest(any(AccountStatusUpdateRequest.class), any(Account.class));

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void updateAccountStatus_ShouldThrowException_WhenRequestedStatusIsSameAsCurrentStatus() {

        AccountStatusUpdateRequest sameStatusRequest =
                AccountStatusUpdateRequest.builder()
                        .accountStatus(AccountStatus.ACTIVE)
                        .build();

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class,
                () -> accountService.updateAccountStatus("123456789012", sameStatusRequest));

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void updateAccountStatus_ShouldThrowException_WhenStatusTransitionIsInvalid() {

        account.setAccountStatus(AccountStatus.FROZEN);

        AccountStatusUpdateRequest dormantRequest =
                AccountStatusUpdateRequest.builder()
                        .accountStatus(AccountStatus.DORMANT)
                        .build();

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class,
                () -> accountService.updateAccountStatus("123456789012", dormantRequest));

        verify(accountRepository, never()).save(any(Account.class));
    }

    // GET ACCOUNT BALANCE TESTS

    @Test
    void getAccountBalance_ShouldReturnBalance_WhenAccountExists() {

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));

        when(accountMapper.toBalanceResponse(account))
                .thenReturn(balanceResponse);

        AccountBalanceResponse result =
                accountService.getAccountBalance("123456789012");

        assertNotNull(result);
        assertEquals("123456789012", result.getAccountNumber());
        assertEquals(new BigDecimal("10000.00"), result.getBalance());

        verify(accountMapper).toBalanceResponse(account);
    }

    @Test
    void getAccountBalance_ShouldThrowException_WhenAccountDoesNotExist() {

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> accountService.getAccountBalance("123456789012"));

        verify(accountMapper, never()).toBalanceResponse(any(Account.class));
    }

    // GET CUSTOMER ACCOUNT BALANCES TESTS

    @Test
    void getCustomerAccountBalances_ShouldReturnBalances_WhenCustomerExists() {

        when(userRepository.existsByCustomerId("12345678901"))
                .thenReturn(true);

        when(accountRepository.findByUser_CustomerId("12345678901"))
                .thenReturn(List.of(account));

        when(accountMapper.toBalanceResponse(account))
                .thenReturn(balanceResponse);

        List<AccountBalanceResponse> result =
                accountService.getCustomerAccountBalances("12345678901");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("10000.00"), result.get(0).getBalance());

        verify(userRepository).existsByCustomerId("12345678901");
        verify(accountRepository).findByUser_CustomerId("12345678901");
        verify(accountMapper).toBalanceResponse(account);
    }

    @Test
    void getCustomerAccountBalances_ShouldReturnEmptyList_WhenCustomerHasNoAccounts() {

        when(userRepository.existsByCustomerId("12345678901"))
                .thenReturn(true);

        when(accountRepository.findByUser_CustomerId("12345678901"))
                .thenReturn(List.of());

        List<AccountBalanceResponse> result =
                accountService.getCustomerAccountBalances("12345678901");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(accountMapper, never()).toBalanceResponse(any(Account.class));
    }

    @Test
    void getCustomerAccountBalances_ShouldThrowException_WhenCustomerDoesNotExist() {

        when(userRepository.existsByCustomerId("12345678901"))
                .thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> accountService.getCustomerAccountBalances("12345678901"));

        verify(accountRepository, never())
                .findByUser_CustomerId(anyString());
    }

    // VERIFY ACCOUNT TESTS

    @Test
    void verifyAccount_ShouldReturnEligible_WhenAccountIsActive() {

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));

        when(accountMapper.toVerificationResponse(
                account,
                true,
                "Account is active and eligible for transactions"))
                .thenReturn(verificationResponse);

        AccountVerificationResponse result =
                accountService.verifyAccount("123456789012");

        assertNotNull(result);
        assertTrue(result.isAccountExists());
        assertTrue(result.isEligibleForTransactions());

        verify(accountMapper).toVerificationResponse(
                account,
                true,
                "Account is active and eligible for transactions");
    }

    @Test
    void verifyAccount_ShouldReturnNotEligible_WhenAccountIsNotActive() {

        account.setAccountStatus(AccountStatus.FROZEN);

        AccountVerificationResponse frozenResponse =
                AccountVerificationResponse.builder()
                        .accountNumber("123456789012")
                        .accountExists(true)
                        .accountStatus(AccountStatus.FROZEN)
                        .eligibleForTransactions(false)
                        .message("Account is not eligible for transactions")
                        .build();

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));

        when(accountMapper.toVerificationResponse(
                account,
                false,
                "Account is not eligible for transactions"))
                .thenReturn(frozenResponse);

        AccountVerificationResponse result =
                accountService.verifyAccount("123456789012");

        assertNotNull(result);
        assertTrue(result.isAccountExists());
        assertFalse(result.isEligibleForTransactions());

        verify(accountMapper).toVerificationResponse(
                account,
                false,
                "Account is not eligible for transactions");
    }

    @Test
    void verifyAccount_ShouldReturnNotExists_WhenAccountDoesNotExist() {

        AccountVerificationResponse missingResponse =
                AccountVerificationResponse.builder()
                        .accountNumber("123456789012")
                        .accountExists(false)
                        .eligibleForTransactions(false)
                        .message("Account does not exist")
                        .build();

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.empty());

        when(accountMapper.toVerificationResponse(
                isNull(),
                eq(false),
                eq("Account does not exist")))
                .thenReturn(missingResponse);

        AccountVerificationResponse result =
                accountService.verifyAccount("123456789012");

        assertNotNull(result);
        assertFalse(result.isAccountExists());
        assertFalse(result.isEligibleForTransactions());

        verify(accountMapper).toVerificationResponse(
                null,
                false,
                "Account does not exist");
    }
}
