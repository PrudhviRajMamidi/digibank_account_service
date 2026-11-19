
package app.boot.digibank_account_service.requestDto;

import app.boot.digibank_account_service.enums.AccountType;
import jakarta.validation.constraints.*;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountOpenRequest {

    @NotBlank(message = "Customer ID is required")
    @Pattern(regexp = "^\\d{11}$",
            message = "Customer ID must contain 11 digits")
    private String customerId;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotBlank(message = "Account currency is required")
    @Pattern(regexp = "^[A-Z]{3}$",
            message = "Currency must be a 3-letter ISO code")
    private String accountCurrency;

    @Size(max = 20)
    private String branchCode;

    @Size(max = 100)
    private String branchName;

    @NotBlank(message = "IFSC code is required")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$",
            message = "Invalid IFSC code")
    private String ifscCode;

    @DecimalMin(value = "0.00",
            message = "Initial deposit cannot be negative")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal initialDeposit;

    @Size(max = 500)
    private String accountDescription;
}
