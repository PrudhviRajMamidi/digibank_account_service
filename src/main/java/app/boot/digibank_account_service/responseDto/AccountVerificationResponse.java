
package app.boot.digibank_account_service.responseDto;

import app.boot.digibank_account_service.enums.AccountStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountVerificationResponse {

    private String accountNumber;

    private boolean accountExists;

    private AccountStatus accountStatus;

    private boolean eligibleForTransactions;

    private String message;
}
