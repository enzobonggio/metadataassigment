# Metadata

## Assigment

### School registration system

Design and implement simple school registration system

- Assuming you already have a list of students
- Assuming you already have a list of courses
- A student can register to multiple courses
- A course can have multiple students enrolled in it.
- A course has 50 students maximum
- A student can register to 5 course maximum

Provide the following REST API:

- Create students CRUD
- Create courses CRUD
- Create API for students to register to courses
- Create abilities for user to view all relationships between students and courses
    - Filter all students with a specific course
    - Filter all courses for a specific student
    - Filter all courses without any students
    - Filter all students without any courses

1. Please add the following emails to your private Gitlab/GitHub project
    - anish.visrolia@metadata.io
    - emily.hoang@metadata.io
    - jaime.rojas@metadata.io
    - francisco.martin@metadata.io
    - dmitry.kabanov@metadata.io
    - administrant@optimhire.com

2. Wrap everything in docker-compose and update README.md with following details:
    - Endpoints and payloads
    - How to setup project

3. Technology stack:
    - Java/Groovy
    - Gradle/Maven
    - Spring Boot
    - Docker (docker-compose)
    - JUnit
    - MySQL
    - Other technologies or frameworks which can help you.

4. Provide unit tests at the minimum
5. Project can be stored under any version control system like GitHub, GitLab etc.
6. Code needs to be production-ready and best representing your skillset.
7. IMPORTANT: Please use the tech stack described above

## Microservices architecture



### Scalability

The idea was to have stateless microservices instead of a monolithic app.

| PROS                                                       | CONS                                                            |
|------------------------------------------------------------|-----------------------------------------------------------------|
| Can scale individual components                            | Not easy to create CI/CD                                        |
| Easy to maintain by different teams                        | There could be a delay because of the systems that are involved |
| Fast CI/CD runs                                            | Not easy to debug                                               |
| An error in one specific component don't affect the others |                                                                 |

## Hexagonal architecture



## Endpoints and payloads

### Reverse proxy

## Setup project

### Docker

### Docker compose

## Testing

## Database

### Versioning

## Message broker

## Cache

## Improvements

### Tracing