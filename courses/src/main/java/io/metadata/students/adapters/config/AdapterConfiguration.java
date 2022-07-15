package io.metadata.students.adapters.config;

import io.metadata.students.adapters.input.messageconsumer.CourseMessageConsumerAdapter;
import io.metadata.students.adapters.mapper.AdapterMapper;
import io.metadata.students.adapters.output.messagesender.InMemoryCourseMessageSenderAdapter;
import io.metadata.students.adapters.output.messagesender.CourseMessageSenderAdapter;
import io.metadata.students.adapters.output.persistence.CoursePersistenceAdapter;
import io.metadata.students.adapters.output.persistence.mapper.PersistenceMapper;
import io.metadata.students.adapters.output.persistence.repository.CourseRepository;
import io.metadata.students.domain.ports.input.UpdateCourseStateUseCase;
import io.metadata.students.domain.ports.output.CourseMessageSender;
import io.metadata.students.domain.ports.output.CourseOutputPort;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class AdapterConfiguration
{
    @Bean
    AdapterMapper restMapper()
    {
        return Mappers.getMapper(AdapterMapper.class);
    }

    @Bean
    @Profile("!kafka-less")
    CourseMessageSender courseMessageSenderAdapter(KafkaTemplate<String, byte[]> template)
    {
        return new CourseMessageSenderAdapter(template);
    }

    @Bean
    @Profile("kafka-less")
    CourseMessageSender inMemoryCourseMessageSenderAdapter()
    {
        return new InMemoryCourseMessageSenderAdapter();
    }

    @Bean
    CourseMessageConsumerAdapter courseMessageConsumerAdapter(UpdateCourseStateUseCase updateCourseStateUseCase, AdapterMapper mapper)
    {
        return new CourseMessageConsumerAdapter(updateCourseStateUseCase, mapper);
    }

    @Bean
    CourseOutputPort coursePersistenceAdapter(CourseRepository courseRepository)
    {
        return new CoursePersistenceAdapter(courseRepository, Mappers.getMapper(PersistenceMapper.class));
    }
}
