package io.metadata.students.adapters.config


import io.metadata.students.adapters.output.InMemoryStudentPersistenceAdapter
import io.metadata.students.adapters.output.persistence.mapper.PersistenceMapper
import org.mapstruct.factory.Mappers
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

@TestConfiguration
class AdapterTestConfiguration {

    @Bean
    @Profile("test")
    InMemoryStudentPersistenceAdapter inMemoryStudentPersistenceAdapter() {
        return new InMemoryStudentPersistenceAdapter(Mappers.getMapper(PersistenceMapper.class))
    }

}
