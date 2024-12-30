package io.hhpulus.school.enrollments.domain;

import io.hhpulus.school.commons.domain.BaseEntity;
import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.users.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "enrollment", uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "course_id"},
        name = "uk_enrollment_user_id_course_id")
)
@Builder
@Getter
@Setter
@AllArgsConstructor

// 동일유저가 동일강좌를 중복신청하는 것을 방지하기 위해서 무결성제약조건을 걸었습니다.
public class Enrollment extends BaseEntity {
    // 구성필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // 생성자
    public Enrollment() {
    }

    public Enrollment(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    public static Enrollment of(User user, Course course) {
        return new Enrollment(user, course);
    }
}
