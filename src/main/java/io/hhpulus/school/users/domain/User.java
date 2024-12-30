package io.hhpulus.school.users.domain;

import io.hhpulus.school.commons.domain.BaseEntity;
import io.hhpulus.school.enrollments.domain.Enrollment;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Setter
@Getter
@NoArgsConstructor
public class User extends BaseEntity {
    // 구성필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    // orphanRemoval: 부모 엔티티와 연관된 자식엔티티의 생명주기를 관리하는 옵션
    @OneToMany(mappedBy= "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Enrollment> enrollments;


    @Builder
    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
