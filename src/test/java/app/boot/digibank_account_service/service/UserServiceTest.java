
package app.boot.digibank_account_service.service;

import app.boot.digibank_account_service.exception.ResourceNotFoundException;
import app.boot.digibank_account_service.mapper.UserMapper;
import app.boot.digibank_account_service.model.User;
import app.boot.digibank_account_service.repository.AccountRepository;
import app.boot.digibank_account_service.repository.UserRepository;
import app.boot.digibank_account_service.requestDto.UserRegistrationRequest;
import app.boot.digibank_account_service.requestDto.UserUpdateRequest;
import app.boot.digibank_account_service.responseDto.UserResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponse userResponse;
    private UserRegistrationRequest registrationRequest;
    private UserUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .customerId("12345678901")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .mobileNumber("9876543210")
                .password("encodedPassword")
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .customerId("12345678901")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .mobileNumber("9876543210")
                .build();

        registrationRequest = UserRegistrationRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .mobileNumber("9876543210")
                .password("Password@123")
                .aadhaarNumber("123456789012")
                .panNumber("ABCDE1234F")
                .build();

        updateRequest = UserUpdateRequest.builder()
                .firstName("UpdatedJohn")
                .build();
    }

    // CREATE USER TESTS

    @Test
    void createUser_ShouldReturnUserResponse_WhenValidRequestIsProvided() {

        when(userRepository.existsByEmail(registrationRequest.getEmail()))
                .thenReturn(false);
        when(userRepository.existsByMobileNumber(registrationRequest.getMobileNumber()))
                .thenReturn(false);
        when(userRepository.existsByAadhaarNumber(registrationRequest.getAadhaarNumber()))
                .thenReturn(false);
        when(userRepository.existsByPanNumber(registrationRequest.getPanNumber()))
                .thenReturn(false);

        when(userMapper.toEntity(registrationRequest))
                .thenReturn(user);
        when(passwordEncoder.encode(registrationRequest.getPassword()))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.toResponse(any(User.class)))
                .thenReturn(userResponse);

        UserResponse result = userService.createUser(registrationRequest);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("john.doe@gmail.com", result.getEmail());
        assertNotNull(user.getCustomerId());
        assertTrue(user.getCustomerId().matches("1\\d{10}"));
        assertEquals("encodedPassword", user.getPassword());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(registrationRequest.getPassword());
        verify(userMapper).toResponse(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {

        when(userRepository.existsByEmail(registrationRequest.getEmail()))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> userService.createUser(registrationRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenMobileNumberAlreadyExists() {

        when(userRepository.existsByEmail(registrationRequest.getEmail()))
                .thenReturn(false);
        when(userRepository.existsByMobileNumber(registrationRequest.getMobileNumber()))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> userService.createUser(registrationRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenAadhaarAlreadyExists() {

        when(userRepository.existsByEmail(registrationRequest.getEmail()))
                .thenReturn(false);
        when(userRepository.existsByMobileNumber(registrationRequest.getMobileNumber()))
                .thenReturn(false);
        when(userRepository.existsByAadhaarNumber(registrationRequest.getAadhaarNumber()))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> userService.createUser(registrationRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenPanAlreadyExists() {

        when(userRepository.existsByEmail(registrationRequest.getEmail()))
                .thenReturn(false);
        when(userRepository.existsByMobileNumber(registrationRequest.getMobileNumber()))
                .thenReturn(false);
        when(userRepository.existsByAadhaarNumber(registrationRequest.getAadhaarNumber()))
                .thenReturn(false);
        when(userRepository.existsByPanNumber(registrationRequest.getPanNumber()))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> userService.createUser(registrationRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    // GET USER BY ID TESTS

    @Test
    void getUserById_ShouldReturnUserResponse_WhenUserExists() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userMapper.toResponse(user))
                .thenReturn(userResponse);

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());

        verify(userRepository).findById(1L);
        verify(userMapper).toResponse(user);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserDoesNotExist() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(1L));

        verify(userMapper, never()).toResponse(any(User.class));
    }

    // GET USER BY CUSTOMER ID TESTS

    @Test
    void getUserByCustomerId_ShouldReturnUserResponse_WhenUserExists() {

        when(userRepository.findByCustomerId("12345678901"))
                .thenReturn(Optional.of(user));
        when(userMapper.toResponse(user))
                .thenReturn(userResponse);

        UserResponse result = userService.getUserByCustomerId("12345678901");

        assertNotNull(result);
        assertEquals("12345678901", result.getCustomerId());
        assertEquals("John", result.getFirstName());

        verify(userRepository).findByCustomerId("12345678901");
        verify(userMapper).toResponse(user);
    }

    @Test
    void getUserByCustomerId_ShouldThrowException_WhenUserDoesNotExist() {

        when(userRepository.findByCustomerId("12345678901"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByCustomerId("12345678901"));

        verify(userMapper, never()).toResponse(any(User.class));
    }

    // GET ALL USERS TESTS

    @Test
    void getAllUsers_ShouldReturnListOfUserResponses() {

        User secondUser = User.builder()
                .id(2L)
                .customerId("12345678902")
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@gmail.com")
                .build();

        UserResponse secondResponse = UserResponse.builder()
                .id(2L)
                .customerId("12345678902")
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@gmail.com")
                .build();

        when(userRepository.findAll())
                .thenReturn(List.of(user, secondUser));
        when(userMapper.toResponse(user))
                .thenReturn(userResponse);
        when(userMapper.toResponse(secondUser))
                .thenReturn(secondResponse);

        List<UserResponse> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());

        verify(userRepository).findAll();
        verify(userMapper, times(2)).toResponse(any(User.class));
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersExist() {

        when(userRepository.findAll())
                .thenReturn(List.of());

        List<UserResponse> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository).findAll();
        verify(userMapper, never()).toResponse(any(User.class));
    }

    // UPDATE USER TESTS

    @Test
    void updateUser_ShouldReturnUpdatedUserResponse_WhenUserExists() {

        UserResponse updatedResponse = UserResponse.builder()
                .id(1L)
                .customerId("12345678901")
                .firstName("UpdatedJohn")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user))
                .thenReturn(user);
        when(userMapper.toResponse(user))
                .thenReturn(updatedResponse);

        UserResponse result = userService.updateUser(1L, updateRequest);

        assertNotNull(result);
        assertEquals("UpdatedJohn", result.getFirstName());

        verify(userRepository).findById(1L);
        verify(userMapper).updateUserFromRequest(updateRequest, user);
        verify(userRepository).save(user);
        verify(userMapper).toResponse(user);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(1L, updateRequest));

        verify(userRepository, never()).save(any(User.class));
        verify(userMapper, never())
                .updateUserFromRequest(any(UserUpdateRequest.class), any(User.class));
    }

    // DELETE USER TESTS

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExistsAndHasNoAccounts() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(accountRepository.existsByUser_Id(1L))
                .thenReturn(false);

        userService.deleteUser(1L);

        verify(userRepository).findById(1L);
        verify(accountRepository).existsByUser_Id(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserDoesNotExist() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(1L));

        verify(accountRepository, never()).existsByUser_Id(anyLong());
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserHasAccounts() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(accountRepository.existsByUser_Id(1L))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> userService.deleteUser(1L));

        verify(accountRepository).existsByUser_Id(1L);
        verify(userRepository, never()).delete(any(User.class));
    }
}
