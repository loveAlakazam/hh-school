package io.hhpulus.school.users.domain.services;

import io.hhpulus.school.users.domain.UserRepository;
import io.hhpulus.school.users.domain.exceptions.UserAlreadyExistsException;
import io.hhpulus.school.users.domain.exceptions.UserNotFoundException;
import io.hhpulus.school.users.presentation.dtos.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static io.hhpulus.school.users.domain.exceptions.UserErrorMessages.ALREADY_EXISTS_NAME;
import static io.hhpulus.school.users.domain.exceptions.UserErrorMessages.USER_ID_DOES_NOT_EXISTS;

@Service
public class UserServiceImpl implements  UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto createNewUser(String name) {
        // name 으로 중복유저 체크
        Optional<UserResponseDto> user = userRepository.findByName(name);
        if(user.isPresent()) {
            throw new UserAlreadyExistsException(ALREADY_EXISTS_NAME);
        }

        // 유저 추가
        return userRepository.createOrUpdate(name);
    }

    @Override
    public Optional<UserResponseDto> getUserInfo(long id) throws UserNotFoundException {
        Optional<UserResponseDto> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new UserNotFoundException(USER_ID_DOES_NOT_EXISTS);
        }
        return user;
    }

    @Override
    public UserResponseDto updateUserInfo(long id, String name) {
        // 유저조회
        this.getUserInfo(id);

        // 중복유저 체크
        Optional<UserResponseDto> user = userRepository.findByName(name);
        if(user.isPresent()) {
            throw new UserAlreadyExistsException(ALREADY_EXISTS_NAME);
        }

        // 유저정보 수정
        return userRepository.createOrUpdate(id, name);
    }
}
