
package app.boot.digibank_account_service.repository;

import app.boot.digibank_account_service.model.Account;
import app.boot.digibank_account_service.enums.AccountStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByUser_CustomerId(String customerId);

    List<Account> findByUser_Id(Long userId);

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByUser_CustomerId(String customerId);

    boolean existsByUser_Id(Long userId);

    boolean existsByUser_CustomerIdAndAccountStatus(
            String customerId,
            AccountStatus accountStatus
    );
}
