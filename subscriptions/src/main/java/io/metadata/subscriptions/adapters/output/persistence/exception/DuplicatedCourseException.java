package io.metadata.subscriptions.adapters.output.persistence.exception;

import io.metadata.subscriptions.domain.model.CourseId;

public class DuplicatedCourseException extends RuntimeException
{
    private final CourseId id;

    public DuplicatedCourseException(final CourseId id)
    {
        this.id = id;
    }

    @Override
    public String getMessage()
    {
        return "Duplicated student id " + id;
    }
}
