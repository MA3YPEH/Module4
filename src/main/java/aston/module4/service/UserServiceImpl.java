package aston.module4.service;

import aston.module4.controller.UserController;
import aston.module4.dto.UserCreateUpdateDto;
import aston.module4.dto.UserGetDto;
import aston.module4.entity.User;
import aston.module4.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserGetDto createUser(UserCreateUpdateDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        User user = dto.toEntity();
        User savedUser = userRepository.save(user);

        return UserGetDto.fromEntity(savedUser);
    }

    @Override
    public UserGetDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));

        return UserGetDto.fromEntity(user);
    }

    @Override
    public List<UserGetDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserGetDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserGetDto updateUser(Long id, UserCreateUpdateDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));

        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Этот email уже занят");
        }

        dto.updateEntity(user);

        return UserGetDto.fromEntity(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Пользователь с ID " + id + " не найден");
        }
        userRepository.deleteById(id);
    }
}
