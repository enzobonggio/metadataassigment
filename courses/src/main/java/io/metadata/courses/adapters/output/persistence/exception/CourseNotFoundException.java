package io.metadata.courses.adapters.output.persistence.exception;

public class CourseNotFoundException extends RuntimeException
{
    private final Long id;

    public CourseNotFoundException(final Long id)
    {
        this.id = id;
    }

    @Override
    public String getMessage()
    {
        return "Course with id " + id + " not found";
    }
}
