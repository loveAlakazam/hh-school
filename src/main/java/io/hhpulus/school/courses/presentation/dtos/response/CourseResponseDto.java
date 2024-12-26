package io.hhpulus.school.courses.presentation.dtos.response;


import java.time.LocalDate;

// read-only
public record CourseResponseDto(
        long id, // 아이디
        String name, // 강의명
        String lectureName, // 강사명
        boolean enableEnroll, // 수강신청가능 여부
        LocalDate enrollStartDate, // 수강신청 시작날짜
        LocalDate enrollEndDate // 수강신청 종료날짜
) {
}
