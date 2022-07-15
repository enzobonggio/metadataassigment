package io.metadata.students.adapters.config;

import io.metadata.students.adapters.input.messageconsumer.StudentMessageConsumerAdapter;
import io.metadata.students.adapters.mapper.AdapterMapper;
import io.metadata.students.adapters.output.messagesender.InMemoryStudentMessageSenderAdapter;
import io.metadata.students.adapters.output.messagesender.StudentMessageSenderAdapter;
import io.metadata.students.adapters.output.persistence.StudentPersistenceAdapter;
import io.metadata.students.adapters.output.persistence.mapper.PersistenceMapper;
import io.metadata.students.adapters.output.persistence.repository.StudentRepository;
import io.metadata.students.domain.ports.input.UpdateStudentStateUseCase;
import io.metadata.students.domain.ports.output.StudentMessageSender;
import io.metadata.students.domain.ports.output.StudentOutputPort;
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
    StudentMessageConsumerAdapter studentMessageConsumerAdapter(UpdateStudentStateUseCase updateStudentStateUseCase, AdapterMapper mapper)
    {
        return new StudentMessageConsumerAdapter(updateStudentStateUseCase, mapper);
    }

    @Bean
    StudentOutputPort studentPersistenceAdapter(StudentRepository studentRepository)
    {
        return new StudentPersistenceAdapter(studentRepository, Mappers.getMapper(PersistenceMapper.class));
    }
}
