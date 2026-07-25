package aston.module4.service;

import aston.module4.dto.UserCreateUpdateDto;
import aston.module4.dto.UserGetDto;

import java.util.List;

public interface UserService {
    UserGetDto createUser(UserCreateUpdateDto dto);
    UserGetDto getUserById(Long id);
    List<UserGetDto> getAllUsers();
    UserGetDto updateUser(Long id, UserCreateUpdateDto dto);
    void deleteUser(Long id);

}
