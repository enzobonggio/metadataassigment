CREATE TABLE courses
(
    id         BIGINT PRIMARY KEY,
    created_at TIMESTAMP
);

CREATE TABLE students
(
    id         BIGINT PRIMARY KEY,
    created_at TIMESTAMP
);

CREATE TABLE subscriptions
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id  BIGINT,
    student_id BIGINT,
    created_at TIMESTAMP,
    UNIQUE (course_id, student_id),
    FOREIGN KEY (course_id) REFERENCES courses (id),
    FOREIGN KEY (student_id) REFERENCES students (id)
);

CREATE INDEX course_id_idx
    ON subscriptions (course_id);

CREATE INDEX student_id_idx
    ON subscriptions (student_id);

INSERT INTO students(id, created_at)
values (1, CURRENT_TIMESTAMP),
       (2, CURRENT_TIMESTAMP),
       (3, CURRENT_TIMESTAMP),
       (4, CURRENT_TIMESTAMP),
       (5, CURRENT_TIMESTAMP);

INSERT INTO courses(id, created_at)
values (1, CURRENT_TIMESTAMP),
       (2, CURRENT_TIMESTAMP),
       (3, CURRENT_TIMESTAMP),
       (4, CURRENT_TIMESTAMP),
       (5, CURRENT_TIMESTAMP);
