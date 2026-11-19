
package app.boot.digibank_account_service.responseDto;

import app.boot.digibank_account_service.enums.Country;
import app.boot.digibank_account_service.enums.Gender;
import app.boot.digibank_account_service.enums.Role;
import app.boot.digibank_account_service.enums.UserStatus;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String customerId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String mobileNumber;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private Country country;

    private String preferredLanguage;

    private String profileImageUrl;

    private String alternateMobileNumber;

    private String occupation;

    private String maritalStatus;

    private String nationality;

    private Role role;

    private UserStatus status;

    private Boolean emailVerified;

    private Boolean phoneNumberVerified;

    private LocalDateTime lastLoginAt;

    private LocalDateTime passwordChangedAt;

    private LocalDateTime lastPasswordResetAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
