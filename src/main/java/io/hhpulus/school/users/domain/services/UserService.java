package io.hhpulus.school.users.domain.services;

import io.hhpulus.school.users.presentation.dtos.UserResponseDto;

import java.util.Optional;

public interface UserService {
    UserResponseDto createNewUser(String name); // 신규유저 생성
    UserResponseDto updateUserInfo(long id, String name); // 유저 정보 수정
    Optional<UserResponseDto> getUserInfo(long id); // 유저 검색
}
