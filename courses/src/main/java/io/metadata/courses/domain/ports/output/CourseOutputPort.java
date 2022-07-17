package io.metadata.courses.domain.ports.output;

import io.metadata.api.commons.State;
import io.metadata.courses.domain.model.Course;
import io.metadata.courses.domain.model.CourseId;
import io.metadata.courses.domain.model.CourseName;
import java.util.Collection;

public interface CourseOutputPort
{
    Course save(CourseName name);

    void update(Course course);

    void updateState(CourseId id, State state);

    Course getById(CourseId id);

    void deleteById(CourseId id);

    Collection<Course> fetch();
}
