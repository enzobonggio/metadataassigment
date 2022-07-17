package io.metadata.students.adapters.config;

import io.metadata.students.adapters.input.messageconsumer.StudentMessageConsumerAdapter;
import io.metadata.students.adapters.input.messageconsumer.mapper.MessageConsumerMapper;
import io.metadata.students.adapters.input.rest.mapper.RestMapper;
import io.metadata.students.adapters.output.messagesender.StudentMessageSenderAdapter;
import io.metadata.students.adapters.output.messagesender.InMemoryStudentMessageSenderAdapter;
import io.metadata.students.adapters.output.persistence.repository.StudentRepository;
import io.metadata.students.domain.ports.output.StudentMessageSender;
import io.metadata.students.domain.ports.output.StudentOutputPort;
import io.metadata.students.adapters.input.messageconsumer.InMemoryStudentMessageConsumerAdapter;
import io.metadata.students.adapters.output.persistence.StudentPersistenceAdapter;
import io.metadata.students.adapters.output.persistence.mapper.PersistenceMapper;
import io.metadata.students.domain.ports.input.StudentMessageConsumer;
import io.metadata.students.domain.ports.input.UpdateStudentStateUseCase;
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
    StudentMessageSender studentMessageSenderAdapter(KafkaTemplate<String, byte[]> template)
    {
        return new StudentMessageSenderAdapter(template);
    }

    @Bean
    @Profile("kafka-less")
    StudentMessageSender inMemoryStudentMessageSenderAdapter()
    {
        return new InMemoryStudentMessageSenderAdapter();
    }

    @Bean
    @Profile("!kafka-less")
    StudentMessageConsumer studentMessageConsumerAdapter(UpdateStudentStateUseCase updateStudentStateUseCase)
    {
        return new StudentMessageConsumerAdapter(updateStudentStateUseCase, Mappers.getMapper(MessageConsumerMapper.class));
    }

    @Bean
    @Profile("kafka-less")
    StudentMessageConsumer inMemoryStudentMessageConsumerAdapter()
    {
        return new InMemoryStudentMessageConsumerAdapter();
    }

    @Bean
    @Profile("!test")
    StudentOutputPort studentPersistenceAdapter(StudentRepository studentRepository)
    {
        return new StudentPersistenceAdapter(studentRepository, Mappers.getMapper(PersistenceMapper.class));
    }
}
