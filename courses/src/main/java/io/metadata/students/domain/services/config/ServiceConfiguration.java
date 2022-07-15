package io.metadata.students.domain.services.config;

import io.metadata.students.domain.ports.output.CourseMessageSender;
import io.metadata.students.domain.ports.output.CourseOutputPort;
import io.metadata.students.domain.services.CourseService;
import io.metadata.students.domain.services.mapper.ServiceMapper;
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
