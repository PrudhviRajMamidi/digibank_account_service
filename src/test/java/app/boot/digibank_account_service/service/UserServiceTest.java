package app.boot.digibank_account_service.service;

import app.boot.digibank_account_service.model.User;
import app.boot.digibank_account_service.repository.UserRepository;
import app.boot.digibank_account_service.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .customerId("12345678901")
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .mobileNumber("9876543210")
                .password("Password@123")
                .aadhaarNumber("123456789012")
                .panNumber("ABCDE1234F")
                .build();
    }

    // 1. Create user successfully
    @Test
    void createUser_Success() {

        when(userRepository.existsByCustomerId(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByMobileNumber(anyString())).thenReturn(false);
        when(userRepository.existsByAadhaarNumber(anyString())).thenReturn(false);
        when(userRepository.existsByPanNumber(anyString())).thenReturn(false);

        when(passwordEncoder.encode("Password@123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository).save(user);
    }

    // 2. Duplicate customer ID
    @Test
    void createUser_DuplicateCustomerId_ThrowsException() {

        when(userRepository.existsByCustomerId("12345678901"))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.createUser(user)
        );

        assertEquals("Customer ID already exists",
                exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    // 3. Duplicate email
    @Test
    void createUser_DuplicateEmail_ThrowsException() {

        when(userRepository.existsByCustomerId(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.createUser(user)
        );

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    // 4. Duplicate mobile number
    @Test
    void createUser_DuplicateMobileNumber_ThrowsException() {

        when(userRepository.existsByCustomerId(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByMobileNumber("9876543210"))
                .thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> userService.createUser(user));

        verify(userRepository, never()).save(any());
    }

    // 5. Duplicate Aadhaar
    @Test
    void createUser_DuplicateAadhaar_ThrowsException() {

        when(userRepository.existsByCustomerId(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByMobileNumber(anyString())).thenReturn(false);
        when(userRepository.existsByAadhaarNumber("123456789012"))
                .thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> userService.createUser(user));

        verify(userRepository, never()).save(any());
    }

    // 6. Duplicate PAN
    @Test
    void createUser_DuplicatePAN_ThrowsException() {

        when(userRepository.existsByCustomerId(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByMobileNumber(anyString())).thenReturn(false);
        when(userRepository.existsByAadhaarNumber(anyString())).thenReturn(false);
        when(userRepository.existsByPanNumber("ABCDE1234F"))
                .thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> userService.createUser(user));

        verify(userRepository, never()).save(any());
    }

    // 7. Get user by ID
    @Test
    void getUserById_Success() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
    }

    // 8. Get user by ID - not found
    @Test
    void getUserById_NotFound_ThrowsException() {

        when(userRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.getUserById(100L));
    }

    // 9. Get user by customer ID
    @Test
    void getUserByCustomerId_Success() {

        when(userRepository.findByCustomerId("12345678901"))
                .thenReturn(Optional.of(user));

        User result =
                userService.getUserByCustomerId("12345678901");

        assertNotNull(result);
        assertEquals("12345678901", result.getCustomerId());
    }

    // 10. Get all users
    @Test
    void getAllUsers_Success() {

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("John", result.getFirst().getFirstName());
    }

    // 11. Update user
    @Test
    void updateUser_Success() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = User.builder()
                .firstName("Updated")
                .lastName("Doe")
                .email("updated@example.com")
                .mobileNumber("9876543210")
                .build();

        User result = userService.updateUser(1L, updatedUser);

        assertEquals("Updated", result.getFirstName());
        assertEquals("updated@example.com", result.getEmail());

        verify(userRepository).save(user);
    }

    // 12. Delete user
    @Test
    void deleteUser_Success() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }

    // 13. Delete user - not found
    @Test
    void deleteUser_NotFound_ThrowsException() {

        when(userRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.deleteUser(100L));

        verify(userRepository, never()).delete(any());
    }
}
