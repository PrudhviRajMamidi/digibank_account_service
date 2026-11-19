
package app.boot.digibank_account_service.requestDto;

import app.boot.digibank_account_service.enums.AccountStatus;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStatusUpdateRequest {

    @NotNull(message = "Account status is required")
    private AccountStatus accountStatus;

    @Size(max = 255)
    private String closureReason;
}
