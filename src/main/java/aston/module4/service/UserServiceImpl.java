package aston.module4.service;

import aston.module4.controller.UserController;
import aston.module4.entity.User;
import aston.module4.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final Map<Long, User> userMap;
    private final AtomicLong idCounter;
    private  final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
        this.userMap = new HashMap<>();
        this.idCounter = new AtomicLong();
    }

    @Override
    @Transactional
    public User createUser(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        repository.save(user);
        log.info("User saved: {}", user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User updateUser(Long id, User user) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
