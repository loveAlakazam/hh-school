package io.hhpulus.school.courses.presentation.controller;

import io.hhpulus.school.courses.domain.CourseService;
import io.hhpulus.school.courses.domain.validations.CourseValidator;
import io.hhpulus.school.courses.presentation.dtos.request.ApplyCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.request.CreateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.request.UpdateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;
import io.hhpulus.school.enrollments.presentation.dtos.EnrolledCourseResponseDto;
import io.hhpulus.school.users.presentation.dtos.UserResponseDto;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CourseResponseDto> createCourse(@RequestBody CreateCourseRequestDto requestDto) {
        CourseResponseDto response = courseService.createCourse(requestDto);
        return ResponseEntity.created(URI.create("/courses/" + response.id())).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(@PathVariable("id") long id, @RequestBody UpdateCourseRequestDto requestDto){
        CourseResponseDto response = courseService.updateCourseInfo(
                requestDto
                        .builder()
                        .id(id)
                        .name(requestDto.name())
                        .lecturerName(requestDto.lecturerName())
                        .enrollStartDate(requestDto.enrollStartDate())
                        .enrollEndDate(requestDto.enrollEndDate())
                        .currentEnrollments(requestDto.currentEnrollments())
                        .build());
        return ResponseEntity.ok(response);
    }

    // 강의 신청
    @PostMapping("/apply")
    public ResponseEntity<Integer> applyCourse(@RequestBody ApplyCourseRequestDto requestDto) {
        return ResponseEntity.ok(
            courseService.applyCourse(requestDto.userId(), requestDto.courseId())
        );
    }

    // 현재 신청 가능한 강좌 목록 조회
    @GetMapping
    public ResponseEntity<Page<CourseResponseDto>> getEnableCourses(@RequestParam(value="page", defaultValue= "1") int page ) {
        return ResponseEntity.ok(
            courseService.getEnableCourses(page)
        );
    }

    // 특정 userId 로 신청완료한 강의 목록조회
    @GetMapping("/my")
    public ResponseEntity<List<EnrolledCourseResponseDto>> myEnrolledCourses(@RequestParam(value="userId") long userId) {
        return ResponseEntity.ok(
            courseService.getEnrollFinishedCourses(userId)
        );
    }
}
