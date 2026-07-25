package aston.module4.service;

import aston.module4.dto.UserCreateUpdateDto;
import aston.module4.dto.UserResponseDto;
import aston.module4.entity.User;
import aston.module4.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Ошибка создания: Пользователь с таким email уже существует")
    void testCreateUserThrowsExceptionWhenEmailExists() {
        UserCreateUpdateDto dto = new UserCreateUpdateDto();
        dto.setName("Egor");
        dto.setEmail("egor@mail.ru");
        dto.setAge(29);

        when(userRepository.existsByEmail("egor@mail.ru")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    void testCreateUserSuccess() {
        UserCreateUpdateDto dto = new UserCreateUpdateDto();
        dto.setName("Egor");
        dto.setEmail("egor@mail.ru");
        dto.setAge(29);

        User userToSave = dto.toEntity();
        User savedUser = dto.toEntity();
        savedUser.setId(1L);

        when(userRepository.existsByEmail("egor@mail.ru")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDto result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Egor", result.getName());
        assertEquals("egor@mail.ru", result.getEmail());

        verify(userRepository, times(1)).existsByEmail("egor@mail.ru");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Ошибка поиска: Пользователь по ID не найден")
    void testGetUserByIdNotFoundThrowsException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(999L));
    }
}
