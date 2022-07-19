package io.metadata.subscriptions.adapter.config

import io.metadata.subscriptions.adapter.output.InMemoryCoursePersistenceAdapter
import io.metadata.subscriptions.adapter.output.InMemoryCourseServiceAdapter
import io.metadata.subscriptions.adapter.output.InMemoryStudentPersistenceAdapter
import io.metadata.subscriptions.adapter.output.InMemoryStudentServiceAdapter
import io.metadata.subscriptions.adapter.output.InMemorySubscriptionPersistenceAdapter
import io.metadata.subscriptions.adapters.output.persistence.mapper.PersistenceMapper
import org.mapstruct.factory.Mappers
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@TestConfiguration
class AdapterTestConfiguration {
    @Bean
    @Profile("test")
    InMemorySubscriptionPersistenceAdapter inMemorySubscriptionPersistenceAdapter() {
        return new InMemorySubscriptionPersistenceAdapter(Mappers.getMapper(PersistenceMapper.class))
    }

    @Bean
    @Profile("test")
    InMemoryCoursePersistenceAdapter inMemoryCoursePersistenceAdapter() {
        return new InMemoryCoursePersistenceAdapter(Mappers.getMapper(PersistenceMapper.class))
    }

    @Bean
    @Profile("test")
    InMemoryStudentPersistenceAdapter inMemoryStudentPersistenceAdapter() {
        return new InMemoryStudentPersistenceAdapter(Mappers.getMapper(PersistenceMapper.class))
    }

    @Bean
    @Profile("test")
    InMemoryStudentServiceAdapter inMemoryStudentServiceAdapter() {
        return new InMemoryStudentServiceAdapter()
    }

    @Bean
    @Profile("test")
    InMemoryCourseServiceAdapter inMemoryCourseServiceAdapter() {
        return new InMemoryCourseServiceAdapter()
    }
}
