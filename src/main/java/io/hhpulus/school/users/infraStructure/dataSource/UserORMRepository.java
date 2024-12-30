package io.hhpulus.school.users.infraStructure.dataSource;

import io.hhpulus.school.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserORMRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
}
