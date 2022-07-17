package io.metadata.courses.adapters.config


import io.metadata.courses.adapters.output.InMemoryCoursePersistenceAdapter
import io.metadata.courses.adapters.output.persistence.mapper.PersistenceMapper
import org.mapstruct.factory.Mappers
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@TestConfiguration
class AdapterTestConfiguration {

    @Bean
    @Profile("test")
    InMemoryCoursePersistenceAdapter inMemoryCoursePersistenceAdapter() {
        return new InMemoryCoursePersistenceAdapter(Mappers.getMapper(PersistenceMapper.class))
    }

}
