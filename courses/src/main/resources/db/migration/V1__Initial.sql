CREATE TABLE courses
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(45),
    state            VARCHAR(45),
    created_at       TIMESTAMP,
    last_modified_at TIMESTAMP
);
