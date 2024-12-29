package io.hhplus.school.users.domain;

import io.hhpulus.school.users.domain.UserRepository;
import io.hhpulus.school.users.domain.services.UserService;
import io.hhpulus.school.users.domain.services.UserServiceImpl;
import io.hhpulus.school.users.domain.exceptions.UserAlreadyExistsException;
import io.hhpulus.school.users.domain.exceptions.UserNotFoundException;
import io.hhpulus.school.users.presentation.dtos.UserResponseDto;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static io.hhpulus.school.users.domain.exceptions.UserErrorMessages.ALREADY_EXISTS_NAME;
import static io.hhpulus.school.users.domain.exceptions.UserErrorMessages.USER_ID_DOES_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService; // 테스트 대상 서비스
    @Mock
    private UserRepository userRepository; // Mock 객체

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);


    @BeforeEach
    void setUp() {
        // UserRepository 를 Mock 으로 설정
        userRepository = Mockito.mock(UserRepository.class);

        // UserServiceImpl 에 Mock UserRepository 주입
        userService = new UserServiceImpl(userRepository);
    }

    @Nested
    public class UserServiceIntegrationTest {


        @Test
        @DisplayName("중복된 유저이름으로 회원가입하면 UserAlreadyExistsException 예외를 발생한다")
        public void createUser_shouldThrow_UserAlreadyExistsException() {

            // given
            String duplicateName = "최은강"; // 중복이름
            UserResponseDto existingUser = new UserResponseDto(1L, duplicateName);
            when(userRepository.findByName(duplicateName)).thenReturn(Optional.of(existingUser)); // 중복유저 반환

            // when
            UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> userService.createNewUser(duplicateName));

            // then
            assertEquals(ALREADY_EXISTS_NAME, exception.getMessage());
            verify(userRepository, times(1)).findByName(duplicateName); // 중복 체크 호출 확인
            verify(userRepository, never()).createOrUpdate(anyString()); // 유저생성은 호출되지 않음
        }
        @Test
        @DisplayName("신규회원 생성을 성공한다")
        public void createNewUser_shouldReturn_UserResponseDto() {
            // given
            String name = "최은강";
            UserResponseDto mockResponse = new UserResponseDto(1L, name);
            when(userRepository.createOrUpdate(name)).thenReturn(mockResponse);

            // when
            UserResponseDto result = userService.createNewUser(name);

            // then
            assertNotNull(result); // 생성된 유저는 null 이 아니다.
            assertEquals(1L, result.id()); // id는 1 이다.
            verify(userRepository, times(1)).createOrUpdate(name); // 래포지토리의 createOrUpdate 메서드를 1번 호출했다.
        }


        @Test
        @DisplayName("존재하지 않은 유저아이디로 회원정보를 조회하면 UserNotFoundException 예외가 발생한다")
        public void findById_shouldThrow_UserNotFoundException() {
            // given
            long id = 999L; // 존재하지 않은 회원 ID
            when(userRepository.findById(id)).thenReturn(Optional.empty());

            // when
            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserInfo(id));

            // then
            assertEquals(USER_ID_DOES_NOT_EXISTS, exception.getMessage());
            verify(userRepository, times(1)).findById(id);
        }
        @Test
        @DisplayName("회원정보 조회를 성공한다")
        public void findById_shouldReturn_Optional_UserResponseDto() {
            // given
            long id = 1L;
            String name = "최은강";
            UserResponseDto mockResponse = new UserResponseDto(id, name);
            when(userRepository.findById(id)).thenReturn(Optional.of(mockResponse));

            // when
            Optional<UserResponseDto> result = userService.getUserInfo(id);

            // then
            assertTrue(result.isPresent());
            assertEquals(id, result.get().id());
            assertEquals(name, result.get().name());
            verify(userRepository, times(1)).findById(id);
        }

        @Test
        @DisplayName("존재하지 않은 유저아이디로 회원정보를 수정하면 UserNotFoundException 예외가 발생한다")
        public void updateUserInfo_shouldThrow_UserNotFoundException() {
            // given
            long id = 999L; // 존재하지 않은 회원 ID
            String name = "최은강";
            when(userRepository.findById(id)).thenReturn(Optional.empty()); // Mock 유저아이디로 유저정보 조회

            // when
            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.updateUserInfo(id, name));

            // then
            assertEquals(USER_ID_DOES_NOT_EXISTS, exception.getMessage());
            verify(userRepository, times(1)).findById(id); // 유저 아이디로 유저정보 조회는 호출
            verify(userRepository, never()).findByName(anyString()); // 유저 이름으로 유저정보 조회는 호출되지 않음
            verify(userRepository, never()).createOrUpdate(anyLong(), anyString()); // 유저수정은 호출되지 않음
        }
        @Test
        @DisplayName("중복된 유저이름으로 회원정보를 수정하면 UserAlreadyExistsException 예외가 발생한다")
        public void updateUserInfo_shouldThrow_UserAlreadyExistsException() {
            // given
            long id = 1L;
            String existingName = "최은강";
            String updatedName = "최은강"; // 이미 등록된 유저 이름
            UserResponseDto existingUser = new UserResponseDto(id, existingName);

            when(userRepository.findById(id)).thenReturn(Optional.of(existingUser)); // Mock 유저아이디로 유저정보 조회
            when(userRepository.findByName(updatedName)).thenReturn(Optional.of(existingUser)); // Mock 유저 이름으로 유저정보 조회

            // when
            UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> userService.updateUserInfo(id, updatedName));

            // then
            assertEquals(ALREADY_EXISTS_NAME, exception.getMessage());
            verify(userRepository, times(1)).findById(id); // 유저 아이디로 유저정보 조회는 호출
            verify(userRepository, times(1)).findByName(updatedName); // 유저 이름으로 유저정보 조회는 호출
            verify(userRepository, never()).createOrUpdate(anyLong(), anyString()); // 유저수정은 호출되지 않음

        }
        @Test
        @DisplayName("회원정보 수정을 성공한다")
        public void updateUserInfo_shouldReturn_UserResponseDto() {
            // given
            long id = 1L;
            String existingName = "김은강";
            String updatedName = "최은강";
            UserResponseDto existingUser = new UserResponseDto(id, existingName);
            UserResponseDto updatedUser = new UserResponseDto(id, updatedName);

            when(userRepository.findById(id)).thenReturn(Optional.of(existingUser)); // Mock 유저아이디로 유저정보 조회
            when(userRepository.findByName(updatedName)).thenReturn(Optional.empty()); // Mock 유저 이름 중복여부 확인
            when(userRepository.createOrUpdate(id, updatedName)).thenReturn(updatedUser); // Mock 유저 정보 수정


            // when
            UserResponseDto result = userService.updateUserInfo(id, updatedName);

            // then
            assertNotNull(result);
            assertEquals(id, result.id());
            assertEquals(updatedName, result.name());

            verify(userRepository, times(1)).findById(id); // 유저 조회 호출 검증
            verify(userRepository, times(1)).findByName(updatedName); // 중복 체크 호출 검증
            verify(userRepository, times(1)).createOrUpdate(id, updatedName); // 유저 업데이트 호출 검증
        }

    }
}
