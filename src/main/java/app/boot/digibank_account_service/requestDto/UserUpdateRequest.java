
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

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String middleName;

    @Size(max = 50)
    private String lastName;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private Gender gender;

    @Size(max = 255)
    private String address;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Pattern(regexp = "^\\d{6}$",
            message = "Pincode must contain 6 digits")
    private String pincode;

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
