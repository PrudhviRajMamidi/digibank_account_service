package app.boot.digibank_account_service.model;

import app.boot.digibank_account_service.enums.Country;
import app.boot.digibank_account_service.enums.Gender;
import app.boot.digibank_account_service.enums.Role;
import app.boot.digibank_account_service.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_customer_id", columnNames = "customer_id"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_mobile_number", columnNames = "mobile_number"),
                @UniqueConstraint(name = "uk_users_aadhaar_number", columnNames = "aadhaar_number"),
                @UniqueConstraint(name = "uk_users_pan_number", columnNames = "pan_number")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false, length = 11)
    @NotBlank
    @Size(min = 11, max = 11)
    @Pattern(regexp = "\\d{11}")
    private String customerId;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotBlank
    @Size(max = 50)
    private String firstName;

    @Column(name = "middle_name", length = 50)
    @Size(max = 50)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotBlank
    @Size(max = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @Column(name = "mobile_number", nullable = false, unique = true, length = 10)
    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String mobileNumber;

    @Column(name = "date_of_birth", nullable = false)
    @NotNull
    @Past
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull
    private Gender gender;

    @Column(nullable = false, length = 255)
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @Column(name = "aadhaar_number", nullable = false, unique = true, length = 12)
    @NotBlank
    @Pattern(regexp = "\\d{12}")
    private String aadhaarNumber;

    @Column(name = "pan_number", nullable = false, unique = true, length = 10)
    @NotBlank
    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$")
    private String panNumber;

    @Column(length = 255)
    @Size(max = 255)
    private String address;

    @Column(length = 100)
    @Size(max = 100)
    private String city;

    @Column(length = 100)
    @Size(max = 100)
    private String state;

    @Column(length = 6)
    @Pattern(regexp = "^\\d{6}$")
    private String pincode;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Country country;

    // Additional profile fields
    @Column(name = "preferred_language", length = 50)
    @Size(max = 50)
    private String preferredLanguage;

    @Column(name = "profile_image_url", length = 500)
    @Size(max = 500)
    private String profileImageUrl;

    @Column(name = "alternate_mobile_number", length = 10)
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String alternateMobileNumber;

    @Column(length = 100)
    @Size(max = 100)
    private String occupation;

    @Column(name = "marital_status", length = 30)
    @Size(max = 30)
    private String maritalStatus;

    @Column(length = 100)
    @Size(max = 100)
    private String nationality;

    // Authentication-related timestamps
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    @Column(name = "last_password_reset_at")
    private LocalDateTime lastPasswordResetAt;

    // User role and status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.CUSTOMER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private UserStatus status = UserStatus.PENDING_VERIFICATION;

    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private boolean emailVerified = false;

    @Column(name = "phone_number_verified", nullable = false)
    @Builder.Default
    private boolean phoneNumberVerified = false;

    // Audit fields
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Account relationship
    @OneToMany(
            mappedBy = "user",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<@Valid Account> accounts = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addAccount(Account account) {
        accounts.add(account);
        account.setUser(this);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
        // Do not set account.user to null because the relationship is non-nullable.
    }
}