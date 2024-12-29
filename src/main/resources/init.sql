USE hhplus_school_mysql;

-- 기존 테이블 삭제
DROP TABLE IF EXISTS enrollment;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS course;

-- hhplus_school_mysql.course definition
CREATE TABLE `course` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `current_enrollments` int DEFAULT NULL,
  `enroll_end_date` date NOT NULL,
  `enroll_start_date` date NOT NULL,
  `lecturer_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- hhplus_school_mysql.`user` definition

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_USER_NAME` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- hhplus_school_mysql.enrollment definition

CREATE TABLE `enrollment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `course_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ENROLLMENT_USER_ID_COURSE_ID` (`user_id`,`course_id`),
  KEY `FK_ENROLLMENT_COURSE_ID` (`course_id`),
  CONSTRAINT `FK_ENROLLMENT_COURSE_ID` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `FK_ENROLLMENT_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- 데이터베이스 쿼리문 실행하기
-- 샘플 데이터 삽입: course
INSERT INTO `course` (`id`, `created_at`, `updated_at`, `current_enrollments`, `enroll_end_date`, `enroll_start_date`, `lecturer_name`, `name`)
VALUES
(1, NOW(), NOW(), 1, DATE_ADD(NOW(), INTERVAL 7 DAY), NOW(), 'teacher1', 'test environment'),
(2, NOW(), NOW(), 2, DATE_ADD(NOW(), INTERVAL 7 DAY), NOW(), 'professor2', 'operating system'),
(3, NOW(), NOW(), 0, DATE_ADD(NOW(), INTERVAL 7 DAY), NOW(), 'teacher3', 'boost writing skills'),
(4, NOW(), NOW(), 0, '2024-12-25', '2024-12-23' ,'professor4', 'already-expired')
;

-- 샘플 데이터 삽입: user
INSERT INTO `user` (`id`, `created_at`, `updated_at`, `name`)
VALUES
(1, NOW(), NOW(), 'student1'),
(2, NOW(), NOW(), 'student2'),
(3, NOW(), NOW(), 'student3')
;

-- 샘플 데이터 삽입: enrollment
INSERT INTO `enrollment` (`id`, `created_at`, `updated_at`, `course_id`, `user_id`)
VALUES
(1, NOW(), NOW(), 1, 1),
(2, NOW(), NOW(), 2, 2),
(3, NOW(), NOW(), 2, 3)
;

-- AUTO_INCREMENT 초기화 (옵션)
ALTER TABLE `course` AUTO_INCREMENT = 5;
ALTER TABLE `user` AUTO_INCREMENT = 4;
ALTER TABLE `enrollment` AUTO_INCREMENT = 4;

