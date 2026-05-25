package org.example.backend.services;

import org.example.backend.dtos.RegistrationDto;
import org.example.backend.dtos.UserUpdateDto;
import org.example.backend.dtos.UserPasswordUpdateDto;
import org.example.backend.dtos.UserDto;
import org.example.backend.exceptions.EmailIsAlreadyTakenException;
import org.example.backend.exceptions.UserNotFoundException;
import org.example.backend.exceptions.UserPasswordUpdateException;
import org.example.backend.mappers.mapstruct.UserMapper;
import org.example.backend.models.entities.User;
import org.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final String id = UUID.randomUUID().toString();

    private User mockUser;
    private UserDto mockUserDto;
    private UserUpdateDto mockUserUpdateDto;
    private UserPasswordUpdateDto mockUserPasswordUpdateDto;
    private RegistrationDto mockRegistrationDto;

    @BeforeEach
    void setUp() {
        Instant now = Instant.now();
        mockUser = new User()
                .setId(id)
                .setEmail("test@gmail.com")
                .setEmailVerified(true)
                .setPendingEmail("test1@gmail.com")
                .setPassword("encoded")
                .setNickname("Test")
                .setGivenName("Test")
                .setFamilyName("Test")
                .setCreatedAt(now)
                .setUpdatedAt(now);

        mockUserDto = new UserDto()
                .setEmail(mockUser.getEmail())
                .setNickname(mockUser.getNickname())
                .setGivenName(mockUser.getGivenName())
                .setFamilyName(mockUser.getFamilyName())
                .setCreatedAt(mockUser.getCreatedAt())
                .setUpdatedAt(mockUser.getUpdatedAt());


        mockUserUpdateDto = new UserUpdateDto()
                .setNickname("NewNickname")
                .setFamilyName("NewFamilyName")
                .setGivenName("NewGivenName");

        mockUserPasswordUpdateDto = new UserPasswordUpdateDto()
                .setNewPassword("newPassword")
                .setOldPassword("password");

        mockRegistrationDto = new RegistrationDto()
                .setEmail(mockUser.getEmail())
                .setNickname(mockUser.getNickname())
                .setGivenName(mockUser.getGivenName())
                .setFamilyName(mockUser.getFamilyName())
                .setPassword("decoded");
    }

    @Test
    void getUserById_WhenUserIdIsPresent_ThenReturnUser() {
        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));

        User user = userService.getUserById(id);

        assertNotNull(user);
        assertEquals(mockUser.getId(), user.getId());
        assertEquals(mockUser.getEmail(), user.getEmail());

        verify(userRepository).findById(id);
    }

    @Test
    void getUserById_WhenUserIdIsNotPresent_ThenThrowException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(id));

        verify(userRepository).findById(id);
    }

    @Test
    void getUserDtoById_WhenUserIdIsPresent_ThenReturnUserDto() {
        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));
        when(userMapper.mapEntityToDto(mockUser)).thenReturn(mockUserDto);

        UserDto userDto = userService.getUserDtoById(id);

        assertEquals(mockUserDto, userDto);

        verify(userRepository).findById(id);
        verify(userMapper).mapEntityToDto(mockUser);
    }

    @Test
    void getUserDtoById_WhenUserIdIsNotPresent_ThenThrowException() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserDtoById(id));

        verify(userRepository).findById(id);
    }

    @Test
    void getAllUsers_WhenUsersArePresent_ThenReturnListOfDtos() {
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findAll(any(Example.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(mockUser)));
        when(userMapper.mapEntityToDto(mockUser)).thenReturn(mockUserDto);

        List<UserDto> list = userService.getAllUsers(mockUser, pageable);

        assertEquals(list, List.of(mockUserDto));

        verify(userRepository).findAll(any(Example.class), eq(pageable));
        verify(userMapper).mapEntityToDto(mockUser);
    }

    @Test
    void getAllUsers_WhenUsersAreNotPresent_ThenReturnEmptyList() {
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findAll(any(Example.class), eq(pageable))).thenReturn(new PageImpl<>(List.of()));

        List<UserDto> list = userService.getAllUsers(mockUser, pageable);

        assertEquals(list, List.of());

        verify(userRepository).findAll(any(Example.class), eq(pageable));
        verify(userMapper, never()).mapEntityToDto(mockUser);
    }

    @Test
    void createUser_WhenUserWithEmailIsNotPresent_ThenReturnUser() {
        when(userRepository.findByEmail(mockRegistrationDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(mockRegistrationDto.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.createUser(mockRegistrationDto);

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(mockRegistrationDto.getEmail(), user.getEmail());
        assertEquals("encoded", user.getPassword());
        assertEquals(mockRegistrationDto.getNickname(), user.getNickname());

        verify(userRepository).findByEmail(mockRegistrationDto.getEmail());
        verify(passwordEncoder).encode(mockRegistrationDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WhenUserWithEmailIsPresentAndNotVerified_ThenReturnUser() {
        mockUser.setEmailVerified(false);

        when(userRepository.findByEmail(mockRegistrationDto.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode(mockRegistrationDto.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.createUser(mockRegistrationDto);

        assertNotNull(user);
        assertEquals(mockUser.getId(), user.getId());
        assertEquals(mockRegistrationDto.getEmail(), user.getEmail());
        assertEquals("encoded", user.getPassword());
        assertEquals(mockRegistrationDto.getNickname(), user.getNickname());

        verify(userRepository).findByEmail(mockRegistrationDto.getEmail());
        verify(passwordEncoder).encode(mockRegistrationDto.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_WhenUserWithEmailIsPresentAndVerified_ThenThrowException() {
        when(userRepository.findByEmail(mockRegistrationDto.getEmail())).thenReturn(Optional.of(mockUser));

        assertThrows(EmailIsAlreadyTakenException.class, () -> userService.createUser(mockRegistrationDto));

        verify(userRepository).findByEmail(mockRegistrationDto.getEmail());
        verify(passwordEncoder, never()).encode(mockRegistrationDto.getPassword());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserIdIsPresent_ThenReturnUser() {
        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.updateUser(id, mockUserUpdateDto);

        assertEquals(mockUserUpdateDto.getNickname(), user.getNickname());
        assertEquals(mockUserUpdateDto.getFamilyName(), user.getFamilyName());
        assertEquals(mockUserUpdateDto.getGivenName(), user.getGivenName());

        verify(userRepository).findById(id);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserIdIsNotPresent_ThenThrowException() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(id, mockUserUpdateDto));

        verify(userRepository).findById(id);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updatePassword_WhenUserIdIsPresentAndPasswordsMatches_ThenReturnUser() {
        String oldPassword = mockUser.getPassword();

        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(mockUserPasswordUpdateDto.getOldPassword(), oldPassword))
                .thenReturn(true);
        when(passwordEncoder.encode(mockUserPasswordUpdateDto.getNewPassword())).thenReturn("newEncoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.updatePassword(id, mockUserPasswordUpdateDto);

        assertEquals("newEncoded", user.getPassword());

        verify(userRepository).findById(id);
        verify(passwordEncoder).matches(mockUserPasswordUpdateDto.getOldPassword(), oldPassword);
        verify(passwordEncoder).encode(mockUserPasswordUpdateDto.getNewPassword());
        verify(userRepository).save(any(User.class));

    }

    @Test
    void updatePassword_WhenUserIdIsNotPresent_ThenThrowException() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(id, mockUserPasswordUpdateDto));

        verify(userRepository).findById(id);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updatePassword_WhenUserIdIsPresentAndPasswordsNotMatches_ThenThrowException() {
        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(mockUserPasswordUpdateDto.getOldPassword(), mockUser.getPassword()))
                .thenReturn(false);

        assertThrows(UserPasswordUpdateException.class, () -> userService.updatePassword(id, mockUserPasswordUpdateDto));

        verify(userRepository).findById(id);
        verify(passwordEncoder).matches(mockUserPasswordUpdateDto.getOldPassword(), mockUser.getPassword());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void verifyEmail_WhenEmailIsFree_ThenReturnUser() {
        mockUser.setEmailVerified(false);
        String pendingEmail = mockUser.getPendingEmail();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.verifyEmail(mockUser);

        assertNull(user.getPendingEmail());
        assertTrue(user.isEmailVerified());
        assertEquals(pendingEmail, user.getEmail());

        verify(userRepository).findByEmail(anyString());
        verify(userRepository, never()).delete(any(User.class));
        verify(userRepository, never()).flush();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void verifyEmail_WhenUserWithSuchEmailIsPresentButNotVerified_ThenReturnUser() {
        String pendingEmail = mockUser.getPendingEmail();
        User existingUser = new User()
                .setEmail("test1@gmail.com")
                .setEmailVerified(false);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));
        doNothing().when(userRepository).delete(existingUser);
        doNothing().when(userRepository).flush();
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = userService.verifyEmail(mockUser);

        assertNull(user.getPendingEmail());
        assertTrue(user.isEmailVerified());
        assertEquals(pendingEmail, user.getEmail());

        verify(userRepository).findByEmail(anyString());
        verify(userRepository).delete(existingUser);
        verify(userRepository).flush();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void verifyEmail_WhenUserWithSuchEmailIsPresentAndVerified_ThenThrowException() {
        User existingUser = new User()
                .setEmail("test1@gmail.com")
                .setEmailVerified(true);

        when(userRepository.findByEmail(mockUser.getPendingEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(EmailIsAlreadyTakenException.class, () -> userService.verifyEmail(mockUser));

        verify(userRepository).findByEmail(anyString());
        verify(userRepository, never()).delete(existingUser);
        verify(userRepository, never()).flush();
        verify(userRepository, never()).save(any(User.class));
    }
}
