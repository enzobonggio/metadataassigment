package io.metadata.subscriptions.adapters.output.persistence.exception;

import io.metadata.subscriptions.domain.model.StudentId;

public class DuplicatedStudentException extends RuntimeException
{
    private final StudentId id;

    public DuplicatedStudentException(final StudentId id)
    {
        this.id = id;
    }

    @Override
    public String getMessage()
    {
        return "Duplicated student id " + id;
    }
}
