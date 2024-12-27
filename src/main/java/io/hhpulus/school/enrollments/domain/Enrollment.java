package io.hhpulus.school.enrollments.domain;

import io.hhpulus.school.commons.domain.BaseEntity;
import io.hhpulus.school.courses.domain.Course;
import io.hhpulus.school.users.domain.User;
import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
public class Enrollment extends BaseEntity {
    // 구성필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // 유저테이블 외래키
    private User user;

    @ManyToOne
    @JoinColumn(name= "course_id", nullable = false)
    private Course course;

    // 생성자
    public Enrollment() {
    }

    public Enrollment(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    // getter
    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Course getCourse() {
        return course;
    }

    // setter
    public void setId(long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
