package io.hhpulus.school.courses.presentation.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.courses.domain.validations.CourseValidator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.sql.Update;

import java.time.LocalDate;

import static io.hhpulus.school.courses.domain.Course.DEFAULT_DAYS;
import static io.hhpulus.school.courses.domain.validations.CourseValidator.*;

@Builder
public record UpdateCourseRequestDto (
        Long id,
        String name,
        String lecturerName,

        @JsonFormat(pattern= "yyyy-MM-dd")
        LocalDate enrollStartDate,

        @JsonFormat(pattern= "yyyy-MM-dd")
        LocalDate enrollEndDate,
        Integer currentEnrollments
){

    public UpdateCourseRequestDto {
        if(id != null) {
            checkCourseId(id);
        }
        if(name != null) {
            checkCourseName(name);
        }
        if(lecturerName != null) {
            checkLecturerName(lecturerName);
        }
        if(enrollStartDate != null) {
            checkEnrollStartDate(enrollStartDate);
        }
        if(enrollStartDate != null && enrollEndDate != null) {
            checkEnrollEndDate(enrollStartDate, enrollEndDate);
        }
        if(currentEnrollments != null) {
            checkCurrentEnrollments(currentEnrollments);
        }
    }

}
