package tn.esprit.spring.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User(1L, "John", "Doe", new Date(0), Role.USER);
    }

    @Test
    void addUser_ShouldReturnSavedUser() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        // Act
        User result = userService.addUser(sampleUser);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        // Arrange
        User updated = new User(1L, "Jane", "Doe", new Date(0), Role.ADMIN);
        when(userRepository.save(any(User.class))).thenReturn(updated);

        // Act
        User result = userService.updateUser(updated);

        // Assert
        assertNotNull(result);
        assertEquals(Role.ADMIN, result.getRole());
        verify(userRepository).save(updated);
    }

    @Test
    void retrieveUser_WhenExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        // Act
        User result = userService.retrieveUser("1");

        // Assert
        assertNotNull(result);
        assertEquals("Doe", result.getLastName());
        verify(userRepository).findById(1L);
    }

    @Test
    void deleteUser_ShouldInvokeRepositoryDelete() {
        // Act
        userService.deleteUser("1");

        // Assert
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(userRepository).deleteById(captor.capture());
        assertEquals(1L, captor.getValue());
    }
}
