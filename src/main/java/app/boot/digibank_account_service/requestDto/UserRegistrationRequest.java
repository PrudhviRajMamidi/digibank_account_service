
package app.boot.digibank_account_service.requestDto;

import app.boot.digibank_account_service.enums.Country;
import app.boot.digibank_account_service.enums.Gender;
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
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid Indian mobile number")
    private String mobileNumber;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100,
            message = "Password must contain 8 to 100 characters")
    private String password;

    @NotBlank(message = "Aadhaar number is required")
    @Pattern(regexp = "^\\d{12}$",
            message = "Aadhaar number must contain 12 digits")
    private String aadhaarNumber;

    @NotBlank(message = "PAN number is required")
    @Pattern(regexp = "^[A-Z]{5}\\d{4}[A-Z]$",
            message = "Invalid PAN number")
    private String panNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 255)
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 100)
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100)
    private String state;

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^\\d{6}$",
            message = "Pincode must contain 6 digits")
    private String pincode;

    @NotNull(message = "Country is required")
    private Country country;

    @Size(max = 30)
    private String preferredLanguage;

    @Size(max = 500)
    private String profileImageUrl;

    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid alternate mobile number")
    private String alternateMobileNumber;

    @Size(max = 100)
    private String occupation;

    @Size(max = 30)
    private String maritalStatus;

    @Size(max = 50)
    private String nationality;
}
