package io.hhpulus.school.users.domain;

import io.hhpulus.school.users.presentation.dtos.UserResponseDto;

import java.util.Optional;

public interface UserRepository {
    UserResponseDto createOrUpdate (String name);

    UserResponseDto createOrUpdate(long id, String name);

    Optional<UserResponseDto> findById(long id);

    Optional<UserResponseDto> findByName(String name);

    User save(User user);

    void deleteAll();
}
