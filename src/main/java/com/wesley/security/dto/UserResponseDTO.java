package com.wesley.security.dto;

import com.wesley.security.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
  private Long id;
  private String email;

  public static UserResponseDTO toResponseDTO(User user) {
    return new UserResponseDTO(user.getId(), user.getEmail());
  }

  public static List<UserResponseDTO> toResponseDTOs(List<User> users) {
    return users.stream().map(UserResponseDTO::toResponseDTO).toList();
  }

}
