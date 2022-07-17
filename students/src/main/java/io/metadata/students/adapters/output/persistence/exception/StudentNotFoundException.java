package io.metadata.students.adapters.output.persistence.exception;

public class StudentNotFoundException extends RuntimeException
{
    private final Long id;

    public StudentNotFoundException(final Long id)
    {
        this.id = id;
    }

    @Override
    public String getMessage()
    {
        return "Student with id " + id + " not found";
    }
}
