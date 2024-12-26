package io.hhpulus.school.courses.domain;

import io.hhpulus.school.commons.domain.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
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

    @Column(name="enable_enroll", nullable = false)
    private boolean enableEnroll = true;

    @Column(name = "enroll_start_date", nullable = false)
    private LocalDate enrollStartDate = LocalDate.now(); // 기본값: 현재날짜

    @Column(nullable = false)
    private int days = DEFAULT_DAYS; // 기본값

    @Column(name = "enroll_end_date", nullable = false)
    private LocalDate enrollEndDate = enrollStartDate.plusDays(days); // enrollStartDate + period 일 을 기본값으로 함.


    // 생성자
    public Course() {
    }

    // getter
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public boolean isEnableEnroll() {
        return enableEnroll;
    }

    public LocalDate getEnrollStartDate() {
        return enrollStartDate;
    }

    public int getDays() {
        return days;
    }

    public LocalDate getEnrollEndDate() {
        return enrollEndDate;
    }


    // setter
    public void setId(long id){
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public void setEnableEnroll(boolean enableEnroll) {
        this.enableEnroll = enableEnroll;
    }

    public void setEnrollStartDate(LocalDate enrollStartDate) {
        this.enrollStartDate = enrollStartDate;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setEnrollEndDate(LocalDate enrollEndDate) {
        this.enrollEndDate = enrollEndDate;
    }
    public void setEnrollEndDate(LocalDate enrollStartDate, int days) {
        this.enrollEndDate = enrollStartDate.plusDays(days);
    }

}
