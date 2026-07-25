package aston.module4.dto;

import aston.module4.entity.User;

import java.time.LocalDate;

public class UserGetDto {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private LocalDate createdAt;

    public static UserGetDto fromEntity(User user) {
        if (user == null) return null;

        UserGetDto dto = new UserGetDto();
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
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
