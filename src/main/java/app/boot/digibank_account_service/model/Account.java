package app.boot.digibank_account_service.model;

import app.boot.digibank_account_service.enums.AccountStatus;
import app.boot.digibank_account_service.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

    @Entity
    @Table(
            name = "accounts",
            indexes = {
                    @Index(name = "idx_accounts_user_id", columnList = "user_id")
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
        @Column(
                name = "account_number",
                nullable = false,
                unique = true,
                length = 20
        )
        private String accountNumber;
        @Enumerated(EnumType.STRING)
        @Column(name = "account_type", nullable = false, length = 30)
        private AccountType accountType;
        @Column(
                name = "account_holder_name",
                nullable = false,
                length = 150
        )
        private String accountHolderName;

        @Column(
                nullable = false,
                precision = 19,
                scale = 2
        )
        @Builder.Default
        private BigDecimal balance = BigDecimal.ZERO;

        @Column(length = 3)
        @Builder.Default
        private String currency = "INR";

        @Column(name = "branch_code", length = 20)
        private String branchCode;

        @Column(name = "ifsc_code", length = 11)
        private String ifscCode;

        @Enumerated(EnumType.STRING)
        @Column(name = "account_status", nullable = false, length = 20)
        @Builder.Default
        private AccountStatus accountStatus = AccountStatus.ACTIVE;

        @Column(name = "minimum_balance", precision = 19, scale = 2)
        @Builder.Default
        private BigDecimal minimumBalance = BigDecimal.ZERO;

        @Column(name = "opening_date", nullable = false)
        private LocalDate openingDate;

        @Column(name = "closing_date")
        private LocalDate closingDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

    }
