package io.hhplus.school.courses.presentations;


import io.hhpulus.school.HHPlusSchoolMainApplication;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = HHPlusSchoolMainApplication.class)
@Transactional
public class CourseControllerTest {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    @DisplayName("데이터베이스의 isolation level 을 확인한다: 트랜잭션 격리수준을 8(SERIALIZABLE) 로 설정해야한다")
    public void checkTransactionIsolation() {
        // Given
        EntityManager em = entityManagerFactory.createEntityManager();

        // When
        // Hibernate의 Session은 JDBC Connection 객체를 직접 다룰 수 있도록한다.
        // 트랜잭션 격리수준 같은 JDBC 레벨 설정을 확인할 수 있다.
        Session session = em.unwrap(Session.class);

        // doReturningWork :
        int isolationLevel = session.doReturningWork(connection -> connection.getTransactionIsolation());

        // Then
        // 일반적으로 mysql의 isolation 레벨이 4 (RepeatableRead) 이다.
        // Serializable(8)은 한개의 트랜잭션에 읽기/쓰기 모든 데이터를 다른 트랜잭션이 접근하지 못하도록 막는다.
        System.out.println("Current Isolation Level: " + isolationLevel);
        assertEquals(Connection.TRANSACTION_SERIALIZABLE, isolationLevel, "The isolation level should be SERIALIZABLE");
    }

}
