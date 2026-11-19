
package app.boot.digibank_account_service.responseDto;

import app.boot.digibank_account_service.enums.AccountStatus;
import app.boot.digibank_account_service.enums.AccountType;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {

    private Long id;

    private String accountNumber;

    private AccountType accountType;

    private String accountHolderName;

    private BigDecimal balance;

    private BigDecimal availableBalance;

    private BigDecimal overdraftLimit;

    private String accountCurrency;

    private BigDecimal interestRate;

    private BigDecimal dailyWithdrawalLimit;

    private BigDecimal dailyTransferLimit;

    private BigDecimal minimumBalance;

    private String branchCode;

    private String branchName;

    private String ifscCode;

    private AccountStatus accountStatus;

    private String accountDescription;

    private LocalDate openingDate;

    private LocalDate closingDate;

    private LocalDateTime closedAt;

    private String closureReason;

    private LocalDateTime lastTransactionAt;

    private LocalDateTime lastStatementGeneratedAt;

    private Long userId;

    private String customerId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
