package io.hhpulus.school.users.infraStructure.application;

import io.hhpulus.school.users.domain.User;
import io.hhpulus.school.users.presentation.dtos.UserResponseDto;

public class UserMapper {
    // entity -> dto 변환
    public static UserResponseDto toResponseDto(User user) {
        if(user == null)  return null;

        long id = user.getId();
        String name = user.getName();
        return new UserResponseDto(id, name);
    }
}
