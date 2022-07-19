CREATE TABLE courses
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(45),
    state            VARCHAR(45),
    created_at       TIMESTAMP,
    last_modified_at TIMESTAMP
);

INSERT INTO courses(id, name, state, created_at, last_modified_at)
values (1, 'course 1', 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'course 2', 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'course 3', 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (4, 'course 4', 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (5, 'course 5', 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

ALTER TABLE courses AUTO_INCREMENT = 6;
