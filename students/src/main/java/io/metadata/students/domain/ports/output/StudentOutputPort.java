package io.metadata.students.domain.ports.output;

import io.metadata.api.commons.State;
import io.metadata.students.domain.model.Student;
import io.metadata.students.domain.model.StudentId;
import io.metadata.students.domain.model.StudentName;
import java.util.Collection;

public interface StudentOutputPort
{
    Student save(StudentName name);

    void update(Student student);

    void updateState(StudentId id, State state);

    Student getById(StudentId id);

    void deleteById(StudentId id);

    Collection<Student> fetch();
}
