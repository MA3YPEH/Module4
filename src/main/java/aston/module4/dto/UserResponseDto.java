package aston.module4.dto;

import aston.module4.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Schema(description = "DTO ответа с данными пользователя")
public class UserResponseDto extends RepresentationModel<UserResponseDto> {

    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;
    @Schema(description = "Имя пользователя", example = "Ivan")
    private String name;
    @Schema(description = "Email пользователя", example = "ivan@mail.ru")
    private String email;
    @Schema(description = "Возраст пользователя", example = "21")
    private Integer age;
    @Schema(description = "Дата создания пользователя", example = "2026-08-09T12:00:00")
    private LocalDateTime createdAt;

    public static UserResponseDto fromEntity(User user) {
        if (user == null) return null;

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
