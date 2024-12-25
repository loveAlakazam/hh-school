package io.hhpulus.school.users.infraStructure.dataSource;

import io.hhpulus.school.users.domain.User;
import io.hhpulus.school.users.domain.UserRepository;
import io.hhpulus.school.users.infraStructure.application.UserMapper;
import io.hhpulus.school.users.presentation.dtos.UserResponseDto;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository{
    private final UserORMRepository userORMRepository;

    public UserRepositoryImpl(UserORMRepository userORMRepository) {
        this.userORMRepository = userORMRepository;
    }

    @Override
    public UserResponseDto createOrUpdate(String name) {
        User user = userORMRepository.save(new User(name));
        return UserMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto createOrUpdate(long id, String name) {
        User user = userORMRepository.save(new User(id, name));
        return UserMapper.toResponseDto(user);
    }

    @Override
    public Optional<UserResponseDto> findById(long id) {
        return userORMRepository.findById(id).map(UserMapper::toResponseDto);
    }

    @Override
    public Optional<UserResponseDto> findByName(String name) {
        return userORMRepository.findByName(name).map(UserMapper::toResponseDto);
    }
}
