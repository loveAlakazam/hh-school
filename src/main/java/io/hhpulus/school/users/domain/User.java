package io.hhpulus.school.users.domain;

import io.hhpulus.school.commons.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
public class User extends BaseEntity {
    // 구성필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    // 생성자
    public User() {
    }

    public User(String name) {
      this();
      this.name = name;
    }

    public User(long id, String name) {
        this(name);
        this.id = id;
    }

    // getter
    public long getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    // setter
    // id는 고유값이기 때문에 setter 을 제외했습니다.
    public void setName(String name) {
        this.name = name;
    }
}
