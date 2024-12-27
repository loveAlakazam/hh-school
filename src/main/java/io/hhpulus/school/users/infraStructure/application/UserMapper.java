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

    // dto -> entity
    public static User toEntity(UserResponseDto responseDto) {
        if(responseDto == null) return null;

        long id = responseDto.id();
        String name = responseDto.name();
        return new User(id, name);
    }
}
