package io.hhpulus.school.courses.domain;

import io.hhpulus.school.commons.domain.BaseEntity;
import io.hhpulus.school.enrollments.domain.Enrollment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course extends BaseEntity {
    // 상수값
    public static final int DEFAULT_DAYS = 7;

    // 구성필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "lecturer_name", nullable = false)
    private String lecturerName;

    @Builder.Default
    @Column(name = "enroll_start_date", nullable = false)
    private LocalDate enrollStartDate = LocalDate.now(); // 기본값: 현재날짜


    @Builder.Default
    @Column(name = "enroll_end_date", nullable = false)
    private LocalDate enrollEndDate = LocalDate.now().plusDays(DEFAULT_DAYS);

    // 현재 강의 신청자 수
    @Builder.Default
    @Column(name="current_enrollments")
    private int currentEnrollments = 0;


    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Enrollment> enrollments;

    @Builder
    public Course(long id, String name, String lecturerName, LocalDate enrollStartDate, LocalDate enrollEndDate, int currentEnrollments) {
        this.id = id;
        this.name = name;
        this.lecturerName = lecturerName;
        this.enrollStartDate = enrollStartDate;
        this.enrollEndDate = enrollEndDate;
        this.currentEnrollments = currentEnrollments;
    }
}
