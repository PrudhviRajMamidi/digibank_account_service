package app.boot.digibank_account_service.model;

import app.boot.digibank_account_service.enums.AccountStatus;
import app.boot.digibank_account_service.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "accounts",
        indexes = {
                @Index(name = "idx_accounts_user_id", columnList = "user_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_accounts_account_number", columnNames = "account_number")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    @NotBlank
    @Size(max = 20)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 30)
    @NotNull
    private AccountType accountType;

    @Column(name = "account_holder_name", nullable = false, length = 150)
    @NotBlank
    @Size(max = 150)
    private String accountHolderName;

    @Column(nullable = false, precision = 19, scale = 2)
    @NotNull
    @Digits(integer = 17, fraction = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "available_balance", nullable = false, precision = 19, scale = 2)
    @NotNull
    @Digits(integer = 17, fraction = 2)
    @Builder.Default
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @Column(name = "overdraft_limit", nullable = false, precision = 19, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 17, fraction = 2)
    @Builder.Default
    private BigDecimal overdraftLimit = BigDecimal.ZERO;

    @Column(name = "account_currency", nullable = false, length = 3)
    @NotBlank
    @Size(min = 3, max = 3)
    @Builder.Default
    private String accountCurrency = "INR";

    @Column(name = "interest_rate", precision = 5, scale = 2)
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal interestRate;

    @Column(name = "daily_withdrawal_limit", precision = 19, scale = 2)
    @DecimalMin("0.00")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal dailyWithdrawalLimit;

    @Column(name = "daily_transfer_limit", precision = 19, scale = 2)
    @DecimalMin("0.00")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal dailyTransferLimit;

    @Column(name = "minimum_balance", nullable = false, precision = 19, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 17, fraction = 2)
    @Builder.Default
    private BigDecimal minimumBalance = BigDecimal.ZERO;

    @Column(name = "branch_code", length = 20)
    @Size(max = 20)
    private String branchCode;

    @Column(name = "branch_name", length = 100)
    @Size(max = 100)
    private String branchName;

    @Column(name = "ifsc_code", length = 11)
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$")
    private String ifscCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 30)
    @NotNull
    @Builder.Default
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Column(name = "account_description", length = 500)
    @Size(max = 500)
    private String accountDescription;

    // Account dates
    @Column(name = "opening_date", nullable = false)
    @NotNull
    private LocalDate openingDate;

    @Column(name = "closing_date")
    private LocalDate closingDate;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "closure_reason", length = 255)
    @Size(max = 255)
    private String closureReason;

    // Account activity timestamps
    @Column(name = "last_transaction_at")
    private LocalDateTime lastTransactionAt;

    @Column(name = "last_statement_generated_at")
    private LocalDateTime lastStatementGeneratedAt;

    // User relationship
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    // Audit fields
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;

        if (openingDate == null) {
            openingDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}