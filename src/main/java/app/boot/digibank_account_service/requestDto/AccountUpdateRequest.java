
package app.boot.digibank_account_service.requestDto;

import jakarta.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateRequest {

    @Size(max = 100)
    private String branchName;

    @Size(max = 500)
    private String accountDescription;
}
