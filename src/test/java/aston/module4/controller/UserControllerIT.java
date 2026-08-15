package aston.module4.controller;

import aston.module4.dto.UserCreateUpdateDto;
import aston.module4.entity.User;
import aston.module4.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserControllerIT {

    @Container
    private static final PostgreSQLContainer postgres = new PostgreSQLContainer(org.testcontainers.utility.DockerImageName.parse("postgres:18-alpine"))
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void clearDatabase() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("API: Успешное создание нового пользователя")
    void testCreateUserApi() throws Exception {
        UserCreateUpdateDto dto = new UserCreateUpdateDto();
        dto.setName("Ivan");
        dto.setEmail("ivan@mail.ru");
        dto.setAge(18);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.email").value("ivan@mail.ru"))
                .andExpect(jsonPath("$.age").value(18))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.update.href").exists())
                .andExpect(jsonPath("$._links.delete.href").exists())
                .andExpect(jsonPath("$._links.all-users.href").exists());
    }

    @Test
    @DisplayName("API: Получение существующего пользователя по ID")
    void testGetUserByIdApi() throws Exception {
        User user = new User("Egor", "egor@mail.ru", 29);
        User savedUser = userRepository.save(user);

        mockMvc.perform(get("/users/" + savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.name").value("Egor"))
                .andExpect(jsonPath("$.email").value("egor@mail.ru"))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/users/" + savedUser.getId()))
                .andExpect(jsonPath("$._links.all-users.href").value("http://localhost/users"));
    }

    @Test
    @DisplayName("API: Ошибка 400 Bad Request при пустом имени")
    void testCreateUserValidationFailed() throws Exception {
        UserCreateUpdateDto dto = new UserCreateUpdateDto();
        dto.setName("");
        dto.setEmail("invalid-email");
        dto.setAge(-1);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("API: Список пользователей содержит ссылки для каждого элемента")
    void testGetAllUsersApi() throws Exception {
        User user1 = new User("User1", "user1@mail.ru", 20);
        User user2 = new User("User2", "user2@mail.ru", 22);
        userRepository.save(user1);
        userRepository.save(user2);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userResponseDtoList").isArray())
                .andExpect(jsonPath("$._embedded.userResponseDtoList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/users"));
    }

    @Test
    @DisplayName("API: Удаление пользователя")
    void testDeleteUserApi() throws Exception {
        User user = new User("Igor", "igor@mail.ru", 40);
        User savedUser = userRepository.save(user);

        mockMvc.perform(delete("/users/" + savedUser.getId()))
                .andExpect(status().isNoContent());

        assertTrue(userRepository.findById(savedUser.getId()).isEmpty());
    }
}