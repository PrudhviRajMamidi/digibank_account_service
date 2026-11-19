
package app.boot.digibank_account_service.responseDto;

import app.boot.digibank_account_service.enums.AccountStatus;
import app.boot.digibank_account_service.enums.AccountType;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountBalanceResponse {

    private String accountNumber;

    private String accountHolderName;

    private AccountType accountType;

    private AccountStatus accountStatus;

    private String accountCurrency;

    private BigDecimal balance;

    private BigDecimal availableBalance;

    private BigDecimal overdraftLimit;

    private BigDecimal minimumBalance;

    private LocalDateTime lastTransactionAt;
}
