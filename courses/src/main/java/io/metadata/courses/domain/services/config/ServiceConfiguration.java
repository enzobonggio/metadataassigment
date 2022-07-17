package io.metadata.courses.domain.services.config;

import io.metadata.courses.domain.ports.output.CourseMessageSender;
import io.metadata.courses.domain.ports.output.CourseOutputPort;
import io.metadata.courses.domain.services.CourseService;
import io.metadata.courses.domain.services.mapper.ServiceMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration
{
    @Bean
    CourseService courseService(CourseOutputPort courseOutputPort, CourseMessageSender courseMessageSender)
    {
        return new CourseService(courseOutputPort, courseMessageSender, Mappers.getMapper(ServiceMapper.class));
    }
}
