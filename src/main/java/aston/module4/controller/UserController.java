package aston.module4.controller;

import aston.module4.dto.UserCreateUpdateDto;
import aston.module4.dto.UserGetDto;
import aston.module4.entity.User;
import aston.module4.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/module4/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserGetDto> createUser(@Valid @RequestBody UserCreateUpdateDto dto) {
        UserGetDto created = userService.createUser(dto);
        log.info("Called createUser {}", dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getUserById(@PathVariable Long id) {
        log.info("Called getUserById: id = {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserGetDto>> getAllUsers() {
        log.info("Called getAllUsers");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGetDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserCreateUpdateDto dto) {
        log.info("Called updateUser id = {}, user = {}", id, dto);
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        log.info("Called deleteUser: id = {}", id);
        userService.deleteUser(id);
    }
}
