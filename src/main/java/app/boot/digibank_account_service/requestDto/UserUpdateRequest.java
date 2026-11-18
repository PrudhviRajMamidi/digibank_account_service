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
public class UserUpdateRequest {

    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Middle name cannot exceed 50 characters")
    private String middleName;

    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private Gender gender;

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
