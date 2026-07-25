package aston.module4.dto;

import aston.module4.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserCreateUpdateDto {
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotNull(message = "Возраст обязателен")
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    private Integer age;

    public User toEntity() {
        return new User(this.name, this.email, this.age);
    }

    public void updateEntity(User user) {
        user.setName(this.name);
        user.setEmail(this.email);
        user.setAge(this.age);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
