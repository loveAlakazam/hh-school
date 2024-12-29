package io.hhpulus.school.courses.infraStructure.dataSource;

import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.courses.domain.CourseRepository;
import io.hhpulus.school.courses.domain.exceptions.CourseNotFoundException;
import io.hhpulus.school.courses.infraStructure.applications.CourseMapper;
import io.hhpulus.school.courses.presentation.dtos.request.CreateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.request.UpdateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;
import io.hhpulus.school.users.presentation.dtos.UpdateUserInfoRequestDto;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactoryFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.hhpulus.school.courses.domain.constants.CourseConstants.*;

@Repository
public class CourseRepositoryImpl implements CourseRepository {

    @Autowired
    private final CourseORMRepository courseORMRepository;

    public CourseRepositoryImpl(CourseORMRepository courseORMRepository) {
        this.courseORMRepository = courseORMRepository;
    }


    // 강좌 데이터 생성
    @Transactional
    @Override
    public CourseResponseDto create(CreateCourseRequestDto requestDto) {
        Course course = courseORMRepository.save(requestDto.toEntity());
        return CourseMapper.toResponseDto(course);
    }

    // 강좌 정보 업데이트
    @Transactional
    @Override
    public void update(Course updatedCourse) {
        // save로 하려고했으나 null이 나와서... 다른 방법을...
        this.courseORMRepository.updateInfo(
                updatedCourse.getId(),
                updatedCourse.getName(),
                updatedCourse.getLecturerName(),
                updatedCourse.getEnrollStartDate(),
                updatedCourse.getEnrollEndDate(),
                updatedCourse.getCurrentEnrollments()
        );
    }


    // 강좌 단건 조회
    @Override
    public Optional<CourseResponseDto> findById(long id) {
        return courseORMRepository.findById(id).map(CourseMapper::toResponseDto);
    }

    // 신청가능한 강좌 단건 조회
    @Override
    public Optional<CourseResponseDto> findEnableCourse(long id, LocalDate currentDate) {
        return courseORMRepository.findEnableEnrollCourse(id, currentDate ).map(CourseMapper::toResponseDto);
    }


    // 신청가능한 강좌목록 조회
    @Override
    public Page<CourseResponseDto> getEnableEnrollCourses(LocalDate currentDate, int page) {
        Pageable pageable = PageRequest.of(page -1 , DEFAULT_PAGINATION_SIZE);
        return courseORMRepository.findEnableEnrollCourses(currentDate, pageable).map(CourseMapper::toResponseDto);
    }
}
