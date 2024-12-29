package io.hhpulus.school.users.presentation.controller;

import io.hhpulus.school.users.domain.services.UserService;
import io.hhpulus.school.users.presentation.dtos.CreateNewUserRequestDto;
import io.hhpulus.school.users.presentation.dtos.UpdateUserInfoRequestDto;
import io.hhpulus.school.users.presentation.dtos.UserResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> createNewUser(@RequestBody CreateNewUserRequestDto requestDto) {
        String name = requestDto.name();
        UserResponseDto response = userService.createNewUser(name);
        return ResponseEntity.created(URI.create("/users/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserInfo(@PathVariable long id) {
        Optional<UserResponseDto> response = userService.getUserInfo(id);
        return ResponseEntity.ok(response.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserInfo(@PathVariable long id, @RequestBody UpdateUserInfoRequestDto requestDto) {
        String name = requestDto.name();
        UserResponseDto response = userService.updateUserInfo(id, name);
        return ResponseEntity.ok(response);
    }
}
