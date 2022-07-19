package io.metadata.subscriptions.adapter.output

import io.metadata.api.courses.CourseResponse
import io.metadata.subscriptions.domain.model.CourseId
import io.metadata.subscriptions.domain.ports.output.CourseServicePort

class InMemoryCourseServiceAdapter implements CourseServicePort {
    @Override
    CourseResponse getById(final CourseId id) {
        return CourseResponse.builder()
                .id(id.value)
                .name("name_" + id.value)
                .build()
    }
}
