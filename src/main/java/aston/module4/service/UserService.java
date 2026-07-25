package aston.module4.service;

import aston.module4.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, User dto);
    void deleteUser(Long id);

}
