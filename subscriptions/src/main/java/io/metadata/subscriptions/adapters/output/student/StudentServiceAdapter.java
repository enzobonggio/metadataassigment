package io.metadata.subscriptions.adapters.output.student;

import io.metadata.api.students.StudentResponse;
import io.metadata.subscriptions.domain.model.StudentId;
import io.metadata.subscriptions.domain.ports.output.StudentServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
public class StudentServiceAdapter implements StudentServicePort
{
    final RestTemplate restTemplate;

    @Value("${student.url:http://localhost:8081/}")
    String url;

    @Value("${student.path:students}")
    String path;

    @Override
    @Cacheable(value = "student", key = "{#id.value}")
    public StudentResponse getById(final StudentId id)
    {
        val response = restTemplate.getForEntity(
            url + path + "/{id}", StudentResponse.class, id.getValue());
        return response.getBody();
    }
}
