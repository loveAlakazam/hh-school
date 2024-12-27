package io.hhpulus.school.courses.infraStructure.dataSource;

import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.courses.domain.CourseRepository;
import io.hhpulus.school.courses.infraStructure.applications.CourseMapper;
import io.hhpulus.school.courses.presentation.dtos.request.CreateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.request.UpdateCourseRequestDto;
import io.hhpulus.school.courses.presentation.dtos.response.CourseResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

import static io.hhpulus.school.courses.domain.constants.CourseConstants.DEFAULT_PAGINATION_PAGE;
import static io.hhpulus.school.courses.domain.constants.CourseConstants.DEFAULT_PAGINATION_SIZE;

@Repository
public class CourseRepositoryImpl implements CourseRepository {
    private final CourseORMRepository courseORMRepository;

    public CourseRepositoryImpl(CourseORMRepository courseORMRepository) {
        this.courseORMRepository = courseORMRepository;
    }


    @Override
    public CourseResponseDto create(CreateCourseRequestDto requestDto) {
        Course course = courseORMRepository.save(requestDto.toEntity());
        return CourseMapper.toResponseDto(course);
    }

    @Override
    public CourseResponseDto update(UpdateCourseRequestDto requestDto) {
        Course course = courseORMRepository.save(requestDto.toEntity());
        return CourseMapper.toResponseDto(course);
    }


    @Override
    public Optional<CourseResponseDto> findById(long id) {
        return courseORMRepository.findById(id).map(CourseMapper::toResponseDto);
    }

    @Override
    public Optional<CourseResponseDto> findEnableCourse(long id, LocalDate currentDate) {
        return courseORMRepository.findEnableEnrollCourse(id, currentDate).map(CourseMapper::toResponseDto);
    }


    @Override
    public Page<CourseResponseDto> getEnableEnrollCourses(LocalDate currentDate, int page) {
        PageRequest pageRequest = PageRequest.of(page, DEFAULT_PAGINATION_SIZE);
        return courseORMRepository.findEnableEnrollCourses(currentDate, pageRequest).map(CourseMapper::toResponseDto);
    }
}
