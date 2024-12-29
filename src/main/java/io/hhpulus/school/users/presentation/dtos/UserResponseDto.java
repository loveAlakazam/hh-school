package io.hhpulus.school.users.presentation.dtos;

import io.hhpulus.school.users.domain.User;

// record 를 사용하는 이유
// - read-only.
// - 데이터 변경을 막아주므로, data carrier 역할
public record UserResponseDto(long id, String name) {

    // 엔티티 변환
    public User toEntity() {
        return new User(id, name);
    }
}
