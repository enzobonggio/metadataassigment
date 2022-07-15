package io.metadata.students.domain.services.config;

import io.metadata.students.domain.ports.output.StudentMessageSender;
import io.metadata.students.domain.ports.output.StudentOutputPort;
import io.metadata.students.domain.services.StudentService;
import io.metadata.students.domain.services.mapper.ServiceMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration
{
    @Bean
    StudentService studentService(StudentOutputPort studentOutputPort, StudentMessageSender studentMessageSender)
    {
        return new StudentService(studentOutputPort, studentMessageSender, Mappers.getMapper(ServiceMapper.class));
    }
}
