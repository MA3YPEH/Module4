package aston.module4.controller;

import aston.module4.dto.UserCreateUpdateDto;
import aston.module4.dto.UserResponseDto;
import aston.module4.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@Tag(name = "Управление пользователем", description = "CRUD операции")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Создать нового пользователя", description = "Сохраняет нового пользователя в БД")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные")
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateUpdateDto dto) {
        UserResponseDto createdUser = userService.createUser(dto);
        log.info("Called createUser {}", dto);
        addHateoasLinks(createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает данные пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким ID не существует")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@Parameter(description = "ID пользователя", example = "1") @PathVariable Long id) {
        log.info("Called getUserById: id = {}", id);

        UserResponseDto findedUser = userService.getUserById(id);
        addHateoasLinks(findedUser);

        return ResponseEntity.ok(findedUser);
    }

    @Operation(summary = "Получить список всех пользователей", description = "Возвращает коллекцию пользователей")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    @GetMapping
    public ResponseEntity<CollectionModel<UserResponseDto>> getAllUsers() {
        log.info("Called getAllUsers");

        List<UserResponseDto> users = userService.getAllUsers();
        users.forEach(user -> addHateoasLinks(user));

        Link selfLink = linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel();
        CollectionModel<UserResponseDto> allUsers = CollectionModel.of(users, selfLink);


        return ResponseEntity.ok(allUsers);
    }

    @Operation(summary = "Изменить пользователя", description = "Обновляет данные пользователя")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserCreateUpdateDto dto) {
        log.info("Called updateUser id = {}, user = {}", id, dto);

        UserResponseDto updatedUser = userService.updateUser(id, dto);
        addHateoasLinks(updatedUser);

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет запись из БД")
    @ApiResponse(responseCode = "204", description = "Пользователь успешно удален")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Called deleteUser: id = {}", id);

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private void addHateoasLinks(UserResponseDto dto) {
        dto.add(linkTo(methodOn(UserController.class).getUserById(dto.getId())).withSelfRel());

        dto.add(linkTo(methodOn(UserController.class).updateUser(dto.getId(), null)).withRel("update"));

        dto.add(linkTo(methodOn(UserController.class).deleteUser(dto.getId())).withRel("delete"));

        dto.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
    }
}
