package io.metadata.subscriptions.adapters.config;

import io.metadata.subscriptions.adapters.input.mapper.RestMapper;
import io.metadata.subscriptions.adapters.input.messageconsumer.SubscriptionMessageConsumerAdapter;
import io.metadata.subscriptions.adapters.output.course.CourseServiceAdapter;
import io.metadata.subscriptions.adapters.output.messagesender.InMemorySubscriptionMessageSenderAdapter;
import io.metadata.subscriptions.adapters.output.messagesender.SubscriptionMessageSenderAdapter;
import io.metadata.subscriptions.adapters.output.persistence.CoursePersistenceAdapter;
import io.metadata.subscriptions.adapters.output.persistence.StudentPersistenceAdapter;
import io.metadata.subscriptions.adapters.output.persistence.SubscriptionPersistenceAdapter;
import io.metadata.subscriptions.adapters.output.persistence.mapper.PersistenceMapper;
import io.metadata.subscriptions.adapters.output.persistence.repository.CourseRepository;
import io.metadata.subscriptions.adapters.output.persistence.repository.StudentRepository;
import io.metadata.subscriptions.adapters.output.persistence.repository.SubscriptionRepository;
import io.metadata.subscriptions.adapters.output.student.StudentServiceAdapter;
import io.metadata.subscriptions.domain.ports.input.CreateCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.CreateStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.DeleteCourseUseCase;
import io.metadata.subscriptions.domain.ports.input.DeleteStudentUseCase;
import io.metadata.subscriptions.domain.ports.input.SubscriptionMessageConsumer;
import io.metadata.subscriptions.domain.ports.output.CourseOutputPort;
import io.metadata.subscriptions.domain.ports.output.CourseServicePort;
import io.metadata.subscriptions.domain.ports.output.StudentOutputPort;
import io.metadata.subscriptions.domain.ports.output.StudentServicePort;
import io.metadata.subscriptions.domain.ports.output.SubscriptionMessageSender;
import io.metadata.subscriptions.domain.ports.output.SubscriptionOutputPort;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class AdapterConfiguration
{
    @Bean
    @Profile("!kafka-less")
    SubscriptionMessageSender courseMessageSenderAdapter(KafkaTemplate<String, byte[]> template)
    {
        return new SubscriptionMessageSenderAdapter(template);
    }

    @Bean
    @Profile("kafka-less")
    SubscriptionMessageSender inMemorySubscriptionMessageSenderAdapter()
    {
        return new InMemorySubscriptionMessageSenderAdapter();
    }

    @Bean
    RestMapper restMapper()
    {
        return Mappers.getMapper(RestMapper.class);
    }

    @Bean
    SubscriptionOutputPort subscriptionPersistenceAdapter(SubscriptionRepository subscriptionRepository)
    {
        return new SubscriptionPersistenceAdapter(subscriptionRepository, Mappers.getMapper(PersistenceMapper.class));
    }

    @Bean
    StudentOutputPort studentPersistenceAdapter(StudentRepository studentRepository)
    {
        return new StudentPersistenceAdapter(studentRepository, Mappers.getMapper(PersistenceMapper.class));
    }

    @Bean
    CourseOutputPort coursePersistenceAdapter(CourseRepository courseRepository)
    {
        return new CoursePersistenceAdapter(courseRepository, Mappers.getMapper(PersistenceMapper.class));
    }

    @Bean
    SubscriptionMessageConsumer subscriptionMessageConsumer(
        CreateStudentUseCase createStudentUseCase,
        CreateCourseUseCase createCourseUseCase,
        DeleteStudentUseCase deleteStudentUseCase,
        DeleteCourseUseCase deleteCourseUseCase)
    {
        return new SubscriptionMessageConsumerAdapter(
            createStudentUseCase,
            deleteStudentUseCase,
            createCourseUseCase,
            deleteCourseUseCase,
            Mappers.getMapper(RestMapper.class));
    }

    @Bean
    StudentServicePort studentServiceAdapter(RestTemplateBuilder builder)
    {
        return new StudentServiceAdapter(builder.build());
    }

    @Bean
    CourseServicePort courseServiceAdapter(RestTemplateBuilder builder)
    {
        return new CourseServiceAdapter(builder.build());
    }
}
