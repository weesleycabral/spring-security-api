package com.wesley.security.dto;

import java.util.List;

import com.wesley.security.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {
  private Long id;
  private String email;
  private String password;

  public static User toEntity(UserRegistrationDTO dto) {
    return new User(dto.getId(), dto.getEmail(), dto.getPassword());
  }

  public static UserRegistrationDTO toDTO(User user) {
    return new UserRegistrationDTO(user.getId(), user.getEmail(), null);
  }

  public static List<UserRegistrationDTO> toDTOs(List<User> users) {
    return users.stream().map(UserRegistrationDTO::toDTO).toList();
  }
}
