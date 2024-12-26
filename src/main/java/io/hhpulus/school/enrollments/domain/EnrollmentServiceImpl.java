package io.hhpulus.school.enrollments.domain;

import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl implements  EnrollmentService{
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public void addEnrollment() {

    }

    @Override
    public void getEnrollmentsByUserId(long userId) {

    }

    @Override
    public void getEnrollmentsByCourseId(long courseId) {

    }
}
