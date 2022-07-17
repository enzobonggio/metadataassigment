package io.metadata.subscriptions.domain.services.config;

import io.metadata.subscriptions.domain.ports.output.CourseOutputPort;
import io.metadata.subscriptions.domain.ports.output.CourseServicePort;
import io.metadata.subscriptions.domain.ports.output.StudentOutputPort;
import io.metadata.subscriptions.domain.ports.output.StudentServicePort;
import io.metadata.subscriptions.domain.ports.output.SubscriptionMessageSender;
import io.metadata.subscriptions.domain.ports.output.SubscriptionOutputPort;
import io.metadata.subscriptions.domain.services.CourseService;
import io.metadata.subscriptions.domain.services.StudentService;
import io.metadata.subscriptions.domain.services.SubscriptionsByStudentService;
import io.metadata.subscriptions.domain.services.mapper.ServiceMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration
{
    @Bean
    StudentService studentService(StudentOutputPort studentOutputPort, SubscriptionMessageSender subscriptionMessageSender, StudentServicePort studentServicePort)
    {
        return new StudentService(studentOutputPort, subscriptionMessageSender, studentServicePort, Mappers.getMapper(ServiceMapper.class));
    }

    @Bean
    CourseService courseService(CourseOutputPort studentOutputPort, SubscriptionMessageSender subscriptionMessageSender, CourseServicePort courseServicePort)
    {
        return new CourseService(studentOutputPort, subscriptionMessageSender, courseServicePort, Mappers.getMapper(ServiceMapper.class));
    }

    @Bean
    SubscriptionsByStudentService sub(SubscriptionOutputPort subscriptionOutputPort, StudentServicePort studentServicePort, CourseServicePort courseServicePort)
    {
        return new SubscriptionsByStudentService(subscriptionOutputPort, studentServicePort, courseServicePort, Mappers.getMapper(ServiceMapper.class));
    }
}
