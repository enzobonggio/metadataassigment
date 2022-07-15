package io.metadata.subscriptions.adapters.output.course;

import io.metadata.api.courses.CourseResponse;
import io.metadata.subscriptions.domain.model.CourseId;
import io.metadata.subscriptions.domain.ports.output.CourseServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
public class CourseServiceAdapter implements CourseServicePort
{

    final RestTemplate restTemplate;

    @Value("${course.url:http://localhost:8080/}")
    String url;

    @Value("${course.path:courses}")
    String path;

    @Override
    @Cacheable(value = "course", key = "{#id.value}")
    public CourseResponse getById(final CourseId id)
    {
        val response = restTemplate.getForEntity(
            url + path + "/{id}", CourseResponse.class, id.getValue());
        return response.getBody();
    }
}
