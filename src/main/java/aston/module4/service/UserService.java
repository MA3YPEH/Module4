package aston.module4.service;

import aston.module4.dto.UserCreateUpdateDto;
import aston.module4.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserCreateUpdateDto dto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id, UserCreateUpdateDto dto);
    void deleteUser(Long id);

}
