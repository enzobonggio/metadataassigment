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

![Diagram](diagrams/Microservices%20architecture.drawio.png)

### Scalability

The idea was to have stateless microservices instead of a monolithic app.

| PROS                                                       | CONS                                                            |
|------------------------------------------------------------|-----------------------------------------------------------------|
| Can scale individual components                            | Not easy to create CI/CD                                        |
| Easy to maintain by different teams                        | There could be a delay because of the systems that are involved |
| Fast CI/CD runs                                            | Not easy to debug                                               |
| An error in one specific component don't affect the others |                                                                 |

## Hexagonal architecture

I choose this architecture mainly because over the years I saw some kind of implementation of this but with some twiks
depending on the project. There are some ideas that should be followed in order to implement it:

* have a domain layer where you could speak a shared language with product team
* write the business logic on that domain layer
* have a port for each adapter, that port can be implemented it to do integration tests
* separate ports between the ones that receive data `input` and the ones that send data to the outside `output`
* Try to leave the infrastructure logic outside the adapters

## Endpoints and payloads

The easiest way to show how to open a rest what could be a possible response is easy to use json schemas. I choose open
api because it has an integration with `spring-web`.

With the project running you could access to the api definitions and examples

* [Courses CRUD](http://localhost/courses/swagger-ui/index.html)
* [Students CRUD](http://localhost/students/swagger-ui/index.html)
* [Subscriptions create and fetch](http://localhost/subscriptions/swagger-ui/index.html)

### Reverse proxy

In order to have a better experience as someone who is going to consume this endpoints I added a reverse proxy to have
all the URL solved by one port `80`.

For this I used `traefik` for this because I already knew how it works and it works with docker

## Monitoring

For monitoring, I added actuator. It gives a lot of functionality out of box.
Actuator works very well with micrometer and with that I expose all metrics using `micrometer-registry-prometheus`.
So in docker composes we have a `prometheus` that is consuming those metrics under `http://localhost:9090`

## Setup project

### Prerequisite

* We will need port `80` available for `traeffik`. We could check this doing `lsof -i :80`
* We will need port `9090` available for `prometheus`. We could check this doing `lsof -i :9090`
* We will need `docker` and `docker-compose` installed

### Docker

* Separate build and run to reduce the size of the image.
* Cache m2 folder in order to reuse dependencies once downloaded.
* Separate jar on classes, libs and meta-inf to reduce unnecessary jar info

After we checked all prerequisite we just run `docker-compose up -d --build`.
This will test build all the microservices, it could take sometime the first time. Remember to have enough RAM on you
docker.

## Testing

### CRUD

The idea inside the CRUDs is the same as they aren't have differences.
Unit tests for all adapters and services.
Integration tests for services but with a mock on repository, this was made to lower down the time of execution of
tests. Using `h2` db made them take a lot of time, and it was not the point of checking the domain logic.

### Subscriptions

Here the main driver of the tests is to check how `kafka` is working. That is why we are using a embedded with `h2`. So
we could say that here we are testing an `e2e` flow.

## Database

Instead of using the same database for all the information I decide to use one for each microservice, that way the
information is not couple and could increase the size of the db instance depending on what we need. The election of
Mysql was mostly because of the stack, thinking on how we want to interact with the information maybe cassandra could be
a better solution because of the quantity of information that we want to show.

### Versioning

I could use `JPA` to generate the schema on the fly but that is a solution I would use on production. Decided that it
was better to have a versioning for the changes that the db is under. `Flyway` seems to be the easiest way to achieve
this because it's only about adding sql files using a naming convention.

## Message broker

*Why am I using a message broker instead of calling the services directly?*

That is so I don't couple the individual creations to the availability of the subscription service. Even if that service
is down you will be able to continue using the CRUD behaviour of a course / student.

## Cache

The idea of using cache when calling to other services is to relly on the information that I have on that cache instead
of going all the time to a service that could be down.

## Improvements

### Redis

As spring let me use `@Cacheable` annotation and decide later what was going to be my implementation and though it was
easier to use a file system cache for this assigment. In the future it would be better to use redis/memcache to save the
responses of the services. Also having some expire on the information that is not used often is another thing to do.


### Test

#### Static check

Adding a way to check the code static like sonar is a good way to enforce good practices and also to have min standards.
For example, we could check what is the coverage of the code and decide to continue or not with deploy / merge depeding
on that

#### Contract test

Thinking on different team working on different microservices is good to have contract tests to assure that different ms
can communicate with each other

### Microservices architecture

I didn't like how the endpoints looks like, that is mainly because I'm missing a `BFF` on top of our current ms that
work as an aggregator of the information. That way we don't have interactions between `ms` on the same level and also
endpoints could be a more `rest full`.

### Monitoring

#### Grafana

To have a way to show the prometheus metrics and give them a sense


#### Logs

We will need to index all logs in order to make it easy to look for them. Datadog or elasticsearch could work for this
purpose.

#### Tracing

As a call for creating a student/ course goes though lots of steps I believe that using a `traceid` along all the flow
should be an easy win.

We could later use `zipkin` to visualize how it impacts all the resources.
