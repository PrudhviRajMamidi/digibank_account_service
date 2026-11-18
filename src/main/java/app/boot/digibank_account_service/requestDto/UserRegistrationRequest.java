package app.boot.digibank_account_service.requestDto;

import app.boot.digibank_account_service.enums.Gender;
import app.boot.digibank_account_service.enums.Country;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Middle name cannot exceed 50 characters")
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Please provide a valid 10-digit Indian mobile number"
    )
    private String mobileNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100,
            message = "Password must contain between 8 and 100 characters")
    private String password;

    @NotBlank(message = "Aadhaar number is required")
    @Pattern(
            regexp = "^[0-9]{12}$",
            message = "Aadhaar number must contain exactly 12 digits"
    )
    private String aadhaarNumber;

    @NotBlank(message = "PAN number is required")
    @Pattern(
            regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$",
            message = "Please provide a valid PAN number"
    )
    private String panNumber;

    private String address;

    private String city;

    private String state;

    @Pattern(
            regexp = "^[0-9]{6}$",
            message = "Pincode must contain exactly 6 digits"
    )
    private String pincode;

    private Country country;
}
