
package app.boot.digibank_account_service.service;

import app.boot.digibank_account_service.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUserById(Long id);

    User getUserByCustomerId(String customerId);

    List<User> getAllUsers();

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}
