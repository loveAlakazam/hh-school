package io.hhplus.school.users.presentations;

import io.hhpulus.school.HHPlusSchoolMainApplication;
import io.hhpulus.school.users.domain.exceptions.UserAlreadyExistsException;
import io.hhpulus.school.users.domain.exceptions.UserNotFoundException;
import io.hhpulus.school.users.domain.services.UserService;
import io.hhpulus.school.users.presentation.dtos.CreateNewUserRequestDto;
import io.hhpulus.school.users.presentation.dtos.UserResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static io.hhpulus.school.users.domain.exceptions.UserErrorMessages.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

@SpringBootTest(classes = HHPlusSchoolMainApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;


    @Test
    @DisplayName("[POST] /users - 중복된 이름으로 유저생성 실패 테스트케이스")
    public void createUser_ShouldThrow_UserAlreadyExistsException() throws Exception {
        // given
        String name = "최은강";
        CreateNewUserRequestDto requestDto = new CreateNewUserRequestDto(name);
        when(userService.createNewUser(anyString())).thenThrow(new UserAlreadyExistsException(ALREADY_EXISTS_NAME)); // Mock 동작

        // when & then
        mvc.perform(
                    post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                ).andDo(print())
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("해당 유저는 이미 존재합니다."))
        ;
    }
    @Test
    @DisplayName("[POST] /users - 유저생성 성공 테스트케이스")
    public void createUser_ShouldReturn_Created() throws Exception {
        // given
        long id = 1L;
        String name = "최은강";
        UserResponseDto mockResponse = new UserResponseDto(id, name);
        CreateNewUserRequestDto requestDto = new CreateNewUserRequestDto(name);
        when(userService.createNewUser(anyString())).thenReturn(mockResponse);

        // when & then
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
            ).andDo(print())
             .andExpect(status().isCreated())
             .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
             .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name));
    }

    @Test
    @DisplayName("[GET] /users/{id} - 존재하지 않은 유저아이디로 유저정보조회 실패 테스트케이스")
    public void getUserInfo_ShouldThrow_UserNotFoundException() throws Exception {
        // given
        long notExistUserId = 999L;
        when(userService.getUserInfo(anyLong())).thenThrow(new UserNotFoundException(USER_ID_DOES_NOT_EXISTS)); // Mock 동작

        // when
        // then
        mvc.perform(
                get("/users/" + notExistUserId).contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
             .andExpect(status().isNotFound())
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("존재하지 않은 회원 입니다"))
        ;
    }
    @Test
    @DisplayName("[GET] /users/{id} - 유저정보조회 성공 테스트케이스")
    public void getUserInfo_ShouldReturn_Ok () throws Exception {
        // given
        long id = 1L;
        String name = "최은강";
        UserResponseDto mockUser = new UserResponseDto(id, name);
        when(userService.getUserInfo(anyLong())).thenReturn(Optional.of(mockUser)); // Mock 동작

        // when
        // then
        mvc.perform(
                    get("/users/" + id).contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
             .andExpect(status().isOk())
             .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
             .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
        ;
    }

    @Test
    @DisplayName("[PUT] /users - 유저정보수정 성공 테스트케이스")
    public void updateUserInfo_ShouldReturn_Ok() throws Exception {
        // given
        long id = 1L;
        String updatedName = "최은강";

        UserResponseDto mockUpdatedUser = new UserResponseDto(id, updatedName);
        CreateNewUserRequestDto requestDto = new CreateNewUserRequestDto(updatedName);
        when(userService.updateUserInfo(anyLong(), anyString())).thenReturn(mockUpdatedUser); // Mock 유저정보 수정

        // when & then
        mvc.perform(put("/users/"+ id )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedName));
    }

}
