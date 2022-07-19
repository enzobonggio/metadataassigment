CREATE TABLE students
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(45),
    state            VARCHAR(45),
    created_at       TIMESTAMP,
    last_modified_at TIMESTAMP
);

INSERT INTO students(id, name, state, created_at, last_modified_at)
values (1, 'student 1', 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'student 2', 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'student 3', 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 'student 4', 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (5, 'student 5', 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

ALTER TABLE students
    AUTO_INCREMENT = 6;
