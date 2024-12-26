package io.hhpulus.school.enrollments.infraStructure.dataSource;

import org.springframework.stereotype.Repository;

@Repository
public class EnrollmentRepositoryImpl {
    private final EnrollmentORMRepository enrollmentORMRepository;

    public EnrollmentRepositoryImpl(EnrollmentORMRepository enrollmentORMRepository) {
        this.enrollmentORMRepository = enrollmentORMRepository;
    }
}
