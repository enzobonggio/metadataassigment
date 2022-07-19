package io.metadata.subscriptions.adapter.output

import io.metadata.api.students.StudentResponse
import io.metadata.subscriptions.domain.model.StudentId
import io.metadata.subscriptions.domain.ports.output.StudentServicePort

class InMemoryStudentServiceAdapter implements StudentServicePort {
    @Override
    StudentResponse getById(final StudentId id) {
        return StudentResponse.builder()
                .id(id.value)
                .name("name_" + id.value)
                .build()
    }
}
