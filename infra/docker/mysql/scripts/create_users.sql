CREATE DATABASE IF NOT EXISTS subscription_db;
CREATE USER IF NOT EXISTS 'subscription_user' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON subscription_db.* TO 'subscription_user';
FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS course_db;
CREATE USER IF NOT EXISTS 'course_user' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON course_db.* TO 'course_user';
FLUSH PRIVILEGES;


CREATE DATABASE IF NOT EXISTS student_db;
CREATE USER IF NOT EXISTS 'student_user' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON student_db.* TO 'student_user';
FLUSH PRIVILEGES;