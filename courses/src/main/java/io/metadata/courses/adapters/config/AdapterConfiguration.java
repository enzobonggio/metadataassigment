package io.metadata.courses.adapters.config;

import io.metadata.courses.adapters.input.messageconsumer.CourseMessageConsumerAdapter;
import io.metadata.courses.adapters.input.messageconsumer.mapper.MessageConsumerMapper;
import io.metadata.courses.adapters.input.rest.mapper.RestMapper;
import io.metadata.courses.adapters.output.messagesender.CourseMessageSenderAdapter;
import io.metadata.courses.adapters.output.messagesender.InMemoryCourseMessageSenderAdapter;
import io.metadata.courses.adapters.output.persistence.repository.CourseRepository;
import io.metadata.courses.domain.ports.output.CourseMessageSender;
import io.metadata.courses.domain.ports.output.CourseOutputPort;
import io.metadata.courses.adapters.input.messageconsumer.InMemoryCourseMessageConsumerAdapter;
import io.metadata.courses.adapters.output.persistence.CoursePersistenceAdapter;
import io.metadata.courses.adapters.output.persistence.mapper.PersistenceMapper;
import io.metadata.courses.domain.ports.input.CourseMessageConsumer;
import io.metadata.courses.domain.ports.input.UpdateCourseStateUseCase;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class AdapterConfiguration
{
    @Bean
    RestMapper restMapper()
    {
        return Mappers.getMapper(RestMapper.class);
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
    @Profile("!kafka-less")
    CourseMessageConsumer courseMessageConsumerAdapter(UpdateCourseStateUseCase updateCourseStateUseCase)
    {
        return new CourseMessageConsumerAdapter(updateCourseStateUseCase, Mappers.getMapper(MessageConsumerMapper.class));
    }

    @Bean
    @Profile("kafka-less")
    CourseMessageConsumer inMemoryCourseMessageConsumerAdapter()
    {
        return new InMemoryCourseMessageConsumerAdapter();
    }

    @Bean
    @Profile("!test")
    CourseOutputPort coursePersistenceAdapter(CourseRepository courseRepository)
    {
        return new CoursePersistenceAdapter(courseRepository, Mappers.getMapper(PersistenceMapper.class));
    }
}
