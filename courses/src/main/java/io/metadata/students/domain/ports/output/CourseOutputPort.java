package io.metadata.students.domain.ports.output;

import io.metadata.api.commons.State;
import io.metadata.students.domain.model.Course;
import io.metadata.students.domain.model.CourseId;
import io.metadata.students.domain.model.CourseName;
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
